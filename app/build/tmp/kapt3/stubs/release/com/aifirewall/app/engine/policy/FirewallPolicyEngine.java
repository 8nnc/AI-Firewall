package com.aifirewall.app.engine.policy;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0016\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!J\u0016\u0010\"\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020$2\u0006\u0010 \u001a\u00020!J\u0018\u0010%\u001a\u00020\u001d2\u0006\u0010&\u001a\u00020\n2\u0006\u0010 \u001a\u00020!H\u0002J\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020$0\tJ\u000e\u0010(\u001a\u00020$2\u0006\u0010\u001e\u001a\u00020\u001fR\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u00108F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0011R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\f0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017\u00a8\u0006)"}, d2 = {"Lcom/aifirewall/app/engine/policy/FirewallPolicyEngine;", "Ljava/io/Closeable;", "context", "Landroid/content/Context;", "ruleRepository", "Lcom/aifirewall/app/data/repository/AppRuleRepository;", "(Landroid/content/Context;Lcom/aifirewall/app/data/repository/AppRuleRepository;)V", "_rulesFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/aifirewall/app/domain/model/AppNetworkRule;", "_snapshotFlow", "Lcom/aifirewall/app/engine/policy/PolicySnapshot;", "engineScope", "Lkotlinx/coroutines/CoroutineScope;", "isClosed", "", "()Z", "rulesCollectorJob", "Lkotlinx/coroutines/Job;", "rulesFlow", "Lkotlinx/coroutines/flow/StateFlow;", "getRulesFlow", "()Lkotlinx/coroutines/flow/StateFlow;", "snapshotFlow", "getSnapshotFlow", "close", "", "evaluate", "Lcom/aifirewall/app/domain/model/NetworkPolicy;", "uid", "", "networkType", "Lcom/aifirewall/app/engine/network/NetworkMonitor$NetworkType;", "evaluatePackage", "packageName", "", "evaluateRuleAgainstNetwork", "rule", "getManagedPackages", "resolvePackageForUid", "app_release"})
public final class FirewallPolicyEngine implements java.io.Closeable {
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.repository.AppRuleRepository ruleRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.aifirewall.app.engine.policy.PolicySnapshot> _snapshotFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.engine.policy.PolicySnapshot> snapshotFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.aifirewall.app.domain.model.AppNetworkRule>> _rulesFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.aifirewall.app.domain.model.AppNetworkRule>> rulesFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope engineScope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job rulesCollectorJob;
    
    public FirewallPolicyEngine(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.repository.AppRuleRepository ruleRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.engine.policy.PolicySnapshot> getSnapshotFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.aifirewall.app.domain.model.AppNetworkRule>> getRulesFlow() {
        return null;
    }
    
    public final boolean isClosed() {
        return false;
    }
    
    @java.lang.Override()
    public void close() {
    }
    
    /**
     * Determines whether traffic for a given UID should be allowed or dropped.
     * Evaluates in O(1) time against the active PolicySnapshot without database or IPC operations.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.NetworkPolicy evaluate(int uid, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.engine.network.NetworkMonitor.NetworkType networkType) {
        return null;
    }
    
    /**
     * Evaluates policy for an explicit package name.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.NetworkPolicy evaluatePackage(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.engine.network.NetworkMonitor.NetworkType networkType) {
        return null;
    }
    
    private final com.aifirewall.app.domain.model.NetworkPolicy evaluateRuleAgainstNetwork(com.aifirewall.app.domain.model.AppNetworkRule rule, com.aifirewall.app.engine.network.NetworkMonitor.NetworkType networkType) {
        return null;
    }
    
    /**
     * Fast in-memory package name lookup for a given UID.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String resolvePackageForUid(int uid) {
        return null;
    }
    
    /**
     * Returns a list of package names managed by the firewall (routed to TUN).
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getManagedPackages() {
        return null;
    }
}