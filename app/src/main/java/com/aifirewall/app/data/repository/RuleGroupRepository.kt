package com.aifirewall.app.data.repository

import android.content.Context
import com.aifirewall.app.data.local.db.FirewallDatabase
import com.aifirewall.app.data.local.db.entity.RuleGroupEntity
import com.aifirewall.app.data.local.db.entity.GroupMemberEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class RuleGroupRepository(context: Context) {
    private val database = FirewallDatabase.getDatabase(context)
    private val groupDao = database.ruleGroupDao()

    fun getAllGroupsFlow(): Flow<List<RuleGroupEntity>> {
        return groupDao.getAllGroupsFlow()
    }

    suspend fun createGroup(name: String, description: String = ""): String {
        val group = RuleGroupEntity(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description
        )
        groupDao.insertGroup(group)
        return group.id
    }

    suspend fun deleteGroup(group: RuleGroupEntity) {
        groupDao.deleteGroup(group)
    }

    fun getGroupMembersFlow(groupId: String): Flow<List<String>> {
        return groupDao.getGroupMembersFlow(groupId)
    }

    suspend fun addMemberToGroup(groupId: String, packageName: String) {
        groupDao.addMember(GroupMemberEntity(groupId = groupId, packageName = packageName))
    }

    suspend fun removeMemberFromGroup(groupId: String, packageName: String) {
        groupDao.removeMember(groupId, packageName)
    }
}
