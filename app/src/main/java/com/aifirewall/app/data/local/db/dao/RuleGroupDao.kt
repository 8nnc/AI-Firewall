package com.aifirewall.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aifirewall.app.data.local.db.entity.GroupMemberEntity
import com.aifirewall.app.data.local.db.entity.RuleGroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleGroupDao {

    @Query("SELECT * FROM rule_groups")
    fun getAllGroupsFlow(): Flow<List<RuleGroupEntity>>
    
    @Query("SELECT * FROM group_members")
    fun getAllGroupMembersFlow(): Flow<List<GroupMemberEntity>>
    
    @Query("SELECT * FROM rule_groups")
    suspend fun getAllGroups(): List<RuleGroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: RuleGroupEntity)

    @Delete
    suspend fun deleteGroup(group: RuleGroupEntity)

    @Query("SELECT packageName FROM group_members WHERE groupId = :groupId")
    fun getGroupMembersFlow(groupId: String): Flow<List<String>>

    @Query("SELECT packageName FROM group_members WHERE groupId = :groupId")
    suspend fun getGroupMembers(groupId: String): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMember(member: GroupMemberEntity)

    @Query("DELETE FROM group_members WHERE groupId = :groupId AND packageName = :packageName")
    suspend fun removeMember(groupId: String, packageName: String)

    @Transaction
    suspend fun addMembers(members: List<GroupMemberEntity>) {
        members.forEach { addMember(it) }
    }
}
