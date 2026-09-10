package com.aifirewall.app.engine.policy

import android.content.Context
import com.aifirewall.app.data.repository.AppRuleRepository
import com.aifirewall.app.domain.model.AppNetworkRule
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.engine.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.Closeable

/**
 * Immutable thread-safe snapshot of active policy configuration.
 */
data class PolicySnapshot(
    val rulesByUid: Map<Int, AppNetworkRule> = emptyMap(),
    val rulesByPackage: Map<String, AppNetworkRule> = emptyMap(),
    val managedPackages: Set<String> = emptySet(),
    val unknownAppPolicy: NetworkPolicy = NetworkPolicy.BLOCK,
    val version: Long = 0L
)

class FirewallPolicyEngine(
    context: Context,
    private val ruleRepository: AppRuleRepository = AppRuleRepository(context)
) : Closeable {
    private val _snapshotFlow = MutableStateFlow(PolicySnapshot())
    val snapshotFlow: StateFlow<PolicySnapshot> = _snapshotFlow.asStateFlow()

    private val _rulesFlow = MutableStateFlow<List<AppNetworkRule>>(emptyList())
    val rulesFlow: StateFlow<List<AppNetworkRule>> = _rulesFlow.asStateFlow()

    private val engineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var rulesCollectorJob: Job? = null

    val isClosed: Boolean
        get() = !engineScope.isActive

    init {
        rulesCollectorJob = engineScope.launch {
            var currentVersion = 0L
            ruleRepository.getAllRulesFlow().collectLatest { rulesMap ->
                currentVersion++
                val rulesList = rulesMap.values.toList()
                val byUid = mutableMapOf<Int, AppNetworkRule>()
                val byPkg = mutableMapOf<String, AppNetworkRule>()
                val managed = mutableSetOf<String>()

                for (rule in rulesList) {
                    byPkg[rule.packageName] = rule
                    if (rule.uid > 0) {
                        byUid[rule.uid] = rule
                    }
                    if (rule.wifiPolicy == NetworkPolicy.BLOCK || rule.mobileDataPolicy == NetworkPolicy.BLOCK) {
                        managed.add(rule.packageName)
                    }
                }

                _rulesFlow.value = rulesList
                _snapshotFlow.value = PolicySnapshot(
                    rulesByUid = byUid,
                    rulesByPackage = byPkg,
                    managedPackages = managed,
                    unknownAppPolicy = NetworkPolicy.BLOCK,
                    version = currentVersion
                )
            }
        }
    }

    override fun close() {
        rulesCollectorJob?.cancel()
        rulesCollectorJob = null
        engineScope.cancel()
        ruleRepository.close()
    }

    /**
     * Determines whether traffic for a given UID should be allowed or dropped.
     * Evaluates in O(1) time against the active PolicySnapshot without database or IPC operations.
     */
    fun evaluate(uid: Int, networkType: NetworkMonitor.NetworkType): NetworkPolicy {
        // Explicitly handle UNKNOWN or invalid UIDs
        if (uid <= 0 || uid == AppNetworkRule.UNKNOWN_UID) {
            return _snapshotFlow.value.unknownAppPolicy
        }

        val snapshot = _snapshotFlow.value
        val rule = snapshot.rulesByUid[uid]
            ?: return NetworkPolicy.ALLOW // Installed application with no explicit blocking rule defaults to ALLOW

        return evaluateRuleAgainstNetwork(rule, networkType)
    }

    /**
     * Evaluates policy for an explicit package name.
     */
    fun evaluatePackage(packageName: String, networkType: NetworkMonitor.NetworkType): NetworkPolicy {
        if (packageName.isBlank() || packageName == AppNetworkRule.UNKNOWN_PACKAGE) {
            return _snapshotFlow.value.unknownAppPolicy
        }

        val snapshot = _snapshotFlow.value
        val rule = snapshot.rulesByPackage[packageName]
            ?: return NetworkPolicy.ALLOW

        return evaluateRuleAgainstNetwork(rule, networkType)
    }

    private fun evaluateRuleAgainstNetwork(rule: AppNetworkRule, networkType: NetworkMonitor.NetworkType): NetworkPolicy {
        return when (networkType) {
            NetworkMonitor.NetworkType.WIFI -> rule.wifiPolicy
            NetworkMonitor.NetworkType.MOBILE -> rule.mobileDataPolicy
            NetworkMonitor.NetworkType.NONE, NetworkMonitor.NetworkType.UNKNOWN -> {
                if (rule.wifiPolicy == NetworkPolicy.BLOCK && rule.mobileDataPolicy == NetworkPolicy.BLOCK) {
                    NetworkPolicy.BLOCK
                } else {
                    NetworkPolicy.ALLOW
                }
            }
        }
    }

    /**
     * Fast in-memory package name lookup for a given UID.
     */
    fun resolvePackageForUid(uid: Int): String {
        if (uid <= 0 || uid == AppNetworkRule.UNKNOWN_UID) {
            return AppNetworkRule.UNKNOWN_PACKAGE
        }
        val snapshot = _snapshotFlow.value
        return snapshot.rulesByUid[uid]?.packageName ?: "UID_$uid"
    }

    /**
     * Returns a list of package names managed by the firewall (routed to TUN).
     */
    fun getManagedPackages(): List<String> {
        return _snapshotFlow.value.managedPackages.toList()
    }
}
