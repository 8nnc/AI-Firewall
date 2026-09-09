package com.aifirewall.app.engine.policy;

@org.junit.runner.RunWith(value = org.mockito.junit.MockitoJUnitRunner.class)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0007J\b\u0010\t\u001a\u00020\bH\u0007J\b\u0010\n\u001a\u00020\bH\u0007J\b\u0010\u000b\u001a\u00020\bH\u0007J\b\u0010\f\u001a\u00020\bH\u0007J\b\u0010\r\u001a\u00020\bH\u0007R\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/aifirewall/app/engine/policy/RulePrecedenceTest;", "", "()V", "mockContext", "Landroid/content/Context;", "repository", "Lcom/aifirewall/app/data/repository/AppRuleRepository;", "setup", "", "tearDown", "testAppRuleOverridesGroupRule", "testConflictingGroupRulesBlockTakesPrecedence", "testDefaultPolicyWhenNoAppRuleAndNoGroup", "testGroupRuleAppliesWhenAppRuleUnset", "app_debugUnitTest"})
public final class RulePrecedenceTest {
    @org.mockito.Mock()
    private android.content.Context mockContext;
    private com.aifirewall.app.data.repository.AppRuleRepository repository;
    
    public RulePrecedenceTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
    
    @org.junit.After()
    public final void tearDown() {
    }
    
    @org.junit.Test()
    public final void testAppRuleOverridesGroupRule() {
    }
    
    @org.junit.Test()
    public final void testGroupRuleAppliesWhenAppRuleUnset() {
    }
    
    @org.junit.Test()
    public final void testConflictingGroupRulesBlockTakesPrecedence() {
    }
    
    @org.junit.Test()
    public final void testDefaultPolicyWhenNoAppRuleAndNoGroup() {
    }
}