package com.aifirewall.app.engine.policy;

/**
 * Immutable thread-safe snapshot of active policy configuration.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u0003\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\u0015\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003H\u00c6\u0003J\u0015\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u0003H\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00070\tH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\rH\u00c6\u0003JY\u0010\u001d\u001a\u00020\u00002\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u00032\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u00032\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\u0004H\u00d6\u0001J\t\u0010\"\u001a\u00020\u0007H\u00d6\u0001R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001d\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00050\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006#"}, d2 = {"Lcom/aifirewall/app/engine/policy/PolicySnapshot;", "", "rulesByUid", "", "", "Lcom/aifirewall/app/domain/model/AppNetworkRule;", "rulesByPackage", "", "managedPackages", "", "unknownAppPolicy", "Lcom/aifirewall/app/domain/model/NetworkPolicy;", "version", "", "(Ljava/util/Map;Ljava/util/Map;Ljava/util/Set;Lcom/aifirewall/app/domain/model/NetworkPolicy;J)V", "getManagedPackages", "()Ljava/util/Set;", "getRulesByPackage", "()Ljava/util/Map;", "getRulesByUid", "getUnknownAppPolicy", "()Lcom/aifirewall/app/domain/model/NetworkPolicy;", "getVersion", "()J", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class PolicySnapshot {
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.Integer, com.aifirewall.app.domain.model.AppNetworkRule> rulesByUid = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, com.aifirewall.app.domain.model.AppNetworkRule> rulesByPackage = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> managedPackages = null;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.domain.model.NetworkPolicy unknownAppPolicy = null;
    private final long version = 0L;
    
    public PolicySnapshot(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Integer, com.aifirewall.app.domain.model.AppNetworkRule> rulesByUid, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.aifirewall.app.domain.model.AppNetworkRule> rulesByPackage, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> managedPackages, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy unknownAppPolicy, long version) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Integer, com.aifirewall.app.domain.model.AppNetworkRule> getRulesByUid() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, com.aifirewall.app.domain.model.AppNetworkRule> getRulesByPackage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getManagedPackages() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.NetworkPolicy getUnknownAppPolicy() {
        return null;
    }
    
    public final long getVersion() {
        return 0L;
    }
    
    public PolicySnapshot() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.Integer, com.aifirewall.app.domain.model.AppNetworkRule> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, com.aifirewall.app.domain.model.AppNetworkRule> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.NetworkPolicy component4() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.engine.policy.PolicySnapshot copy(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Integer, com.aifirewall.app.domain.model.AppNetworkRule> rulesByUid, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.aifirewall.app.domain.model.AppNetworkRule> rulesByPackage, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> managedPackages, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy unknownAppPolicy, long version) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}