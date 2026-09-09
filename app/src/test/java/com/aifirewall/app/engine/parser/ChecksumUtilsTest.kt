package com.aifirewall.app.engine.parser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.nio.ByteBuffer

class ChecksumUtilsTest {

    @Test
    fun testIpHeaderChecksumCalculation() {
        // Sample 20-byte IPv4 header with zero checksum field
        val ipHeader = byteArrayOf(
            0x45.toByte(), 0x00.toByte(), 0x00.toByte(), 0x3c.toByte(),
            0x1c.toByte(), 0x46.toByte(), 0x40.toByte(), 0x00.toByte(),
            0x40.toByte(), 0x06.toByte(), 0x00.toByte(), 0x00.toByte(), // checksum at bytes 10-11
            192.toByte(), 168.toByte(), 1.toByte(), 1.toByte(),
            192.toByte(), 168.toByte(), 1.toByte(), 2.toByte()
        )
        val buffer = ByteBuffer.wrap(ipHeader)
        val checksum = ChecksumUtils.calculateIpHeaderChecksum(buffer, 0, 20)

        // Non-zero checksum calculated
        assertNotEquals(0.toShort(), checksum)
    }

    @Test
    fun testTransportChecksumIPv4() {
        val srcIp = byteArrayOf(10.toByte(), 1.toByte(), 10.toByte(), 1.toByte())
        val dstIp = byteArrayOf(8.toByte(), 8.toByte(), 8.toByte(), 8.toByte())

        val udpPayload = "HELLO".toByteArray()
        val udpHeaderAndPayload = ByteArray(8 + udpPayload.size)
        // Src port 12345, Dst port 53, Length 13, Checksum 0
        udpHeaderAndPayload[0] = 0x30
        udpHeaderAndPayload[1] = 0x39
        udpHeaderAndPayload[2] = 0x00
        udpHeaderAndPayload[3] = 0x35
        udpHeaderAndPayload[4] = 0x00
        udpHeaderAndPayload[5] = 0x0d
        System.arraycopy(udpPayload, 0, udpHeaderAndPayload, 8, udpPayload.size)

        val checksum = ChecksumUtils.calculateTransportChecksumIPv4(
            srcIpBytes = srcIp,
            dstIpBytes = dstIp,
            protocol = PacketParser.PROTOCOL_UDP,
            transportHeaderAndPayload = udpHeaderAndPayload
        )

        assertNotEquals(0.toShort(), checksum)
    }

    @Test
    fun testTransportChecksumIPv6() {
        val srcIp = ByteArray(16) { 0x01 }
        val dstIp = ByteArray(16) { 0x02 }

        val tcpPayload = "IPv6 TCP TEST".toByteArray()
        val tcpHeaderAndPayload = ByteArray(20 + tcpPayload.size)
        // Src port 8080, Dst port 443, seq 1, ack 1, flags ACK
        tcpHeaderAndPayload[0] = 0x1f
        tcpHeaderAndPayload[1] = 0x90.toByte()
        tcpHeaderAndPayload[2] = 0x01
        tcpHeaderAndPayload[3] = 0xbb.toByte()
        System.arraycopy(tcpPayload, 0, tcpHeaderAndPayload, 20, tcpPayload.size)

        val checksum = ChecksumUtils.calculateTransportChecksumIPv6(
            srcIpBytes = srcIp,
            dstIpBytes = dstIp,
            protocol = PacketParser.PROTOCOL_TCP,
            transportHeaderAndPayload = tcpHeaderAndPayload
        )

        assertNotEquals(0.toShort(), checksum)
    }
}
