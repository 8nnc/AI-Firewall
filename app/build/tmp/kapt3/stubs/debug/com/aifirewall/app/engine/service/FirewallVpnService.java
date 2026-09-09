package com.aifirewall.app.engine.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 52\u00020\u0001:\u00015B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0002J0\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00182\u0006\u0010 \u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"H\u0002J\u001e\u0010#\u001a\u0004\u0018\u00010$2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00180&H\u0082@\u00a2\u0006\u0002\u0010\'J\b\u0010(\u001a\u00020\u001aH\u0016J\b\u0010)\u001a\u00020\u001aH\u0016J\b\u0010*\u001a\u00020\u001aH\u0016J\"\u0010+\u001a\u00020\u001e2\b\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u00020\u001eH\u0016J\u000e\u00100\u001a\u00020$H\u0082@\u00a2\u0006\u0002\u00101J\b\u00102\u001a\u00020\u001aH\u0002J\b\u00103\u001a\u00020\u001aH\u0002J\u0010\u00104\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00066"}, d2 = {"Lcom/aifirewall/app/engine/service/FirewallVpnService;", "Landroid/net/VpnService;", "()V", "cleanupJob", "Lkotlinx/coroutines/Job;", "monitorJob", "packetParser", "Lcom/aifirewall/app/engine/parser/PacketParser;", "policyEngine", "Lcom/aifirewall/app/engine/policy/FirewallPolicyEngine;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "tcpForwarder", "Lcom/aifirewall/app/engine/network/TcpForwarder;", "udpForwarder", "Lcom/aifirewall/app/engine/network/UdpForwarder;", "vpnInterface", "Landroid/os/ParcelFileDescriptor;", "vpnJob", "vpnMutex", "Lkotlinx/coroutines/sync/Mutex;", "createNotification", "Landroid/app/Notification;", "contentText", "", "createNotificationChannel", "", "dispatchBlockEvent", "packageName", "uid", "", "protocolStr", "networkStr", "packet", "Lcom/aifirewall/app/engine/parser/PacketParser$ParsedPacket;", "establishVpn", "", "managedPackages", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onCreate", "onDestroy", "onRevoke", "onStartCommand", "intent", "Landroid/content/Intent;", "flags", "startId", "processPackets", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startFirewall", "stopFirewall", "updateNotification", "Companion", "app_debug"})
public final class FirewallVpnService extends android.net.VpnService {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "FirewallVpnService";
    private static final int NOTIFICATION_ID = 10101;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "firewall_vpn_channel";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String VPN_ADDRESS = "10.1.10.1";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String VPN_ROUTE = "0.0.0.0";
    private static final int MTU = 1500;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_START = "com.aifirewall.app.START_VPN";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "com.aifirewall.app.STOP_VPN";
    @org.jetbrains.annotations.NotNull()
    private kotlinx.coroutines.CoroutineScope serviceScope;
    @org.jetbrains.annotations.Nullable()
    private android.os.ParcelFileDescriptor vpnInterface;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job vpnJob;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job monitorJob;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job cleanupJob;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.sync.Mutex vpnMutex = null;
    @org.jetbrains.annotations.Nullable()
    private com.aifirewall.app.engine.network.TcpForwarder tcpForwarder;
    @org.jetbrains.annotations.Nullable()
    private com.aifirewall.app.engine.network.UdpForwarder udpForwarder;
    private com.aifirewall.app.engine.policy.FirewallPolicyEngine policyEngine;
    private com.aifirewall.app.engine.parser.PacketParser packetParser;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.service.FirewallVpnService.Companion Companion = null;
    
    public FirewallVpnService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void startFirewall() {
    }
    
    private final java.lang.Object establishVpn(java.util.List<java.lang.String> managedPackages, kotlin.coroutines.Continuation<java.lang.Object> $completion) {
        return null;
    }
    
    private final java.lang.Object processPackets(kotlin.coroutines.Continuation<java.lang.Object> $completion) {
        return null;
    }
    
    private final void dispatchBlockEvent(java.lang.String packageName, int uid, java.lang.String protocolStr, java.lang.String networkStr, com.aifirewall.app.engine.parser.PacketParser.ParsedPacket packet) {
    }
    
    private final void stopFirewall() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    public void onRevoke() {
    }
    
    private final void createNotificationChannel() {
    }
    
    private final android.app.Notification createNotification(java.lang.String contentText) {
        return null;
    }
    
    private final void updateNotification(java.lang.String contentText) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/aifirewall/app/engine/service/FirewallVpnService$Companion;", "", "()V", "ACTION_START", "", "ACTION_STOP", "CHANNEL_ID", "MTU", "", "NOTIFICATION_ID", "TAG", "VPN_ADDRESS", "VPN_ROUTE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}