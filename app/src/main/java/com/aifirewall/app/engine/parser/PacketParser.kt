package com.aifirewall.app.engine.parser

import java.net.InetAddress
import java.nio.ByteBuffer

/**
 * High-performance, memory-safe IPv4 & IPv6 packet parser.
 * Supports TCP control flags, sequence/ack numbers, IPv6 extension headers, and fragmentation safety.
 */
class PacketParser {

    data class ParsedPacket(
        val ipVersion: Int,
        val protocol: Int,
        val sourceAddress: String,
        val destinationAddress: String,
        val sourcePort: Int,
        val destinationPort: Int,
        val payloadLength: Int,
        val payload: ByteArray? = null,
        val tcpFlags: Int = 0,
        val sequenceNumber: Long = 0L,
        val ackNumber: Long = 0L,
        val isFragmented: Boolean = false,
        val rawSrcIpBytes: ByteArray = ByteArray(0),
        val rawDstIpBytes: ByteArray = ByteArray(0)
    ) {
        val isSyn: Boolean get() = (tcpFlags and FLAG_SYN) != 0
        val isAck: Boolean get() = (tcpFlags and FLAG_ACK) != 0
        val isFin: Boolean get() = (tcpFlags and FLAG_FIN) != 0
        val isRst: Boolean get() = (tcpFlags and FLAG_RST) != 0
        val isPsh: Boolean get() = (tcpFlags and FLAG_PSH) != 0
    }

    companion object {
        const val PROTOCOL_ICMP = 1
        const val PROTOCOL_TCP = 6
        const val PROTOCOL_UDP = 17
        const val PROTOCOL_ICMPV6 = 58

        const val FLAG_FIN = 0x01
        const val FLAG_SYN = 0x02
        const val FLAG_RST = 0x04
        const val FLAG_PSH = 0x08
        const val FLAG_ACK = 0x10
        const val FLAG_URG = 0x20

        // IPv6 Extension Header types
        private val IPV6_EXT_HEADERS = setOf(0, 43, 44, 50, 51, 60)
    }

    /**
     * Attempts to parse a raw IP packet buffer.
     * Returns a ParsedPacket if valid, or null if malformed or truncated.
     */
    fun parse(buffer: ByteBuffer, length: Int): ParsedPacket? {
        if (length < 20) return null

        buffer.position(0)
        val versionAndIHL = buffer.get().toInt() and 0xFF
        val version = (versionAndIHL shr 4) and 0x0F

        if (version == 4) {
            val ihl = versionAndIHL and 0x0F
            val headerLength = ihl * 4
            if (headerLength < 20 || length < headerLength) return null

            buffer.position(2)
            val totalLength = buffer.short.toInt() and 0xFFFF
            if (length < totalLength || totalLength < headerLength) return null

            buffer.position(6)
            val flagsAndFragment = buffer.short.toInt() and 0xFFFF
            val fragmentOffset = (flagsAndFragment and 0x1FFF) * 8
            val moreFragments = (flagsAndFragment and 0x2000) != 0
            val isFragmented = fragmentOffset > 0 || moreFragments

            buffer.position(9)
            val protocol = buffer.get().toInt() and 0xFF

            buffer.position(12)
            val srcIpBytes = ByteArray(4)
            buffer.get(srcIpBytes)
            val sourceAddress = InetAddress.getByAddress(srcIpBytes).hostAddress ?: ""

            val dstIpBytes = ByteArray(4)
            buffer.get(dstIpBytes)
            val destinationAddress = InetAddress.getByAddress(dstIpBytes).hostAddress ?: ""

            var sourcePort = 0
            var destinationPort = 0
            var tcpFlags = 0
            var sequenceNumber = 0L
            var ackNumber = 0L
            var payloadBytes: ByteArray? = null
            val transportHeaderOffset = headerLength

            if (!isFragmented || fragmentOffset == 0) {
                if (protocol == PROTOCOL_TCP) {
                    if (length >= transportHeaderOffset + 20) {
                        buffer.position(transportHeaderOffset)
                        sourcePort = buffer.short.toInt() and 0xFFFF
                        destinationPort = buffer.short.toInt() and 0xFFFF
                        sequenceNumber = buffer.int.toLong() and 0xFFFFFFFFL
                        ackNumber = buffer.int.toLong() and 0xFFFFFFFFL
                        val dataOffsetAndFlags = buffer.short.toInt() and 0xFFFF
                        val tcpHeaderLength = ((dataOffsetAndFlags shr 12) and 0x0F) * 4
                        tcpFlags = dataOffsetAndFlags and 0x3F

                        val payloadStart = transportHeaderOffset + tcpHeaderLength
                        if (totalLength >= payloadStart) {
                            val payloadSize = totalLength - payloadStart
                            if (payloadSize > 0 && length >= payloadStart + payloadSize) {
                                buffer.position(payloadStart)
                                payloadBytes = ByteArray(payloadSize)
                                buffer.get(payloadBytes)
                            }
                        }
                    }
                } else if (protocol == PROTOCOL_UDP) {
                    if (length >= transportHeaderOffset + 8) {
                        buffer.position(transportHeaderOffset)
                        sourcePort = buffer.short.toInt() and 0xFFFF
                        destinationPort = buffer.short.toInt() and 0xFFFF
                        val udpLength = buffer.short.toInt() and 0xFFFF
                        val payloadStart = transportHeaderOffset + 8
                        if (udpLength >= 8 && totalLength >= payloadStart) {
                            val payloadSize = minOf(udpLength - 8, totalLength - payloadStart)
                            if (payloadSize > 0 && length >= payloadStart + payloadSize) {
                                buffer.position(payloadStart)
                                payloadBytes = ByteArray(payloadSize)
                                buffer.get(payloadBytes)
                            }
                        }
                    }
                }
            }

            return ParsedPacket(
                ipVersion = version,
                protocol = protocol,
                sourceAddress = sourceAddress,
                destinationAddress = destinationAddress,
                sourcePort = sourcePort,
                destinationPort = destinationPort,
                payloadLength = totalLength - headerLength,
                payload = payloadBytes,
                tcpFlags = tcpFlags,
                sequenceNumber = sequenceNumber,
                ackNumber = ackNumber,
                isFragmented = isFragmented,
                rawSrcIpBytes = srcIpBytes,
                rawDstIpBytes = dstIpBytes
            )
        } else if (version == 6) {
            if (length < 40) return null

            buffer.position(4)
            val payloadLengthIPv6 = buffer.short.toInt() and 0xFFFF
            var nextHeader = buffer.get().toInt() and 0xFF

            buffer.position(8)
            val srcIpBytes = ByteArray(16)
            buffer.get(srcIpBytes)
            val sourceAddress = InetAddress.getByAddress(srcIpBytes).hostAddress ?: ""

            val dstIpBytes = ByteArray(16)
            buffer.get(dstIpBytes)
            val destinationAddress = InetAddress.getByAddress(dstIpBytes).hostAddress ?: ""

            var headerOffset = 40
            var extHeaderCount = 0

            // Traversal for IPv6 extension headers (safely bounded)
            while (IPV6_EXT_HEADERS.contains(nextHeader) && extHeaderCount < 8 && length >= headerOffset + 2) {
                buffer.position(headerOffset)
                val currNextHeader = buffer.get().toInt() and 0xFF
                val extLen = (buffer.get().toInt() and 0xFF + 1) * 8
                nextHeader = currNextHeader
                headerOffset += extLen
                extHeaderCount++
            }

            var sourcePort = 0
            var destinationPort = 0
            var tcpFlags = 0
            var sequenceNumber = 0L
            var ackNumber = 0L
            var payloadBytes: ByteArray? = null

            if (nextHeader == PROTOCOL_TCP) {
                if (length >= headerOffset + 20) {
                    buffer.position(headerOffset)
                    sourcePort = buffer.short.toInt() and 0xFFFF
                    destinationPort = buffer.short.toInt() and 0xFFFF
                    sequenceNumber = buffer.int.toLong() and 0xFFFFFFFFL
                    ackNumber = buffer.int.toLong() and 0xFFFFFFFFL
                    val dataOffsetAndFlags = buffer.short.toInt() and 0xFFFF
                    val tcpHeaderLength = ((dataOffsetAndFlags shr 12) and 0x0F) * 4
                    tcpFlags = dataOffsetAndFlags and 0x3F

                    val payloadStart = headerOffset + tcpHeaderLength
                    val totalPacketLength = 40 + payloadLengthIPv6
                    if (totalPacketLength >= payloadStart) {
                        val payloadSize = totalPacketLength - payloadStart
                        if (payloadSize > 0 && length >= payloadStart + payloadSize) {
                            buffer.position(payloadStart)
                            payloadBytes = ByteArray(payloadSize)
                            buffer.get(payloadBytes)
                        }
                    }
                }
            } else if (nextHeader == PROTOCOL_UDP) {
                if (length >= headerOffset + 8) {
                    buffer.position(headerOffset)
                    sourcePort = buffer.short.toInt() and 0xFFFF
                    destinationPort = buffer.short.toInt() and 0xFFFF
                    val udpLength = buffer.short.toInt() and 0xFFFF
                    val payloadStart = headerOffset + 8
                    if (udpLength >= 8) {
                        val payloadSize = udpLength - 8
                        if (payloadSize > 0 && length >= payloadStart + payloadSize) {
                            buffer.position(payloadStart)
                            payloadBytes = ByteArray(payloadSize)
                            buffer.get(payloadBytes)
                        }
                    }
                }
            }

            return ParsedPacket(
                ipVersion = version,
                protocol = nextHeader,
                sourceAddress = sourceAddress,
                destinationAddress = destinationAddress,
                sourcePort = sourcePort,
                destinationPort = destinationPort,
                payloadLength = payloadLengthIPv6,
                payload = payloadBytes,
                tcpFlags = tcpFlags,
                sequenceNumber = sequenceNumber,
                ackNumber = ackNumber,
                isFragmented = false,
                rawSrcIpBytes = srcIpBytes,
                rawDstIpBytes = dstIpBytes
            )
        }

        return null
    }
}
