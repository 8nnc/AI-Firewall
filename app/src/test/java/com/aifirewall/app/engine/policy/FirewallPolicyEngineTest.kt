package com.aifirewall.app.engine.policy

import android.content.Context
import com.aifirewall.app.domain.model.AppNetworkRule
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.engine.network.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Field

@RunWith(MockitoJUnitRunner.Silent::class)
class FirewallPolicyEngineTest {

    @Mock
    private lateinit var mockContext: Context
    
    private lateinit var policyEngine: FirewallPolicyEngine

    @Before
    fun setup() {
        org.mockito.MockitoAnnotations.openMocks(this)
        org.mockito.Mockito.`when`(mockContext.applicationContext).thenReturn(mockContext)
        val mockRepo = org.mockito.Mockito.mock(com.aifirewall.app.data.repository.AppRuleRepository::class.java)
        org.mockito.Mockito.`when`(mockRepo.getAllRulesFlow()).thenReturn(kotlinx.coroutines.flow.emptyFlow())
        policyEngine = FirewallPolicyEngine(mockContext, mockRepo)
    }

    @After
    fun tearDown() {
        policyEngine.close()
    }

    private fun setRules(rules: List<AppNetworkRule>) {
        val byUid = rules.filter { it.uid > 0 }.associateBy { it.uid }
        val byPkg = rules.associateBy { it.packageName }
        val managed = rules.filter { it.wifiPolicy == NetworkPolicy.BLOCK || it.mobileDataPolicy == NetworkPolicy.BLOCK }
            .map { it.packageName }.toSet()

        val snapshotField: Field = FirewallPolicyEngine::class.java.getDeclaredField("_snapshotFlow")
        snapshotField.isAccessible = true
        val snapshotFlow = snapshotField.get(policyEngine) as MutableStateFlow<PolicySnapshot>
        snapshotFlow.value = PolicySnapshot(
            rulesByUid = byUid,
            rulesByPackage = byPkg,
            managedPackages = managed,
            unknownAppPolicy = NetworkPolicy.BLOCK,
            version = 1L
        )

        val rulesField: Field = FirewallPolicyEngine::class.java.getDeclaredField("_rulesFlow")
        rulesField.isAccessible = true
        val rulesFlow = rulesField.get(policyEngine) as MutableStateFlow<List<AppNetworkRule>>
        rulesFlow.value = rules
    }

    @Test
    fun testAllowPolicy() {
        setRules(listOf(
            AppNetworkRule("com.test.app", 1001, NetworkPolicy.ALLOW, NetworkPolicy.ALLOW)
        ))
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.MOBILE))
        
        val managed = policyEngine.getManagedPackages()
        assertTrue(managed.isEmpty())
    }

    @Test
    fun testBlockWifiPolicy() {
        setRules(listOf(
            AppNetworkRule("com.test.app", 1001, NetworkPolicy.BLOCK, NetworkPolicy.ALLOW)
        ))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.MOBILE))
        
        val managed = policyEngine.getManagedPackages()
        assertTrue(managed.contains("com.test.app"))
    }

    @Test
    fun testBlockMobilePolicy() {
        setRules(listOf(
            AppNetworkRule("com.test.app", 1001, NetworkPolicy.ALLOW, NetworkPolicy.BLOCK)
        ))
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.MOBILE))
        
        val managed = policyEngine.getManagedPackages()
        assertTrue(managed.contains("com.test.app"))
    }

    @Test
    fun testBlockBothPolicy() {
        setRules(listOf(
            AppNetworkRule("com.test.app", 1001, NetworkPolicy.BLOCK, NetworkPolicy.BLOCK)
        ))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.MOBILE))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.NONE))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.UNKNOWN))
    }

    @Test
    fun testUnknownUidHandling() {
        // UID = -1 or invalid UID must evaluate to UNKNOWN_APPLICATION policy (BLOCK)
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(-1, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(0, NetworkMonitor.NetworkType.MOBILE))
        assertEquals(AppNetworkRule.UNKNOWN_PACKAGE, policyEngine.resolvePackageForUid(-1))
        assertEquals(AppNetworkRule.UNKNOWN_PACKAGE, policyEngine.resolvePackageForUid(0))
    }

    @Test
    fun testInstalledAppWithoutBlockingRuleDefaultsToAllow() {
        // App 9999 is installed but has no custom blocking rule
        setRules(listOf(
            AppNetworkRule("com.test.app", 1001, NetworkPolicy.BLOCK, NetworkPolicy.BLOCK)
        ))
        
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(9999, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(9999, NetworkMonitor.NetworkType.MOBILE))
    }

    @Test
    fun testMultiApplicationIsolation() {
        setRules(listOf(
            AppNetworkRule("com.test.appA", 1001, NetworkPolicy.BLOCK, NetworkPolicy.BLOCK),
            AppNetworkRule("com.test.appB", 1002, NetworkPolicy.ALLOW, NetworkPolicy.ALLOW),
            AppNetworkRule("com.test.appC", 1003, NetworkPolicy.BLOCK, NetworkPolicy.ALLOW),
            AppNetworkRule("com.test.appD", 1004, NetworkPolicy.ALLOW, NetworkPolicy.BLOCK)
        ))

        // App A is completely blocked
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1001, NetworkMonitor.NetworkType.MOBILE))

        // App B is allowed everywhere
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(1002, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(1002, NetworkMonitor.NetworkType.MOBILE))

        // App C is wifi blocked only
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1003, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(1003, NetworkMonitor.NetworkType.MOBILE))

        // App D is mobile blocked only
        assertEquals(NetworkPolicy.ALLOW, policyEngine.evaluate(1004, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.BLOCK, policyEngine.evaluate(1004, NetworkMonitor.NetworkType.MOBILE))
    }
}
