package com.aifirewall.app.engine.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u0014B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u000fJ\b\u0010\u0013\u001a\u00020\u000fH\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/aifirewall/app/engine/network/NetworkMonitor;", "", "()V", "_currentNetwork", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/aifirewall/app/engine/network/NetworkMonitor$NetworkType;", "connectivityManager", "Landroid/net/ConnectivityManager;", "currentNetwork", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentNetwork", "()Lkotlinx/coroutines/flow/StateFlow;", "networkCallback", "Landroid/net/ConnectivityManager$NetworkCallback;", "startMonitoring", "", "context", "Landroid/content/Context;", "stopMonitoring", "updateNetworkState", "NetworkType", "app_debug"})
public final class NetworkMonitor {
    @org.jetbrains.annotations.Nullable()
    private static android.net.ConnectivityManager connectivityManager;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.aifirewall.app.engine.network.NetworkMonitor.NetworkType> _currentNetwork = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.engine.network.NetworkMonitor.NetworkType> currentNetwork = null;
    @org.jetbrains.annotations.NotNull()
    private static final android.net.ConnectivityManager.NetworkCallback networkCallback = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.network.NetworkMonitor INSTANCE = null;
    
    private NetworkMonitor() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.engine.network.NetworkMonitor.NetworkType> getCurrentNetwork() {
        return null;
    }
    
    private final void updateNetworkState() {
    }
    
    @kotlin.jvm.Synchronized()
    public final synchronized void startMonitoring(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.jvm.Synchronized()
    public final synchronized void stopMonitoring() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/aifirewall/app/engine/network/NetworkMonitor$NetworkType;", "", "(Ljava/lang/String;I)V", "WIFI", "MOBILE", "NONE", "UNKNOWN", "app_debug"})
    public static enum NetworkType {
        /*public static final*/ WIFI /* = new WIFI() */,
        /*public static final*/ MOBILE /* = new MOBILE() */,
        /*public static final*/ NONE /* = new NONE() */,
        /*public static final*/ UNKNOWN /* = new UNKNOWN() */;
        
        NetworkType() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.aifirewall.app.engine.network.NetworkMonitor.NetworkType> getEntries() {
            return null;
        }
    }
}