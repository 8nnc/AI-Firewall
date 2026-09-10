package com.aifirewall.app.engine.parser

import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.nio.ByteBuffer

object ChecksumUtils {

    /**
     * Calculates the standard 16-bit 1's complement checksum for IPv4 header bytes.
     */
    fun calculateIpHeaderChecksum(buffer: ByteBuffer, offset: Int, headerLength: Int): Short {
        var sum = 0
        val end = offset + headerLength
        for (i in offset until end step 2) {
            if (i == offset + 10) continue // Skip the checksum field itself (bytes 10-11 in IPv4 header)
            val word = ((buffer.get(i).toInt() and 0xFF) shl 8) or (buffer.get(i + 1).toInt() and 0xFF)
            sum += word
        }
        while (sum shr 16 > 0) {
            sum = (sum and 0xFFFF) + (sum shr 16)
        }
        return (sum.inv() and 0xFFFF).toShort()
    }

    /**
     * Calculates TCP/UDP checksum over IPv4 using IPv4 Pseudo-Header.
     */
    fun calculateTransportChecksumIPv4(
        srcIpBytes: ByteArray,
        dstIpBytes: ByteArray,
        protocol: Int,
        transportHeaderAndPayload: ByteArray
    ): Short {
        var sum = 0

        // Pseudo-Header (12 bytes):
        // 4 bytes Source IP
        // 4 bytes Destination IP
        // 1 byte Zero
        // 1 byte Protocol
        // 2 bytes Transport Length

        for (i in 0 until 4 step 2) {
            sum += ((srcIpBytes[i].toInt() and 0xFF) shl 8) or (srcIpBytes[i + 1].toInt() and 0xFF)
        }
        for (i in 0 until 4 step 2) {
            sum += ((dstIpBytes[i].toInt() and 0xFF) shl 8) or (dstIpBytes[i + 1].toInt() and 0xFF)
        }
        sum += protocol and 0xFF
        val transportLength = transportHeaderAndPayload.size
        sum += transportLength

        // Transport header and payload
        for (i in 0 until transportLength step 2) {
            val byte1 = transportHeaderAndPayload[i].toInt() and 0xFF
            val byte2 = if (i + 1 < transportLength) transportHeaderAndPayload[i + 1].toInt() and 0xFF else 0
            val word = (byte1 shl 8) or byte2
            sum += word
        }

        while (sum shr 16 > 0) {
            sum = (sum and 0xFFFF) + (sum shr 16)
        }

        var checksum = sum.inv() and 0xFFFF
        if (protocol == PacketParser.PROTOCOL_UDP && checksum == 0) {
            checksum = 0xFFFF // In UDP, 0xFFFF represents 0 in 1's complement sum
        }
        return checksum.toShort()
    }

    /**
     * Calculates TCP/UDP checksum over IPv6 using IPv6 Pseudo-Header.
     */
    fun calculateTransportChecksumIPv6(
        srcIpBytes: ByteArray,
        dstIpBytes: ByteArray,
        protocol: Int,
        transportHeaderAndPayload: ByteArray
    ): Short {
        var sum = 0

        // IPv6 Pseudo-Header (40 bytes):
        // 16 bytes Source IPv6
        // 16 bytes Destination IPv6
        // 4 bytes Transport Length (32-bit int)
        // 3 bytes Zero
        // 1 byte Next Header / Protocol

        for (i in 0 until 16 step 2) {
            sum += ((srcIpBytes[i].toInt() and 0xFF) shl 8) or (srcIpBytes[i + 1].toInt() and 0xFF)
        }
        for (i in 0 until 16 step 2) {
            sum += ((dstIpBytes[i].toInt() and 0xFF) shl 8) or (dstIpBytes[i + 1].toInt() and 0xFF)
        }

        val transportLength = transportHeaderAndPayload.size
        sum += (transportLength shr 16) and 0xFFFF
        sum += transportLength and 0xFFFF
        sum += protocol and 0xFF

        // Transport header and payload
        for (i in 0 until transportLength step 2) {
            val byte1 = transportHeaderAndPayload[i].toInt() and 0xFF
            val byte2 = if (i + 1 < transportLength) transportHeaderAndPayload[i + 1].toInt() and 0xFF else 0
            val word = (byte1 shl 8) or byte2
            sum += word
        }

        while (sum shr 16 > 0) {
            sum = (sum and 0xFFFF) + (sum shr 16)
        }

        var checksum = sum.inv() and 0xFFFF
        if (protocol == PacketParser.PROTOCOL_UDP && checksum == 0) {
            checksum = 0xFFFF
        }
        return checksum.toShort()
    }
}
