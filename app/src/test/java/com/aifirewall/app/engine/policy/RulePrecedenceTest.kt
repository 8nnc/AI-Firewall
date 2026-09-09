package com.aifirewall.app.engine.policy

import android.content.Context
import com.aifirewall.app.data.local.db.entity.AppRuleEntity
import com.aifirewall.app.data.local.db.entity.GroupMemberEntity
import com.aifirewall.app.data.local.db.entity.RuleGroupEntity
import com.aifirewall.app.data.repository.AppRuleRepository
import com.aifirewall.app.domain.model.NetworkPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RulePrecedenceTest {

    @Mock
    private lateinit var mockContext: Context

    private lateinit var repository: AppRuleRepository

    @Before
    fun setup() {
        org.mockito.MockitoAnnotations.openMocks(this)
        repository = AppRuleRepository(mockContext)
    }

    @After
    fun tearDown() {
        repository.close()
    }

    @Test
    fun testAppRuleOverridesGroupRule() {
        val packageName = "com.example.app"
        val uid = 10050

        // App Rule: ALLOW
        val appRule = AppRuleEntity(
            profileId = "default",
            packageName = packageName,
            uid = uid,
            wifiPolicy = NetworkPolicy.ALLOW.name,
            mobileDataPolicy = NetworkPolicy.ALLOW.name
        )

        // Group Rule: BLOCK_WIFI
        val group = RuleGroupEntity(
            id = "group1",
            name = "Strict Group",
            wifiPolicy = NetworkPolicy.BLOCK.name,
            mobileDataPolicy = NetworkPolicy.BLOCK.name
        )
        val member = GroupMemberEntity(groupId = "group1", packageName = packageName)

        val resolved = repository.resolvePolicy(
            packageName = packageName,
            uid = uid,
            appRule = appRule,
            groups = listOf(group),
            memberships = listOf(member)
        )

        // App rule (ALLOW) MUST win over Group rule (BLOCK)
        assertEquals(NetworkPolicy.ALLOW, resolved.wifiPolicy)
        assertEquals(NetworkPolicy.ALLOW, resolved.mobileDataPolicy)
    }

    @Test
    fun testGroupRuleAppliesWhenAppRuleUnset() {
        val packageName = "com.example.app"
        val uid = 10050

        // App Rule: UNSET
        val appRule = AppRuleEntity(
            profileId = "default",
            packageName = packageName,
            uid = uid,
            wifiPolicy = NetworkPolicy.UNSET.name,
            mobileDataPolicy = NetworkPolicy.UNSET.name
        )

        // Group Rule: BLOCK_WIFI, ALLOW_MOBILE
        val group = RuleGroupEntity(
            id = "group1",
            name = "Social Media Group",
            wifiPolicy = NetworkPolicy.BLOCK.name,
            mobileDataPolicy = NetworkPolicy.ALLOW.name
        )
        val member = GroupMemberEntity(groupId = "group1", packageName = packageName)

        val resolved = repository.resolvePolicy(
            packageName = packageName,
            uid = uid,
            appRule = appRule,
            groups = listOf(group),
            memberships = listOf(member)
        )

        assertEquals(NetworkPolicy.BLOCK, resolved.wifiPolicy)
        assertEquals(NetworkPolicy.ALLOW, resolved.mobileDataPolicy)
    }

    @Test
    fun testConflictingGroupRulesBlockTakesPrecedence() {
        val packageName = "com.example.app"
        val uid = 10050

        // Group 1: ALLOW wifi
        val group1 = RuleGroupEntity(id = "g1", name = "Group 1", wifiPolicy = NetworkPolicy.ALLOW.name)
        // Group 2: BLOCK wifi
        val group2 = RuleGroupEntity(id = "g2", name = "Group 2", wifiPolicy = NetworkPolicy.BLOCK.name)

        val members = listOf(
            GroupMemberEntity(groupId = "g1", packageName = packageName),
            GroupMemberEntity(groupId = "g2", packageName = packageName)
        )

        val resolved = repository.resolvePolicy(
            packageName = packageName,
            uid = uid,
            appRule = null,
            groups = listOf(group1, group2),
            memberships = members
        )

        // BLOCK in any group wins over ALLOW
        assertEquals(NetworkPolicy.BLOCK, resolved.wifiPolicy)
    }

    @Test
    fun testDefaultPolicyWhenNoAppRuleAndNoGroup() {
        val packageName = "com.example.app"
        val uid = 10050

        val resolved = repository.resolvePolicy(
            packageName = packageName,
            uid = uid,
            appRule = null,
            groups = emptyList(),
            memberships = emptyList()
        )

        assertEquals(NetworkPolicy.ALLOW, resolved.wifiPolicy)
        assertEquals(NetworkPolicy.ALLOW, resolved.mobileDataPolicy)
    }
}
