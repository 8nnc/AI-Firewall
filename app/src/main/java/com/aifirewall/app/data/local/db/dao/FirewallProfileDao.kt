package com.aifirewall.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.aifirewall.app.data.local.db.entity.FirewallProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FirewallProfileDao {

    @Query("SELECT * FROM firewall_profiles ORDER BY createdAt ASC")
    fun getAllProfilesFlow(): Flow<List<FirewallProfileEntity>>
    
    @Query("SELECT * FROM firewall_profiles")
    suspend fun getAllProfiles(): List<FirewallProfileEntity>

    @Query("SELECT * FROM firewall_profiles WHERE id = :id")
    suspend fun getProfileById(id: String): FirewallProfileEntity?

    @Query("SELECT * FROM firewall_profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveProfileFlow(): Flow<FirewallProfileEntity?>
    
    @Query("SELECT * FROM firewall_profiles WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveProfile(): FirewallProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: FirewallProfileEntity)

    @Update
    suspend fun updateProfile(profile: FirewallProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: FirewallProfileEntity)

    @Transaction
    suspend fun activateProfile(profileId: String) {
        // Deactivate all
        deactivateAll()
        // Activate selected
        setActive(profileId, true)
    }

    @Query("UPDATE firewall_profiles SET isActive = 0")
    suspend fun deactivateAll()

    @Query("UPDATE firewall_profiles SET isActive = :isActive WHERE id = :id")
    suspend fun setActive(id: String, isActive: Boolean)
}
