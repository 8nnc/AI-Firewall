package com.aifirewall.app.engine.parser;

/**
 * High-performance, memory-safe IPv4 & IPv6 packet parser.
 * Supports TCP control flags, sequence/ack numbers, IPv6 extension headers, and fragmentation safety.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \t2\u00020\u0001:\u0002\t\nB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b\u00a8\u0006\u000b"}, d2 = {"Lcom/aifirewall/app/engine/parser/PacketParser;", "", "()V", "parse", "Lcom/aifirewall/app/engine/parser/PacketParser$ParsedPacket;", "buffer", "Ljava/nio/ByteBuffer;", "length", "", "Companion", "ParsedPacket", "app_release"})
public final class PacketParser {
    public static final int PROTOCOL_ICMP = 1;
    public static final int PROTOCOL_TCP = 6;
    public static final int PROTOCOL_UDP = 17;
    public static final int PROTOCOL_ICMPV6 = 58;
    public static final int FLAG_FIN = 1;
    public static final int FLAG_SYN = 2;
    public static final int FLAG_RST = 4;
    public static final int FLAG_PSH = 8;
    public static final int FLAG_ACK = 16;
    public static final int FLAG_URG = 32;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.Integer> IPV6_EXT_HEADERS = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.parser.PacketParser.Companion Companion = null;
    
    public PacketParser() {
        super();
    }
    
    /**
     * Attempts to parse a raw IP packet buffer.
     * Returns a ParsedPacket if valid, or null if malformed or truncated.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.aifirewall.app.engine.parser.PacketParser.ParsedPacket parse(@org.jetbrains.annotations.NotNull()
    java.nio.ByteBuffer buffer, int length) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\"\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/aifirewall/app/engine/parser/PacketParser$Companion;", "", "()V", "FLAG_ACK", "", "FLAG_FIN", "FLAG_PSH", "FLAG_RST", "FLAG_SYN", "FLAG_URG", "IPV6_EXT_HEADERS", "", "PROTOCOL_ICMP", "PROTOCOL_ICMPV6", "PROTOCOL_TCP", "PROTOCOL_UDP", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b.\b\u0086\b\u0018\u00002\u00020\u0001B\u0085\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\f\u0012\b\b\u0002\u0010\u0014\u001a\u00020\f\u00a2\u0006\u0002\u0010\u0015J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u000fH\u00c6\u0003J\t\u0010/\u001a\u00020\u000fH\u00c6\u0003J\t\u00100\u001a\u00020\u0012H\u00c6\u0003J\t\u00101\u001a\u00020\fH\u00c6\u0003J\t\u00102\u001a\u00020\fH\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\u0006H\u00c6\u0003J\t\u00105\u001a\u00020\u0006H\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u0010:\u001a\u00020\u0003H\u00c6\u0003J\u0097\u0001\u0010;\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\fH\u00c6\u0001J\u0013\u0010<\u001a\u00020\u00122\b\u0010=\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010>\u001a\u00020\u0003H\u00d6\u0001J\t\u0010?\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u0010\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001bR\u0011\u0010\u001d\u001a\u00020\u00128F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u001f\u001a\u00020\u00128F\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\u001eR\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u001eR\u0011\u0010 \u001a\u00020\u00128F\u00a2\u0006\u0006\u001a\u0004\b \u0010\u001eR\u0011\u0010!\u001a\u00020\u00128F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u001eR\u0011\u0010\"\u001a\u00020\u00128F\u00a2\u0006\u0006\u001a\u0004\b\"\u0010\u001eR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001bR\u0011\u0010\u0014\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010$R\u0011\u0010\u0013\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010$R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0017R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0019R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001bR\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001b\u00a8\u0006@"}, d2 = {"Lcom/aifirewall/app/engine/parser/PacketParser$ParsedPacket;", "", "ipVersion", "", "protocol", "sourceAddress", "", "destinationAddress", "sourcePort", "destinationPort", "payloadLength", "payload", "", "tcpFlags", "sequenceNumber", "", "ackNumber", "isFragmented", "", "rawSrcIpBytes", "rawDstIpBytes", "(IILjava/lang/String;Ljava/lang/String;III[BIJJZ[B[B)V", "getAckNumber", "()J", "getDestinationAddress", "()Ljava/lang/String;", "getDestinationPort", "()I", "getIpVersion", "isAck", "()Z", "isFin", "isPsh", "isRst", "isSyn", "getPayload", "()[B", "getPayloadLength", "getProtocol", "getRawDstIpBytes", "getRawSrcIpBytes", "getSequenceNumber", "getSourceAddress", "getSourcePort", "getTcpFlags", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_release"})
    public static final class ParsedPacket {
        private final int ipVersion = 0;
        private final int protocol = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String sourceAddress = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String destinationAddress = null;
        private final int sourcePort = 0;
        private final int destinationPort = 0;
        private final int payloadLength = 0;
        @org.jetbrains.annotations.Nullable()
        private final byte[] payload = null;
        private final int tcpFlags = 0;
        private final long sequenceNumber = 0L;
        private final long ackNumber = 0L;
        private final boolean isFragmented = false;
        @org.jetbrains.annotations.NotNull()
        private final byte[] rawSrcIpBytes = null;
        @org.jetbrains.annotations.NotNull()
        private final byte[] rawDstIpBytes = null;
        
        public ParsedPacket(int ipVersion, int protocol, @org.jetbrains.annotations.NotNull()
        java.lang.String sourceAddress, @org.jetbrains.annotations.NotNull()
        java.lang.String destinationAddress, int sourcePort, int destinationPort, int payloadLength, @org.jetbrains.annotations.Nullable()
        byte[] payload, int tcpFlags, long sequenceNumber, long ackNumber, boolean isFragmented, @org.jetbrains.annotations.NotNull()
        byte[] rawSrcIpBytes, @org.jetbrains.annotations.NotNull()
        byte[] rawDstIpBytes) {
            super();
        }
        
        public final int getIpVersion() {
            return 0;
        }
        
        public final int getProtocol() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getSourceAddress() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDestinationAddress() {
            return null;
        }
        
        public final int getSourcePort() {
            return 0;
        }
        
        public final int getDestinationPort() {
            return 0;
        }
        
        public final int getPayloadLength() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final byte[] getPayload() {
            return null;
        }
        
        public final int getTcpFlags() {
            return 0;
        }
        
        public final long getSequenceNumber() {
            return 0L;
        }
        
        public final long getAckNumber() {
            return 0L;
        }
        
        public final boolean isFragmented() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final byte[] getRawSrcIpBytes() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final byte[] getRawDstIpBytes() {
            return null;
        }
        
        public final boolean isSyn() {
            return false;
        }
        
        public final boolean isAck() {
            return false;
        }
        
        public final boolean isFin() {
            return false;
        }
        
        public final boolean isRst() {
            return false;
        }
        
        public final boolean isPsh() {
            return false;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final long component10() {
            return 0L;
        }
        
        public final long component11() {
            return 0L;
        }
        
        public final boolean component12() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final byte[] component13() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final byte[] component14() {
            return null;
        }
        
        public final int component2() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component4() {
            return null;
        }
        
        public final int component5() {
            return 0;
        }
        
        public final int component6() {
            return 0;
        }
        
        public final int component7() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final byte[] component8() {
            return null;
        }
        
        public final int component9() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.engine.parser.PacketParser.ParsedPacket copy(int ipVersion, int protocol, @org.jetbrains.annotations.NotNull()
        java.lang.String sourceAddress, @org.jetbrains.annotations.NotNull()
        java.lang.String destinationAddress, int sourcePort, int destinationPort, int payloadLength, @org.jetbrains.annotations.Nullable()
        byte[] payload, int tcpFlags, long sequenceNumber, long ackNumber, boolean isFragmented, @org.jetbrains.annotations.NotNull()
        byte[] rawSrcIpBytes, @org.jetbrains.annotations.NotNull()
        byte[] rawDstIpBytes) {
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
}