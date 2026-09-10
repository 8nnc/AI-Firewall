package com.aifirewall.app.engine.policy

import android.content.Context
import com.aifirewall.app.domain.model.AppNetworkRule
import com.aifirewall.app.domain.model.NetworkPolicy
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Field

@RunWith(MockitoJUnitRunner.Silent::class)
class UidMappingTest {

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
    }

    @Test
    fun testValidUidResolvesCorrectPackage() {
        setRules(listOf(
            AppNetworkRule("com.google.android.youtube", 10123, NetworkPolicy.ALLOW, NetworkPolicy.BLOCK)
        ))

        val resolvedPackage = policyEngine.resolvePackageForUid(10123)
        assertEquals("com.google.android.youtube", resolvedPackage)
    }

    @Test
    fun testInvalidUidMinusOneReturnsUnknownApplication() {
        val resolvedPackage = policyEngine.resolvePackageForUid(-1)
        assertEquals(AppNetworkRule.UNKNOWN_PACKAGE, resolvedPackage)
    }

    @Test
    fun testZeroUidReturnsUnknownApplication() {
        val resolvedPackage = policyEngine.resolvePackageForUid(0)
        assertEquals(AppNetworkRule.UNKNOWN_PACKAGE, resolvedPackage)
    }

    @Test
    fun testUnmappedInstalledUidReturnsFallbackUidString() {
        setRules(emptyList())
        val resolvedPackage = policyEngine.resolvePackageForUid(10999)
        assertEquals("UID_10999", resolvedPackage)
    }
}
