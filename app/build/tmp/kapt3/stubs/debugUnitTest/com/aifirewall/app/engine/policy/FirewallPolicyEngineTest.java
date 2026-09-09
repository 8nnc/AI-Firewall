package com.aifirewall.app.engine.policy;

@org.junit.runner.RunWith(value = org.mockito.junit.MockitoJUnitRunner.Silent.class)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002J\b\u0010\f\u001a\u00020\bH\u0007J\b\u0010\r\u001a\u00020\bH\u0007J\b\u0010\u000e\u001a\u00020\bH\u0007J\b\u0010\u000f\u001a\u00020\bH\u0007J\b\u0010\u0010\u001a\u00020\bH\u0007J\b\u0010\u0011\u001a\u00020\bH\u0007J\b\u0010\u0012\u001a\u00020\bH\u0007J\b\u0010\u0013\u001a\u00020\bH\u0007J\b\u0010\u0014\u001a\u00020\bH\u0007R\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/aifirewall/app/engine/policy/FirewallPolicyEngineTest;", "", "()V", "mockContext", "Landroid/content/Context;", "policyEngine", "Lcom/aifirewall/app/engine/policy/FirewallPolicyEngine;", "setRules", "", "rules", "", "Lcom/aifirewall/app/domain/model/AppNetworkRule;", "setup", "tearDown", "testAllowPolicy", "testBlockBothPolicy", "testBlockMobilePolicy", "testBlockWifiPolicy", "testInstalledAppWithoutBlockingRuleDefaultsToAllow", "testMultiApplicationIsolation", "testUnknownUidHandling", "app_debugUnitTest"})
public final class FirewallPolicyEngineTest {
    @org.mockito.Mock()
    private android.content.Context mockContext;
    private com.aifirewall.app.engine.policy.FirewallPolicyEngine policyEngine;
    
    public FirewallPolicyEngineTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
    
    @org.junit.After()
    public final void tearDown() {
    }
    
    private final void setRules(java.util.List<com.aifirewall.app.domain.model.AppNetworkRule> rules) {
    }
    
    @org.junit.Test()
    public final void testAllowPolicy() {
    }
    
    @org.junit.Test()
    public final void testBlockWifiPolicy() {
    }
    
    @org.junit.Test()
    public final void testBlockMobilePolicy() {
    }
    
    @org.junit.Test()
    public final void testBlockBothPolicy() {
    }
    
    @org.junit.Test()
    public final void testUnknownUidHandling() {
    }
    
    @org.junit.Test()
    public final void testInstalledAppWithoutBlockingRuleDefaultsToAllow() {
    }
    
    @org.junit.Test()
    public final void testMultiApplicationIsolation() {
    }
}