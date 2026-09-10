package com.aifirewall.app.data.repository

import android.content.Context
import android.net.Uri
import com.aifirewall.app.data.local.db.FirewallDatabase
import com.aifirewall.app.data.local.db.entity.AppRuleEntity
import com.aifirewall.app.data.local.db.entity.FirewallProfileEntity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.UUID

data class FirewallBackup(
    val version: Int = 1,
    val profiles: List<FirewallProfileEntity>,
    val rules: List<AppRuleEntity>
)

class ConfigurationManager(private val context: Context) {
    private val database = FirewallDatabase.getDatabase(context)
    private val profileDao = database.firewallProfileDao()
    private val ruleDao = database.appRuleDao()
    private val gson = Gson()

    suspend fun exportConfiguration(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val profiles = profileDao.getAllProfilesFlow().firstOrNull() ?: emptyList()
            // In a real app we might want to paginate, but we assume the number of rules is reasonable
            // We need a direct DAO method to get all rules for backup, or we can just fetch rules per profile.
            val allRules = mutableListOf<AppRuleEntity>()
            for (profile in profiles) {
                allRules.addAll(ruleDao.getRulesForProfile(profile.id))
            }

            val backup = FirewallBackup(
                profiles = profiles,
                rules = allRules
            )

            val json = gson.toJson(backup)
            
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(json)
                }
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importConfiguration(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            var json: String = ""
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    json = reader.readText()
                }
            }

            val backup = gson.fromJson(json, FirewallBackup::class.java)
            if (backup.version == 1) {
                // To avoid conflict, we can either wipe existing, or append.
                // Let's generate new IDs for profiles to avoid conflicts, and import them as new.
                for (oldProfile in backup.profiles) {
                    val newId = UUID.randomUUID().toString()
                    val newProfile = oldProfile.copy(
                        id = newId, 
                        name = oldProfile.name + " (Imported)",
                        isActive = false
                    )
                    profileDao.insertProfile(newProfile)
                    
                    val oldRules = backup.rules.filter { it.profileId == oldProfile.id }
                    val newRules = oldRules.map { 
                        it.copy(id = 0, profileId = newId) 
                    }
                    if (newRules.isNotEmpty()) {
                        ruleDao.insertRules(newRules)
                    }
                }
            } else {
                return@withContext Result.failure(Exception("Unsupported backup version"))
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
