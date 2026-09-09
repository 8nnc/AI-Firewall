package com.aifirewall.app.engine.network

import android.net.VpnService
import android.util.Log
import com.aifirewall.app.engine.parser.ChecksumUtils
import com.aifirewall.app.engine.parser.PacketParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class TcpForwarder(
    private val vpnService: VpnService,
    private val vpnOutput: FileOutputStream
) {
    companion object {
        private const val TAG = "TcpForwarder"
        private const val CONNECT_TIMEOUT_MS = 15000
        private const val IDLE_TIMEOUT_MS = 120000L
    }

    data class TcpFlowKey(
        val srcIp: String,
        val srcPort: Int,
        val dstIp: String,
        val dstPort: Int,
        val isIPv6: Boolean
    )

    enum class TcpState {
        CLOSED,
        SYN_SENT,
        ESTABLISHED,
        FIN_WAIT,
        CLOSE_WAIT,
        LAST_ACK,
        TIME_WAIT,
        RESET
    }

    private class TcpConnection(
        val key: TcpFlowKey,
        val scope: CoroutineScope,
        val vpnOutput: FileOutputStream
    ) {
        var state: TcpState = TcpState.CLOSED
        var channel: SocketChannel? = null
        var job: Job? = null

        var clientSeq: Long = 0L
        var clientAck: Long = 0L
        var serverSeq: Long = Random.nextLong(1000, 1000000)
        var serverAck: Long = 0L

        @Volatile
        var lastActivityTime: Long = System.currentTimeMillis()

        fun updateActivity() {
            lastActivityTime = System.currentTimeMillis()
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeConnections = ConcurrentHashMap<TcpFlowKey, TcpConnection>()

    fun handleTcpPacket(packet: PacketParser.ParsedPacket, isAllowed: Boolean, onBlocked: () -> Unit) {
        val isIPv6 = packet.ipVersion == 6
        val flowKey = TcpFlowKey(
            srcIp = packet.sourceAddress,
            srcPort = packet.sourcePort,
            dstIp = packet.destinationAddress,
            dstPort = packet.destinationPort,
            isIPv6 = isIPv6
        )

        var conn = activeConnections[flowKey]

        if (!isAllowed) {
            // Blocked by policy engine
            if (conn == null && packet.isSyn) {
                sendRstPacket(packet)
                onBlocked()
            } else if (conn != null) {
                closeConnection(flowKey)
                sendRstPacket(packet)
                onBlocked()
            }
            return
        }

        if (packet.isSyn && !packet.isAck) {
            // Initial SYN packet for new TCP connection
            conn?.let { closeConnection(flowKey) } // Clean up stale flow if present

            val newConn = TcpConnection(flowKey, scope, vpnOutput).apply {
                clientSeq = packet.sequenceNumber
                state = TcpState.SYN_SENT
                updateActivity()
            }

            activeConnections[flowKey] = newConn
            startOutboundConnection(newConn, packet)
            return
        }

        if (conn == null) {
            // Received non-SYN packet for untracked connection
            if (!packet.isRst) {
                sendRstPacket(packet)
            }
            return
        }

        conn.updateActivity()

        if (packet.isRst) {
            closeConnection(flowKey)
            return
        }

        if (packet.isFin) {
            conn.state = TcpState.CLOSE_WAIT
            scope.launch {
                try {
                    conn.channel?.socket()?.shutdownOutput()
                } catch (e: Exception) {
                    // Ignore
                }
                sendAckPacket(conn, packet.sequenceNumber + 1, packet.ackNumber, PacketParser.FLAG_ACK or PacketParser.FLAG_FIN)
                closeConnection(flowKey)
            }
            return
        }

        // Process TCP payload
        if (packet.payload != null && packet.payload.isNotEmpty()) {
            val payload = packet.payload
            conn.clientSeq = packet.sequenceNumber + payload.size
            val currentChannel = conn.channel

            if (currentChannel != null && currentChannel.isConnected) {
                scope.launch {
                    try {
                        val buf = ByteBuffer.wrap(payload)
                        while (buf.hasRemaining()) {
                            currentChannel.write(buf)
                        }
                        sendAckPacket(conn, conn.clientSeq, conn.serverSeq, PacketParser.FLAG_ACK)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error writing TCP payload to external channel", e)
                        sendRstPacket(packet)
                        closeConnection(flowKey)
                    }
                }
            }
        }
    }

    private fun startOutboundConnection(conn: TcpConnection, initialPacket: PacketParser.ParsedPacket) {
        conn.job = scope.launch {
            var socketChannel: SocketChannel? = null
            try {
                socketChannel = SocketChannel.open()
                socketChannel.configureBlocking(true)
                
                // CRITICAL: Protect socket to bypass VPN loop
                if (!vpnService.protect(socketChannel.socket())) {
                    Log.e(TAG, "Failed to protect TCP socket for ${conn.key}")
                    sendRstPacket(initialPacket)
                    closeConnection(conn.key)
                    return@launch
                }

                val destAddress = InetSocketAddress(InetAddress.getByName(conn.key.dstIp), conn.key.dstPort)
                socketChannel.socket().connect(destAddress, CONNECT_TIMEOUT_MS)

                conn.channel = socketChannel
                conn.state = TcpState.ESTABLISHED
                conn.serverAck = conn.clientSeq + 1

                // Send SYN-ACK packet back to TUN
                sendSynAckPacket(conn)

                // Start reading return traffic from external socket
                val readBuffer = ByteBuffer.allocate(32768)
                while (isActive && conn.state == TcpState.ESTABLISHED) {
                    readBuffer.clear()
                    val bytesRead = try {
                        socketChannel.read(readBuffer)
                    } catch (e: Exception) {
                        -1
                    }

                    if (bytesRead > 0) {
                        readBuffer.flip()
                        val returnData = ByteArray(bytesRead)
                        readBuffer.get(returnData)

                        sendDataPacket(conn, returnData)
                        conn.updateActivity()
                    } else if (bytesRead == -1) {
                        // Remote end closed connection (FIN/EOF)
                        sendAckPacket(conn, conn.clientSeq, conn.serverSeq, PacketParser.FLAG_FIN or PacketParser.FLAG_ACK)
                        conn.serverSeq++
                        conn.state = TcpState.CLOSED
                        break
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "TCP connection failed to ${conn.key.dstIp}:${conn.key.dstPort}: ${e.message}")
                sendRstPacket(initialPacket)
            } finally {
                closeConnection(conn.key)
            }
        }
    }

    private fun sendSynAckPacket(conn: TcpConnection) {
        val packetBytes = buildTcpPacket(
            srcIp = conn.key.dstIp,
            dstIp = conn.key.srcIp,
            srcPort = conn.key.dstPort,
            dstPort = conn.key.srcPort,
            seq = conn.serverSeq,
            ack = conn.serverAck,
            flags = PacketParser.FLAG_SYN or PacketParser.FLAG_ACK,
            payload = null,
            isIPv6 = conn.key.isIPv6
        )
        conn.serverSeq++
        synchronized(vpnOutput) {
            try {
                vpnOutput.write(packetBytes)
            } catch (e: Exception) {
                Log.e(TAG, "Error writing SYN-ACK to TUN", e)
            }
        }
    }

    private fun sendAckPacket(conn: TcpConnection, ackNum: Long, seqNum: Long, flags: Int) {
        val packetBytes = buildTcpPacket(
            srcIp = conn.key.dstIp,
            dstIp = conn.key.srcIp,
            srcPort = conn.key.dstPort,
            dstPort = conn.key.srcPort,
            seq = seqNum,
            ack = ackNum,
            flags = flags,
            payload = null,
            isIPv6 = conn.key.isIPv6
        )
        synchronized(vpnOutput) {
            try {
                vpnOutput.write(packetBytes)
            } catch (e: Exception) {
                Log.e(TAG, "Error writing ACK to TUN", e)
            }
        }
    }

    private fun sendDataPacket(conn: TcpConnection, payload: ByteArray) {
        val packetBytes = buildTcpPacket(
            srcIp = conn.key.dstIp,
            dstIp = conn.key.srcIp,
            srcPort = conn.key.dstPort,
            dstPort = conn.key.srcPort,
            seq = conn.serverSeq,
            ack = conn.serverAck,
            flags = PacketParser.FLAG_ACK or PacketParser.FLAG_PSH,
            payload = payload,
            isIPv6 = conn.key.isIPv6
        )
        conn.serverSeq += payload.size
        synchronized(vpnOutput) {
            try {
                vpnOutput.write(packetBytes)
            } catch (e: Exception) {
                Log.e(TAG, "Error writing return TCP data to TUN", e)
            }
        }
    }

    private fun sendRstPacket(packet: PacketParser.ParsedPacket) {
        val isIPv6 = packet.ipVersion == 6
        val rstAck = if (packet.isSyn) packet.sequenceNumber + 1 else packet.sequenceNumber
        val packetBytes = buildTcpPacket(
            srcIp = packet.destinationAddress,
            dstIp = packet.sourceAddress,
            srcPort = packet.destinationPort,
            dstPort = packet.sourcePort,
            seq = packet.ackNumber,
            ack = rstAck,
            flags = PacketParser.FLAG_RST or PacketParser.FLAG_ACK,
            payload = null,
            isIPv6 = isIPv6
        )
        synchronized(vpnOutput) {
            try {
                vpnOutput.write(packetBytes)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private fun buildTcpPacket(
        srcIp: String,
        dstIp: String,
        srcPort: Int,
        dstPort: Int,
        seq: Long,
        ack: Long,
        flags: Int,
        payload: ByteArray?,
        isIPv6: Boolean
    ): ByteArray {
        val payloadSize = payload?.size ?: 0
        val tcpHeaderLength = 20
        
        if (!isIPv6) {
            val ipHeaderLength = 20
            val totalLength = ipHeaderLength + tcpHeaderLength + payloadSize
            val buffer = ByteBuffer.allocate(totalLength)

            // IPv4 Header
            buffer.put(0x45.toByte())
            buffer.put(0.toByte())
            buffer.putShort(totalLength.toShort())
            buffer.putShort(Random.nextInt(0, 65535).toShort())
            buffer.putShort(0x4000.toShort()) // DF flag
            buffer.put(64.toByte()) // TTL
            buffer.put(PacketParser.PROTOCOL_TCP.toByte())
            buffer.putShort(0) // Header checksum placeholder

            val srcBytes = InetAddress.getByName(srcIp).address
            val dstBytes = InetAddress.getByName(dstIp).address
            buffer.put(srcBytes)
            buffer.put(dstBytes)

            // Compute IPv4 header checksum
            val ipChecksum = ChecksumUtils.calculateIpHeaderChecksum(buffer, 0, ipHeaderLength)
            buffer.putShort(10, ipChecksum)

            // TCP Header
            buffer.position(ipHeaderLength)
            buffer.putShort(srcPort.toShort())
            buffer.putShort(dstPort.toShort())
            buffer.putInt(seq.toInt())
            buffer.putInt(ack.toInt())
            buffer.putShort(((5 shl 12) or flags).toShort()) // Data offset = 5 words (20 bytes)
            buffer.putShort(64240.toShort()) // Window size
            buffer.putShort(0) // Checksum placeholder
            buffer.putShort(0) // Urgent pointer

            if (payload != null) {
                buffer.put(payload)
            }

            // Transport Checksum over IPv4
            val transportBytes = ByteArray(tcpHeaderLength + payloadSize)
            buffer.position(ipHeaderLength)
            buffer.get(transportBytes)

            val tcpChecksum = ChecksumUtils.calculateTransportChecksumIPv4(
                srcBytes, dstBytes, PacketParser.PROTOCOL_TCP, transportBytes
            )
            buffer.putShort(ipHeaderLength + 16, tcpChecksum)

            return buffer.array()
        } else {
            val ipHeaderLength = 40
            val totalLength = ipHeaderLength + tcpHeaderLength + payloadSize
            val buffer = ByteBuffer.allocate(totalLength)

            // IPv6 Header
            buffer.putInt(0x60000000) // Version 6, Traffic Class 0, Flow Label 0
            buffer.putShort((tcpHeaderLength + payloadSize).toShort())
            buffer.put(PacketParser.PROTOCOL_TCP.toByte()) // Next header
            buffer.put(64.toByte()) // Hop limit

            val srcBytes = InetAddress.getByName(srcIp).address
            val dstBytes = InetAddress.getByName(dstIp).address
            buffer.put(srcBytes)
            buffer.put(dstBytes)

            // TCP Header
            buffer.position(ipHeaderLength)
            buffer.putShort(srcPort.toShort())
            buffer.putShort(dstPort.toShort())
            buffer.putInt(seq.toInt())
            buffer.putInt(ack.toInt())
            buffer.putShort(((5 shl 12) or flags).toShort())
            buffer.putShort(64240.toShort())
            buffer.putShort(0) // Checksum placeholder
            buffer.putShort(0)

            if (payload != null) {
                buffer.put(payload)
            }

            // Transport Checksum over IPv6
            val transportBytes = ByteArray(tcpHeaderLength + payloadSize)
            buffer.position(ipHeaderLength)
            buffer.get(transportBytes)

            val tcpChecksum = ChecksumUtils.calculateTransportChecksumIPv6(
                srcBytes, dstBytes, PacketParser.PROTOCOL_TCP, transportBytes
            )
            buffer.putShort(ipHeaderLength + 16, tcpChecksum)

            return buffer.array()
        }
    }

    private fun closeConnection(key: TcpFlowKey) {
        val conn = activeConnections.remove(key) ?: return
        conn.state = TcpState.CLOSED
        conn.job?.cancel()
        try {
            conn.channel?.close()
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun cleanStaleConnections() {
        val now = System.currentTimeMillis()
        activeConnections.forEach { (key, conn) ->
            if (now - conn.lastActivityTime > IDLE_TIMEOUT_MS) {
                closeConnection(key)
            }
        }
    }

    fun stop() {
        activeConnections.keys.toList().forEach { key ->
            closeConnection(key)
        }
        activeConnections.clear()
    }
}
