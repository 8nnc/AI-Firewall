package com.aifirewall.app.engine;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\r"}, d2 = {"Lcom/aifirewall/app/engine/FirewallStateManager;", "", "()V", "_firewallState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/aifirewall/app/domain/model/FirewallState;", "firewallState", "Lkotlinx/coroutines/flow/StateFlow;", "getFirewallState", "()Lkotlinx/coroutines/flow/StateFlow;", "setState", "", "state", "app_debug"})
public final class FirewallStateManager {
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.aifirewall.app.domain.model.FirewallState> _firewallState = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.domain.model.FirewallState> firewallState = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.FirewallStateManager INSTANCE = null;
    
    private FirewallStateManager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.domain.model.FirewallState> getFirewallState() {
        return null;
    }
    
    public final void setState(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.FirewallState state) {
    }
}