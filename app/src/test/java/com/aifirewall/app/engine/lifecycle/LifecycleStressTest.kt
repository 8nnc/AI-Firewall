package com.aifirewall.app.engine.lifecycle

import android.content.Context
import com.aifirewall.app.data.repository.AppRuleRepository
import com.aifirewall.app.domain.model.AppNetworkRule
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.engine.network.NetworkMonitor
import com.aifirewall.app.engine.policy.FirewallPolicyEngine
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Field

@RunWith(MockitoJUnitRunner.Silent::class)
class LifecycleStressTest {

    @Mock
    private lateinit var mockContext: Context

    private val rulesFlow = MutableStateFlow<Map<String, AppNetworkRule>>(emptyMap())

    @Before
    fun setup() {
        `when`(mockContext.applicationContext).thenReturn(mockContext)
    }

    private fun createMockRepo(): AppRuleRepository {
        val repo = mock(AppRuleRepository::class.java)
        `when`(repo.getAllRulesFlow()).thenReturn(rulesFlow)
        return repo
    }

    @Test
    fun testStartStopRepeatedLifecycle() {
        // Phase 9 Stress Test: START -> STOP -> START -> STOP -> START -> STOP
        for (i in 1..3) {
            val repo = createMockRepo()
            val engine = FirewallPolicyEngine(mockContext, repo)
            assertFalse("Engine should be active after start", engine.isClosed)

            // STOP
            engine.close()
            assertTrue("Engine should be closed after stop", engine.isClosed)
        }
    }

    @Test
    fun testDuplicateStartsDoNotCrashOrLeak() {
        // Phase 9: START -> START -> START
        val repo1 = createMockRepo()
        val engine1 = FirewallPolicyEngine(mockContext, repo1)
        assertFalse(engine1.isClosed)

        // Starting another instance while one is active
        val repo2 = createMockRepo()
        val engine2 = FirewallPolicyEngine(mockContext, repo2)
        assertFalse(engine2.isClosed)

        val repo3 = createMockRepo()
        val engine3 = FirewallPolicyEngine(mockContext, repo3)
        assertFalse(engine3.isClosed)

        // Clean teardown
        engine1.close()
        engine2.close()
        engine3.close()

        assertTrue(engine1.isClosed)
        assertTrue(engine2.isClosed)
        assertTrue(engine3.isClosed)
    }

    @Test
    fun testDuplicateStopsDoNotCrash() {
        // Phase 9: STOP -> STOP -> STOP
        val repo = createMockRepo()
        val engine = FirewallPolicyEngine(mockContext, repo)
        assertFalse(engine.isClosed)

        // First stop
        engine.close()
        assertTrue(engine.isClosed)

        // Duplicate stop 2
        engine.close()
        assertTrue(engine.isClosed)

        // Duplicate stop 3
        engine.close()
        assertTrue(engine.isClosed)
    }

    @Test
    fun testStartStopStartStopCycle() {
        // Phase 9: START -> STOP -> START -> STOP cycle
        for (cycle in 1..2) {
            val repo = createMockRepo()
            val engine = FirewallPolicyEngine(mockContext, repo)
            assertFalse(engine.isClosed)

            engine.close()
            assertTrue(engine.isClosed)
        }
    }

    @Test
    fun testAppRuleRepositoryCloseLifecycle() {
        val repo = AppRuleRepository(mockContext)
        // Should close cleanly without throwing exceptions
        repo.close()
        // Duplicate close should also be safe
        repo.close()
    }

    @Test
    fun testPolicyEngineRegressionAfterLifecycleFix() {
        val repo = createMockRepo()
        val engine = FirewallPolicyEngine(mockContext, repo)

        val pkg1 = "com.example.allowed"
        val pkg2 = "com.example.blockwifi"
        val pkg3 = "com.example.blockmobile"
        val pkg4 = "com.example.blockboth"

        val rules = listOf(
            AppNetworkRule(pkg1, 1001, NetworkPolicy.ALLOW, NetworkPolicy.ALLOW),
            AppNetworkRule(pkg2, 1002, NetworkPolicy.BLOCK, NetworkPolicy.ALLOW),
            AppNetworkRule(pkg3, 1003, NetworkPolicy.ALLOW, NetworkPolicy.BLOCK),
            AppNetworkRule(pkg4, 1004, NetworkPolicy.BLOCK, NetworkPolicy.BLOCK)
        )

        val snapshotField: Field = FirewallPolicyEngine::class.java.getDeclaredField("_snapshotFlow")
        snapshotField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val snapshotFlow = snapshotField.get(engine) as MutableStateFlow<com.aifirewall.app.engine.policy.PolicySnapshot>
        snapshotFlow.value = com.aifirewall.app.engine.policy.PolicySnapshot(
            rulesByUid = rules.associateBy { it.uid },
            rulesByPackage = rules.associateBy { it.packageName },
            managedPackages = setOf(pkg2, pkg3, pkg4),
            unknownAppPolicy = NetworkPolicy.BLOCK,
            version = 1L
        )

        // Verify ALLOW
        assertEquals(NetworkPolicy.ALLOW, engine.evaluate(1001, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.ALLOW, engine.evaluate(1001, NetworkMonitor.NetworkType.MOBILE))

        // Verify BLOCK_WIFI
        assertEquals(NetworkPolicy.BLOCK, engine.evaluate(1002, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.ALLOW, engine.evaluate(1002, NetworkMonitor.NetworkType.MOBILE))

        // Verify BLOCK_MOBILE
        assertEquals(NetworkPolicy.ALLOW, engine.evaluate(1003, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.BLOCK, engine.evaluate(1003, NetworkMonitor.NetworkType.MOBILE))

        // Verify BLOCK_BOTH
        assertEquals(NetworkPolicy.BLOCK, engine.evaluate(1004, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.BLOCK, engine.evaluate(1004, NetworkMonitor.NetworkType.MOBILE))

        // Verify UNKNOWN UID defaults to unknownAppPolicy (BLOCK)
        assertEquals(NetworkPolicy.BLOCK, engine.evaluate(-1, NetworkMonitor.NetworkType.WIFI))
        assertEquals(NetworkPolicy.BLOCK, engine.evaluate(0, NetworkMonitor.NetworkType.WIFI))

        // Verify package resolution
        assertEquals(pkg1, engine.resolvePackageForUid(1001))
        assertEquals(pkg2, engine.resolvePackageForUid(1002))
        assertEquals("UID_9999", engine.resolvePackageForUid(9999))

        engine.close()
        assertTrue(engine.isClosed)
    }
}
