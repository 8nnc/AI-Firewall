package com.aifirewall.app.engine.parser;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\n\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bJ&\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\fJ&\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\f\u00a8\u0006\u0011"}, d2 = {"Lcom/aifirewall/app/engine/parser/ChecksumUtils;", "", "()V", "calculateIpHeaderChecksum", "", "buffer", "Ljava/nio/ByteBuffer;", "offset", "", "headerLength", "calculateTransportChecksumIPv4", "srcIpBytes", "", "dstIpBytes", "protocol", "transportHeaderAndPayload", "calculateTransportChecksumIPv6", "app_debug"})
public final class ChecksumUtils {
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.parser.ChecksumUtils INSTANCE = null;
    
    private ChecksumUtils() {
        super();
    }
    
    /**
     * Calculates the standard 16-bit 1's complement checksum for IPv4 header bytes.
     */
    public final short calculateIpHeaderChecksum(@org.jetbrains.annotations.NotNull()
    java.nio.ByteBuffer buffer, int offset, int headerLength) {
        return 0;
    }
    
    /**
     * Calculates TCP/UDP checksum over IPv4 using IPv4 Pseudo-Header.
     */
    public final short calculateTransportChecksumIPv4(@org.jetbrains.annotations.NotNull()
    byte[] srcIpBytes, @org.jetbrains.annotations.NotNull()
    byte[] dstIpBytes, int protocol, @org.jetbrains.annotations.NotNull()
    byte[] transportHeaderAndPayload) {
        return 0;
    }
    
    /**
     * Calculates TCP/UDP checksum over IPv6 using IPv6 Pseudo-Header.
     */
    public final short calculateTransportChecksumIPv6(@org.jetbrains.annotations.NotNull()
    byte[] srcIpBytes, @org.jetbrains.annotations.NotNull()
    byte[] dstIpBytes, int protocol, @org.jetbrains.annotations.NotNull()
    byte[] transportHeaderAndPayload) {
        return 0;
    }
}