package com.aifirewall.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aifirewall.app.data.local.db.FirewallDatabase
import com.aifirewall.app.data.local.db.entity.AppRuleEntity
import com.aifirewall.app.data.local.db.entity.FirewallProfileEntity
import com.aifirewall.app.domain.model.AppNetworkRule
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.data.local.db.entity.GroupMemberEntity
import com.aifirewall.app.data.local.db.entity.RuleGroupEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.Closeable
import java.util.UUID

private val Context.rulesDataStore: DataStore<Preferences> by preferencesDataStore(name = "network_rules")

class AppRuleRepository(private val context: Context) : Closeable {

    private val database by lazy { FirewallDatabase.getDatabase(context) }
    private val ruleDao by lazy { database.appRuleDao() }
    private val profileDao by lazy { database.firewallProfileDao() }
    private val groupDao by lazy { database.ruleGroupDao() }
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var migrationJob: Job? = null

    init {
        migrationJob = repositoryScope.launch {
            try {
                migrateDataStoreToRoom()
            } catch (_: Exception) {
                // Safe fallback for unit testing environments
            } finally {
                // Auto-cancel the repository scope once one-off migration terminates
                repositoryScope.cancel()
            }
        }
    }

    override fun close() {
        migrationJob?.cancel()
        repositoryScope.cancel()
    }

    private suspend fun migrateDataStoreToRoom() {
        val prefs = context.rulesDataStore.data.firstOrNull() ?: return
        val allKeys = prefs.asMap().keys
        if (allKeys.isEmpty()) return

        // We have DataStore rules to migrate
        var defaultProfile = profileDao.getActiveProfile()
        if (defaultProfile == null) {
            defaultProfile = FirewallProfileEntity(
                id = UUID.randomUUID().toString(),
                name = "Default",
                description = "Default Firewall Profile",
                isActive = true
            )
            profileDao.insertProfile(defaultProfile)
        }

        val rulesToInsert = mutableListOf<AppRuleEntity>()
        val packages = allKeys.map { key -> key.name.substringBefore("_") }.toSet()
        
        for (pkg in packages) {
            val wifiKey = stringPreferencesKey("${pkg}_wifi")
            val mobileKey = stringPreferencesKey("${pkg}_mobile")
            val uidKey = stringPreferencesKey("${pkg}_uid")

            if (prefs.contains(wifiKey) || prefs.contains(mobileKey)) {
                val wifiStr = prefs[wifiKey] as? String ?: NetworkPolicy.ALLOW.name
                val mobileStr = prefs[mobileKey] as? String ?: NetworkPolicy.ALLOW.name
                val uidStr = prefs[uidKey] as? String ?: "0"
                
                rulesToInsert.add(
                    AppRuleEntity(
                        profileId = defaultProfile.id,
                        packageName = pkg,
                        uid = uidStr.toIntOrNull() ?: 0,
                        wifiPolicy = wifiStr,
                        mobileDataPolicy = mobileStr
                    )
                )
            }
        }

        if (rulesToInsert.isNotEmpty()) {
            ruleDao.insertRules(rulesToInsert)
        }

        // Clear DataStore after successful migration
        context.rulesDataStore.edit { it.clear() }
    }

    suspend fun getActiveProfileId(): String {
        var active = profileDao.getActiveProfile()
        if (active == null) {
            val id = UUID.randomUUID().toString()
            active = FirewallProfileEntity(id = id, name = "Default", isActive = true)
            profileDao.insertProfile(active)
        }
        return active.id
    }

    internal fun resolvePolicy(
        packageName: String,
        uid: Int,
        appRule: AppRuleEntity?,
        groups: List<RuleGroupEntity>,
        memberships: List<GroupMemberEntity>
    ): AppNetworkRule {
        var finalWifi = NetworkPolicy.UNSET
        var finalMobile = NetworkPolicy.UNSET

        // 1. App Rule has highest priority
        if (appRule != null) {
            val appWifi = try { NetworkPolicy.valueOf(appRule.wifiPolicy) } catch (e: Exception) { NetworkPolicy.UNSET }
            val appMobile = try { NetworkPolicy.valueOf(appRule.mobileDataPolicy) } catch (e: Exception) { NetworkPolicy.UNSET }
            if (appWifi != NetworkPolicy.UNSET) finalWifi = appWifi
            if (appMobile != NetworkPolicy.UNSET) finalMobile = appMobile
        }

        // 2. Group Rules (if any are applicable)
        if (finalWifi == NetworkPolicy.UNSET || finalMobile == NetworkPolicy.UNSET) {
            val appGroupIds = memberships.filter { it.packageName == packageName }.map { it.groupId }.toSet()
            val applicableGroups = groups.filter { it.id in appGroupIds }

            var groupFinalWifi = NetworkPolicy.UNSET
            var groupFinalMobile = NetworkPolicy.UNSET

            for (group in applicableGroups) {
                val groupWifi = try { NetworkPolicy.valueOf(group.wifiPolicy) } catch (e: Exception) { NetworkPolicy.UNSET }
                val groupMobile = try { NetworkPolicy.valueOf(group.mobileDataPolicy) } catch (e: Exception) { NetworkPolicy.UNSET }

                if (groupWifi == NetworkPolicy.BLOCK) groupFinalWifi = NetworkPolicy.BLOCK
                else if (groupWifi == NetworkPolicy.ALLOW && groupFinalWifi == NetworkPolicy.UNSET) groupFinalWifi = NetworkPolicy.ALLOW

                if (groupMobile == NetworkPolicy.BLOCK) groupFinalMobile = NetworkPolicy.BLOCK
                else if (groupMobile == NetworkPolicy.ALLOW && groupFinalMobile == NetworkPolicy.UNSET) groupFinalMobile = NetworkPolicy.ALLOW
            }
            if (finalWifi == NetworkPolicy.UNSET) finalWifi = groupFinalWifi
            if (finalMobile == NetworkPolicy.UNSET) finalMobile = groupFinalMobile
        }

        // 3. Default Profile Policy
        if (finalWifi == NetworkPolicy.UNSET) finalWifi = NetworkPolicy.ALLOW
        if (finalMobile == NetworkPolicy.UNSET) finalMobile = NetworkPolicy.ALLOW

        return AppNetworkRule(
            packageName = packageName,
            uid = uid,
            wifiPolicy = finalWifi,
            mobileDataPolicy = finalMobile
        )
    }

    fun getRuleFlow(packageName: String, uid: Int): Flow<AppNetworkRule> = flow {
        val activeProfileId = getActiveProfileId()
        val appRuleFlow = ruleDao.getRuleFlow(activeProfileId, packageName)
        val allGroupsFlow = groupDao.getAllGroupsFlow()
        val allMembersFlow = groupDao.getAllGroupMembersFlow()

        emitAll(
            combine(appRuleFlow, allGroupsFlow, allMembersFlow) { appEntity, groups, members ->
                resolvePolicy(packageName, uid, appEntity, groups, members)
            }
        )
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun getAllRulesFlow(): Flow<Map<String, AppNetworkRule>> {
        return profileDao.getActiveProfileFlow().flatMapLatest { profile ->
            if (profile == null) {
                flowOf(emptyMap())
            } else {
                combine(
                    ruleDao.getRulesForProfileFlow(profile.id),
                    groupDao.getAllGroupsFlow(),
                    groupDao.getAllGroupMembersFlow()
                ) { appRules, groups, members ->
                    val rulesMap = mutableMapOf<String, AppNetworkRule>()
                    val packagesToResolve = mutableMapOf<String, Int>()
                    
                    // Add all apps with explicit rules
                    for (rule in appRules) {
                        packagesToResolve[rule.packageName] = rule.uid
                    }
                    
                    // Add all apps in any group
                    for (member in members) {
                        if (!packagesToResolve.containsKey(member.packageName)) {
                            var resolvedUid = 0
                            try {
                                val pm = context.packageManager
                                val packageInfo = pm?.getPackageInfo(member.packageName, 0)
                                resolvedUid = packageInfo?.applicationInfo?.uid ?: 0
                            } catch (e: Throwable) {
                                // Package not found or context uninitialized in tests
                            }
                            packagesToResolve[member.packageName] = resolvedUid
                        }
                    }
                    
                    for ((pkg, uid) in packagesToResolve) {
                        val appRule = appRules.find { it.packageName == pkg }
                        rulesMap[pkg] = resolvePolicy(
                            pkg,
                            uid,
                            appRule,
                            groups,
                            members
                        )
                    }
                    rulesMap
                }
            }
        }
    }

    suspend fun saveRule(rule: AppNetworkRule) {
        val profileId = getActiveProfileId()
        ruleDao.insertRule(
            AppRuleEntity(
                profileId = profileId,
                packageName = rule.packageName,
                uid = rule.uid,
                wifiPolicy = rule.wifiPolicy.name,
                mobileDataPolicy = rule.mobileDataPolicy.name
            )
        )
    }

    suspend fun saveRules(rules: List<AppNetworkRule>) {
        val profileId = getActiveProfileId()
        val entities = rules.map { rule ->
            AppRuleEntity(
                profileId = profileId,
                packageName = rule.packageName,
                uid = rule.uid,
                wifiPolicy = rule.wifiPolicy.name,
                mobileDataPolicy = rule.mobileDataPolicy.name
            )
        }
        ruleDao.insertRules(entities)
    }
}
