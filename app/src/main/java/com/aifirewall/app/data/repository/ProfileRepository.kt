package com.aifirewall.app.data.repository

import android.content.Context
import com.aifirewall.app.data.local.db.FirewallDatabase
import com.aifirewall.app.data.local.db.entity.FirewallProfileEntity
import kotlinx.coroutines.flow.Flow

class ProfileRepository(context: Context) {
    private val database = FirewallDatabase.getDatabase(context)
    private val profileDao = database.firewallProfileDao()
    private val ruleDao = database.appRuleDao()

    fun getAllProfilesFlow(): Flow<List<FirewallProfileEntity>> {
        return profileDao.getAllProfilesFlow()
    }

    suspend fun createProfile(name: String, description: String = ""): String {
        val newProfile = FirewallProfileEntity(
            name = name,
            description = description,
            isActive = false
        )
        profileDao.insertProfile(newProfile)
        return newProfile.id
    }

    suspend fun duplicateProfile(sourceId: String, newName: String) {
        val source = profileDao.getProfileById(sourceId) ?: return
        val newProfile = FirewallProfileEntity(
            name = newName,
            description = source.description + " (Copy)",
            isActive = false
        )
        profileDao.insertProfile(newProfile)

        val rules = ruleDao.getRulesForProfile(sourceId)
        val newRules = rules.map {
            it.copy(id = 0, profileId = newProfile.id) // Ensure id=0 for autoGenerate
        }
        ruleDao.insertRules(newRules)
    }

    suspend fun updateProfile(profile: FirewallProfileEntity) {
        profileDao.updateProfile(profile.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteProfile(profile: FirewallProfileEntity) {
        val allProfiles = profileDao.getAllProfiles()
        if (allProfiles.size <= 1) {
            // Do not delete the last remaining profile
            return
        }
        
        if (profile.isActive) {
            // Find another profile to activate
            val fallback = allProfiles.firstOrNull { it.id != profile.id }
            if (fallback != null) {
                profileDao.activateProfile(fallback.id)
            }
        }
        
        profileDao.deleteProfile(profile)
    }

    suspend fun activateProfile(profileId: String) {
        profileDao.activateProfile(profileId)
    }
}
