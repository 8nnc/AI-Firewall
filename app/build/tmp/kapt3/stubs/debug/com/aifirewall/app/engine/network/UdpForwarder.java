package com.aifirewall.app.engine.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u001c2\u00020\u0001:\u0003\u001c\u001d\u001eB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J8\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u0006\u0010\u0018\u001a\u00020\u0019J6\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u0017J\u0006\u0010\u001b\u001a\u00020\u0019R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/aifirewall/app/engine/network/UdpForwarder;", "", "vpnService", "Landroid/net/VpnService;", "vpnOutput", "Ljava/io/FileOutputStream;", "(Landroid/net/VpnService;Ljava/io/FileOutputStream;)V", "activeSessions", "Ljava/util/concurrent/ConcurrentHashMap;", "Lcom/aifirewall/app/engine/network/UdpForwarder$UdpFlowKey;", "Lcom/aifirewall/app/engine/network/UdpForwarder$UdpFlowSession;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "buildUdpPacket", "", "srcIp", "", "dstIp", "srcPort", "", "dstPort", "payload", "isIPv6", "", "cleanStaleSessions", "", "forwardUdpPacket", "stop", "Companion", "UdpFlowKey", "UdpFlowSession", "app_debug"})
public final class UdpForwarder {
    @org.jetbrains.annotations.NotNull()
    private final android.net.VpnService vpnService = null;
    @org.jetbrains.annotations.NotNull()
    private final java.io.FileOutputStream vpnOutput = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "UdpForwarder";
    private static final int DEFAULT_TIMEOUT_MS = 30000;
    private static final int DNS_TIMEOUT_MS = 10000;
    private static final int PORT_DNS = 53;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<com.aifirewall.app.engine.network.UdpForwarder.UdpFlowKey, com.aifirewall.app.engine.network.UdpForwarder.UdpFlowSession> activeSessions = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.network.UdpForwarder.Companion Companion = null;
    
    public UdpForwarder(@org.jetbrains.annotations.NotNull()
    android.net.VpnService vpnService, @org.jetbrains.annotations.NotNull()
    java.io.FileOutputStream vpnOutput) {
        super();
    }
    
    public final void forwardUdpPacket(@org.jetbrains.annotations.NotNull()
    java.lang.String srcIp, @org.jetbrains.annotations.NotNull()
    java.lang.String dstIp, int srcPort, int dstPort, @org.jetbrains.annotations.NotNull()
    byte[] payload, boolean isIPv6) {
    }
    
    private final byte[] buildUdpPacket(java.lang.String srcIp, java.lang.String dstIp, int srcPort, int dstPort, byte[] payload, boolean isIPv6) {
        return null;
    }
    
    public final void cleanStaleSessions() {
    }
    
    public final void stop() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/aifirewall/app/engine/network/UdpForwarder$Companion;", "", "()V", "DEFAULT_TIMEOUT_MS", "", "DNS_TIMEOUT_MS", "PORT_DNS", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0013\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\tH\u00c6\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\t2\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000e\u00a8\u0006\u001c"}, d2 = {"Lcom/aifirewall/app/engine/network/UdpForwarder$UdpFlowKey;", "", "srcIp", "", "srcPort", "", "dstIp", "dstPort", "isIPv6", "", "(Ljava/lang/String;ILjava/lang/String;IZ)V", "getDstIp", "()Ljava/lang/String;", "getDstPort", "()I", "()Z", "getSrcIp", "getSrcPort", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
    public static final class UdpFlowKey {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String srcIp = null;
        private final int srcPort = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String dstIp = null;
        private final int dstPort = 0;
        private final boolean isIPv6 = false;
        
        public UdpFlowKey(@org.jetbrains.annotations.NotNull()
        java.lang.String srcIp, int srcPort, @org.jetbrains.annotations.NotNull()
        java.lang.String dstIp, int dstPort, boolean isIPv6) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSrcIp() {
            return null;
        }
        
        public final int getSrcPort() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDstIp() {
            return null;
        }
        
        public final int getDstPort() {
            return 0;
        }
        
        public final boolean isIPv6() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        public final int component4() {
            return 0;
        }
        
        public final boolean component5() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.engine.network.UdpForwarder.UdpFlowKey copy(@org.jetbrains.annotations.NotNull()
        java.lang.String srcIp, int srcPort, @org.jetbrains.annotations.NotNull()
        java.lang.String dstIp, int dstPort, boolean isIPv6) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u0006\u0010\u001a\u001a\u00020\u001bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006\u001c"}, d2 = {"Lcom/aifirewall/app/engine/network/UdpForwarder$UdpFlowSession;", "", "key", "Lcom/aifirewall/app/engine/network/UdpForwarder$UdpFlowKey;", "socket", "Ljava/net/DatagramSocket;", "packetQueue", "Lkotlinx/coroutines/channels/Channel;", "", "job", "Lkotlinx/coroutines/Job;", "(Lcom/aifirewall/app/engine/network/UdpForwarder$UdpFlowKey;Ljava/net/DatagramSocket;Lkotlinx/coroutines/channels/Channel;Lkotlinx/coroutines/Job;)V", "getJob", "()Lkotlinx/coroutines/Job;", "getKey", "()Lcom/aifirewall/app/engine/network/UdpForwarder$UdpFlowKey;", "lastActivityTime", "", "getLastActivityTime", "()J", "setLastActivityTime", "(J)V", "getPacketQueue", "()Lkotlinx/coroutines/channels/Channel;", "getSocket", "()Ljava/net/DatagramSocket;", "touch", "", "app_debug"})
    static final class UdpFlowSession {
        @org.jetbrains.annotations.NotNull()
        private final com.aifirewall.app.engine.network.UdpForwarder.UdpFlowKey key = null;
        @org.jetbrains.annotations.NotNull()
        private final java.net.DatagramSocket socket = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlinx.coroutines.channels.Channel<byte[]> packetQueue = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlinx.coroutines.Job job = null;
        @kotlin.jvm.Volatile()
        private volatile long lastActivityTime;
        
        public UdpFlowSession(@org.jetbrains.annotations.NotNull()
        com.aifirewall.app.engine.network.UdpForwarder.UdpFlowKey key, @org.jetbrains.annotations.NotNull()
        java.net.DatagramSocket socket, @org.jetbrains.annotations.NotNull()
        kotlinx.coroutines.channels.Channel<byte[]> packetQueue, @org.jetbrains.annotations.NotNull()
        kotlinx.coroutines.Job job) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.engine.network.UdpForwarder.UdpFlowKey getKey() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.net.DatagramSocket getSocket() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.channels.Channel<byte[]> getPacketQueue() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.Job getJob() {
            return null;
        }
        
        public final long getLastActivityTime() {
            return 0L;
        }
        
        public final void setLastActivityTime(long p0) {
        }
        
        public final void touch() {
        }
    }
}