package com.aifirewall.app.engine.parser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.ByteBuffer

class PacketParserTest {

    private val parser = PacketParser()

    @Test
    fun testParseIPv4TcpSynPacket() {
        val raw = ByteArray(40)
        val buf = ByteBuffer.wrap(raw)

        // Version 4, IHL 5 (20 bytes)
        raw[0] = 0x45.toByte()
        // Total length = 40
        raw[2] = 0x00
        raw[3] = 0x28
        // Protocol = 6 (TCP)
        raw[9] = 0x06
        // Src IP = 10.0.0.1
        raw[12] = 10
        raw[13] = 0
        raw[14] = 0
        raw[15] = 1
        // Dst IP = 8.8.8.8
        raw[16] = 8
        raw[17] = 8
        raw[18] = 8
        raw[19] = 8

        // TCP Header (at offset 20)
        // Src port = 12345 (0x3039)
        raw[20] = 0x30
        raw[21] = 0x39
        // Dst port = 443 (0x01BB)
        raw[22] = 0x01
        raw[23] = 0xBB.toByte()
        // Seq = 1000
        raw[24] = 0
        raw[25] = 0
        raw[26] = 0x03
        raw[27] = 0xE8.toByte()
        // Data offset = 5 (20 bytes), Flags = SYN (0x02)
        raw[32] = 0x50
        raw[33] = 0x02

        val packet = parser.parse(buf, 40)
        assertNotNull(packet)
        assertEquals(4, packet!!.ipVersion)
        assertEquals(PacketParser.PROTOCOL_TCP, packet.protocol)
        assertEquals("10.0.0.1", packet.sourceAddress)
        assertEquals("8.8.8.8", packet.destinationAddress)
        assertEquals(12345, packet.sourcePort)
        assertEquals(443, packet.destinationPort)
        assertTrue(packet.isSyn)
        assertEquals(1000L, packet.sequenceNumber)
    }

    @Test
    fun testParseIPv4UdpPacket() {
        val raw = ByteArray(28)
        val buf = ByteBuffer.wrap(raw)

        raw[0] = 0x45.toByte()
        raw[2] = 0x00
        raw[3] = 28
        raw[9] = 17 // UDP
        raw[12] = 192.toByte()
        raw[13] = 168.toByte()
        raw[14] = 1
        raw[15] = 50
        raw[16] = 1
        raw[17] = 1
        raw[18] = 1
        raw[19] = 1

        // UDP Header at 20
        raw[20] = 0x00
        raw[21] = 53 // Src port 53
        raw[22] = 0x1F
        raw[23] = 0x90.toByte() // Dst port 8080
        raw[24] = 0x00
        raw[25] = 8 // UDP length 8

        val packet = parser.parse(buf, 28)
        assertNotNull(packet)
        assertEquals(4, packet!!.ipVersion)
        assertEquals(PacketParser.PROTOCOL_UDP, packet.protocol)
        assertEquals("192.168.1.50", packet.sourceAddress)
        assertEquals("1.1.1.1", packet.destinationAddress)
        assertEquals(53, packet.sourcePort)
        assertEquals(8080, packet.destinationPort)
    }

    @Test
    fun testParseTruncatedPacketReturnsNull() {
        val raw = ByteArray(10) // Too small for IPv4 header
        val buf = ByteBuffer.wrap(raw)
        val packet = parser.parse(buf, 10)
        assertNull(packet)
    }

    @Test
    fun testParseIPv6Packet() {
        val raw = ByteArray(48)
        val buf = ByteBuffer.wrap(raw)

        // Version 6
        raw[0] = 0x60
        // Payload length = 8
        raw[4] = 0x00
        raw[5] = 0x08
        // Next header = 17 (UDP)
        raw[6] = 17

        // Src IPv6 = ::1
        raw[23] = 1
        // Dst IPv6 = ::2
        raw[39] = 2

        // UDP Header at 40
        raw[40] = 0x00
        raw[41] = 53
        raw[42] = 0x00
        raw[43] = 53
        raw[44] = 0x00
        raw[45] = 8

        val packet = parser.parse(buf, 48)
        assertNotNull(packet)
        assertEquals(6, packet!!.ipVersion)
        assertEquals(PacketParser.PROTOCOL_UDP, packet.protocol)
        assertEquals(53, packet.sourcePort)
        assertEquals(53, packet.destinationPort)
    }
}
