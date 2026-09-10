package com.aifirewall.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aifirewall.app.data.local.db.entity.AppRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppRuleDao {

    @Query("SELECT * FROM app_rules WHERE profileId = :profileId")
    fun getRulesForProfileFlow(profileId: String): Flow<List<AppRuleEntity>>
    
    @Query("SELECT * FROM app_rules WHERE profileId = :profileId")
    suspend fun getRulesForProfile(profileId: String): List<AppRuleEntity>

    @Query("SELECT * FROM app_rules WHERE profileId = :profileId AND packageName = :packageName LIMIT 1")
    suspend fun getRule(profileId: String, packageName: String): AppRuleEntity?

    @Query("SELECT * FROM app_rules WHERE profileId = :profileId AND packageName = :packageName LIMIT 1")
    fun getRuleFlow(profileId: String, packageName: String): Flow<AppRuleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: AppRuleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRules(rules: List<AppRuleEntity>)

    @Query("DELETE FROM app_rules WHERE profileId = :profileId AND packageName = :packageName")
    suspend fun deleteRule(profileId: String, packageName: String)
}
