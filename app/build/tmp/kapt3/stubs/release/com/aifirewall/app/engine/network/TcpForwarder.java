package com.aifirewall.app.engine.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u0000 02\u00020\u0001:\u00040123B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006JR\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00132\b\u0010\u0019\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0006\u0010\u001c\u001a\u00020\u001dJ\u0010\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\tH\u0002J$\u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001d0%J(\u0010&\u001a\u00020\u001d2\u0006\u0010\'\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u00162\u0006\u0010)\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0013H\u0002J\u0018\u0010*\u001a\u00020\u001d2\u0006\u0010\'\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u000eH\u0002J\u0010\u0010+\u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\"H\u0002J\u0010\u0010,\u001a\u00020\u001d2\u0006\u0010\'\u001a\u00020\nH\u0002J\u0018\u0010-\u001a\u00020\u001d2\u0006\u0010\'\u001a\u00020\n2\u0006\u0010.\u001a\u00020\"H\u0002J\u0006\u0010/\u001a\u00020\u001dR\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2 = {"Lcom/aifirewall/app/engine/network/TcpForwarder;", "", "vpnService", "Landroid/net/VpnService;", "vpnOutput", "Ljava/io/FileOutputStream;", "(Landroid/net/VpnService;Ljava/io/FileOutputStream;)V", "activeConnections", "Ljava/util/concurrent/ConcurrentHashMap;", "Lcom/aifirewall/app/engine/network/TcpForwarder$TcpFlowKey;", "Lcom/aifirewall/app/engine/network/TcpForwarder$TcpConnection;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "buildTcpPacket", "", "srcIp", "", "dstIp", "srcPort", "", "dstPort", "seq", "", "ack", "flags", "payload", "isIPv6", "", "cleanStaleConnections", "", "closeConnection", "key", "handleTcpPacket", "packet", "Lcom/aifirewall/app/engine/parser/PacketParser$ParsedPacket;", "isAllowed", "onBlocked", "Lkotlin/Function0;", "sendAckPacket", "conn", "ackNum", "seqNum", "sendDataPacket", "sendRstPacket", "sendSynAckPacket", "startOutboundConnection", "initialPacket", "stop", "Companion", "TcpConnection", "TcpFlowKey", "TcpState", "app_release"})
public final class TcpForwarder {
    @org.jetbrains.annotations.NotNull()
    private final android.net.VpnService vpnService = null;
    @org.jetbrains.annotations.NotNull()
    private final java.io.FileOutputStream vpnOutput = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "TcpForwarder";
    private static final int CONNECT_TIMEOUT_MS = 15000;
    private static final long IDLE_TIMEOUT_MS = 120000L;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<com.aifirewall.app.engine.network.TcpForwarder.TcpFlowKey, com.aifirewall.app.engine.network.TcpForwarder.TcpConnection> activeConnections = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.network.TcpForwarder.Companion Companion = null;
    
    public TcpForwarder(@org.jetbrains.annotations.NotNull()
    android.net.VpnService vpnService, @org.jetbrains.annotations.NotNull()
    java.io.FileOutputStream vpnOutput) {
        super();
    }
    
    public final void handleTcpPacket(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.engine.parser.PacketParser.ParsedPacket packet, boolean isAllowed, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBlocked) {
    }
    
    private final void startOutboundConnection(com.aifirewall.app.engine.network.TcpForwarder.TcpConnection conn, com.aifirewall.app.engine.parser.PacketParser.ParsedPacket initialPacket) {
    }
    
    private final void sendSynAckPacket(com.aifirewall.app.engine.network.TcpForwarder.TcpConnection conn) {
    }
    
    private final void sendAckPacket(com.aifirewall.app.engine.network.TcpForwarder.TcpConnection conn, long ackNum, long seqNum, int flags) {
    }
    
    private final void sendDataPacket(com.aifirewall.app.engine.network.TcpForwarder.TcpConnection conn, byte[] payload) {
    }
    
    private final void sendRstPacket(com.aifirewall.app.engine.parser.PacketParser.ParsedPacket packet) {
    }
    
    private final byte[] buildTcpPacket(java.lang.String srcIp, java.lang.String dstIp, int srcPort, int dstPort, long seq, long ack, int flags, byte[] payload, boolean isIPv6) {
        return null;
    }
    
    private final void closeConnection(com.aifirewall.app.engine.network.TcpForwarder.TcpFlowKey key) {
    }
    
    public final void cleanStaleConnections() {
    }
    
    public final void stop() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/aifirewall/app/engine/network/TcpForwarder$Companion;", "", "()V", "CONNECT_TIMEOUT_MS", "", "IDLE_TIMEOUT_MS", "", "TAG", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u00103\u001a\u000204R\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0014R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001a\u0010 \u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0012\"\u0004\b\"\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u001a\u0010%\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0012\"\u0004\b\'\u0010\u0014R\u001a\u0010(\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0012\"\u0004\b*\u0010\u0014R\u001a\u0010+\u001a\u00020,X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u00102\u00a8\u00065"}, d2 = {"Lcom/aifirewall/app/engine/network/TcpForwarder$TcpConnection;", "", "key", "Lcom/aifirewall/app/engine/network/TcpForwarder$TcpFlowKey;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "vpnOutput", "Ljava/io/FileOutputStream;", "(Lcom/aifirewall/app/engine/network/TcpForwarder$TcpFlowKey;Lkotlinx/coroutines/CoroutineScope;Ljava/io/FileOutputStream;)V", "channel", "Ljava/nio/channels/SocketChannel;", "getChannel", "()Ljava/nio/channels/SocketChannel;", "setChannel", "(Ljava/nio/channels/SocketChannel;)V", "clientAck", "", "getClientAck", "()J", "setClientAck", "(J)V", "clientSeq", "getClientSeq", "setClientSeq", "job", "Lkotlinx/coroutines/Job;", "getJob", "()Lkotlinx/coroutines/Job;", "setJob", "(Lkotlinx/coroutines/Job;)V", "getKey", "()Lcom/aifirewall/app/engine/network/TcpForwarder$TcpFlowKey;", "lastActivityTime", "getLastActivityTime", "setLastActivityTime", "getScope", "()Lkotlinx/coroutines/CoroutineScope;", "serverAck", "getServerAck", "setServerAck", "serverSeq", "getServerSeq", "setServerSeq", "state", "Lcom/aifirewall/app/engine/network/TcpForwarder$TcpState;", "getState", "()Lcom/aifirewall/app/engine/network/TcpForwarder$TcpState;", "setState", "(Lcom/aifirewall/app/engine/network/TcpForwarder$TcpState;)V", "getVpnOutput", "()Ljava/io/FileOutputStream;", "updateActivity", "", "app_release"})
    static final class TcpConnection {
        @org.jetbrains.annotations.NotNull()
        private final com.aifirewall.app.engine.network.TcpForwarder.TcpFlowKey key = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlinx.coroutines.CoroutineScope scope = null;
        @org.jetbrains.annotations.NotNull()
        private final java.io.FileOutputStream vpnOutput = null;
        @org.jetbrains.annotations.NotNull()
        private com.aifirewall.app.engine.network.TcpForwarder.TcpState state = com.aifirewall.app.engine.network.TcpForwarder.TcpState.CLOSED;
        @org.jetbrains.annotations.Nullable()
        private java.nio.channels.SocketChannel channel;
        @org.jetbrains.annotations.Nullable()
        private kotlinx.coroutines.Job job;
        private long clientSeq = 0L;
        private long clientAck = 0L;
        private long serverSeq;
        private long serverAck = 0L;
        @kotlin.jvm.Volatile()
        private volatile long lastActivityTime;
        
        public TcpConnection(@org.jetbrains.annotations.NotNull()
        com.aifirewall.app.engine.network.TcpForwarder.TcpFlowKey key, @org.jetbrains.annotations.NotNull()
        kotlinx.coroutines.CoroutineScope scope, @org.jetbrains.annotations.NotNull()
        java.io.FileOutputStream vpnOutput) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.engine.network.TcpForwarder.TcpFlowKey getKey() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.CoroutineScope getScope() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.io.FileOutputStream getVpnOutput() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.engine.network.TcpForwarder.TcpState getState() {
            return null;
        }
        
        public final void setState(@org.jetbrains.annotations.NotNull()
        com.aifirewall.app.engine.network.TcpForwarder.TcpState p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.nio.channels.SocketChannel getChannel() {
            return null;
        }
        
        public final void setChannel(@org.jetbrains.annotations.Nullable()
        java.nio.channels.SocketChannel p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final kotlinx.coroutines.Job getJob() {
            return null;
        }
        
        public final void setJob(@org.jetbrains.annotations.Nullable()
        kotlinx.coroutines.Job p0) {
        }
        
        public final long getClientSeq() {
            return 0L;
        }
        
        public final void setClientSeq(long p0) {
        }
        
        public final long getClientAck() {
            return 0L;
        }
        
        public final void setClientAck(long p0) {
        }
        
        public final long getServerSeq() {
            return 0L;
        }
        
        public final void setServerSeq(long p0) {
        }
        
        public final long getServerAck() {
            return 0L;
        }
        
        public final void setServerAck(long p0) {
        }
        
        public final long getLastActivityTime() {
            return 0L;
        }
        
        public final void setLastActivityTime(long p0) {
        }
        
        public final void updateActivity() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0013\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\tH\u00c6\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\t2\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000e\u00a8\u0006\u001c"}, d2 = {"Lcom/aifirewall/app/engine/network/TcpForwarder$TcpFlowKey;", "", "srcIp", "", "srcPort", "", "dstIp", "dstPort", "isIPv6", "", "(Ljava/lang/String;ILjava/lang/String;IZ)V", "getDstIp", "()Ljava/lang/String;", "getDstPort", "()I", "()Z", "getSrcIp", "getSrcPort", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "app_release"})
    public static final class TcpFlowKey {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String srcIp = null;
        private final int srcPort = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String dstIp = null;
        private final int dstPort = 0;
        private final boolean isIPv6 = false;
        
        public TcpFlowKey(@org.jetbrains.annotations.NotNull()
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
        public final com.aifirewall.app.engine.network.TcpForwarder.TcpFlowKey copy(@org.jetbrains.annotations.NotNull()
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\u000b"}, d2 = {"Lcom/aifirewall/app/engine/network/TcpForwarder$TcpState;", "", "(Ljava/lang/String;I)V", "CLOSED", "SYN_SENT", "ESTABLISHED", "FIN_WAIT", "CLOSE_WAIT", "LAST_ACK", "TIME_WAIT", "RESET", "app_release"})
    public static enum TcpState {
        /*public static final*/ CLOSED /* = new CLOSED() */,
        /*public static final*/ SYN_SENT /* = new SYN_SENT() */,
        /*public static final*/ ESTABLISHED /* = new ESTABLISHED() */,
        /*public static final*/ FIN_WAIT /* = new FIN_WAIT() */,
        /*public static final*/ CLOSE_WAIT /* = new CLOSE_WAIT() */,
        /*public static final*/ LAST_ACK /* = new LAST_ACK() */,
        /*public static final*/ TIME_WAIT /* = new TIME_WAIT() */,
        /*public static final*/ RESET /* = new RESET() */;
        
        TcpState() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.aifirewall.app.engine.network.TcpForwarder.TcpState> getEntries() {
            return null;
        }
    }
}