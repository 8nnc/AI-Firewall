package com.aifirewall.app.engine.network

import android.net.VpnService
import android.util.Log
import com.aifirewall.app.engine.parser.ChecksumUtils
import com.aifirewall.app.engine.parser.PacketParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class UdpForwarder(
    private val vpnService: VpnService,
    private val vpnOutput: FileOutputStream
) {
    companion object {
        private const val TAG = "UdpForwarder"
        private const val DEFAULT_TIMEOUT_MS = 30000
        private const val DNS_TIMEOUT_MS = 10000
        private const val PORT_DNS = 53
    }

    data class UdpFlowKey(
        val srcIp: String,
        val srcPort: Int,
        val dstIp: String,
        val dstPort: Int,
        val isIPv6: Boolean
    )

    private class UdpFlowSession(
        val key: UdpFlowKey,
        val socket: DatagramSocket,
        val packetQueue: Channel<ByteArray>,
        val job: Job
    ) {
        @Volatile
        var lastActivityTime: Long = System.currentTimeMillis()

        fun touch() {
            lastActivityTime = System.currentTimeMillis()
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeSessions = ConcurrentHashMap<UdpFlowKey, UdpFlowSession>()

    fun forwardUdpPacket(
        srcIp: String,
        dstIp: String,
        srcPort: Int,
        dstPort: Int,
        payload: ByteArray,
        isIPv6: Boolean
    ) {
        val flowKey = UdpFlowKey(srcIp, srcPort, dstIp, dstPort, isIPv6)
        var session = activeSessions[flowKey]

        if (session != null) {
            session.touch()
            session.packetQueue.trySend(payload)
            return
        }

        // New UDP flow session
        val packetQueue = Channel<ByteArray>(Channel.UNLIMITED)
        packetQueue.trySend(payload)

        val job = scope.launch {
            var socket: DatagramSocket? = null
            try {
                socket = DatagramSocket()
                // CRITICAL: Protect socket from VPN routing loops
                if (!vpnService.protect(socket)) {
                    Log.e(TAG, "Failed to protect UDP socket for $flowKey")
                    return@launch
                }

                val destAddress = InetAddress.getByName(dstIp)
                val timeout = if (dstPort == PORT_DNS || srcPort == PORT_DNS) DNS_TIMEOUT_MS else DEFAULT_TIMEOUT_MS
                socket.soTimeout = timeout

                val flowSession = UdpFlowSession(flowKey, socket, packetQueue, coroutineContext[Job]!!)
                activeSessions[flowKey] = flowSession

                // Sender Coroutine (Outbound queue reader -> Remote UDP Server)
                val senderJob = launch {
                    for (outboundPayload in packetQueue) {
                        try {
                            val packet = DatagramPacket(outboundPayload, outboundPayload.size, destAddress, dstPort)
                            socket.send(packet)
                            flowSession.touch()
                        } catch (e: Exception) {
                            break
                        }
                    }
                }

                // Receiver Loop (Remote UDP Server -> TUN interface)
                val receiveBuffer = ByteArray(65535)
                while (isActive) {
                    val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)
                    socket.receive(receivePacket)

                    flowSession.touch()
                    val responsePayload = receiveBuffer.copyOfRange(0, receivePacket.length)
                    val responseIpPacket = buildUdpPacket(
                        srcIp = dstIp,
                        dstIp = srcIp,
                        srcPort = dstPort,
                        dstPort = srcPort,
                        payload = responsePayload,
                        isIPv6 = isIPv6
                    )

                    synchronized(vpnOutput) {
                        try {
                            vpnOutput.write(responseIpPacket)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error writing UDP return packet to TUN", e)
                        }
                    }
                }
                senderJob.cancel()
            } catch (e: Exception) {
                // Flow terminated or timed out
            } finally {
                socket?.close()
                activeSessions.remove(flowKey)
            }
        }
    }

    private fun buildUdpPacket(
        srcIp: String,
        dstIp: String,
        srcPort: Int,
        dstPort: Int,
        payload: ByteArray,
        isIPv6: Boolean
    ): ByteArray {
        val udpHeaderLen = 8
        val payloadSize = payload.size
        val udpTotalLen = udpHeaderLen + payloadSize

        if (!isIPv6) {
            val ipHeaderLen = 20
            val totalLength = ipHeaderLen + udpTotalLen
            val buffer = ByteBuffer.allocate(totalLength)

            // IPv4 Header
            buffer.put(0x45.toByte())
            buffer.put(0.toByte())
            buffer.putShort(totalLength.toShort())
            buffer.putShort(Random.nextInt(0, 65535).toShort())
            buffer.putShort(0)
            buffer.put(64.toByte()) // TTL
            buffer.put(PacketParser.PROTOCOL_UDP.toByte())
            buffer.putShort(0) // Checksum placeholder

            val srcBytes = InetAddress.getByName(srcIp).address
            val dstBytes = InetAddress.getByName(dstIp).address
            buffer.put(srcBytes)
            buffer.put(dstBytes)

            // IPv4 Header Checksum
            val ipChecksum = ChecksumUtils.calculateIpHeaderChecksum(buffer, 0, ipHeaderLen)
            buffer.putShort(10, ipChecksum)

            // UDP Header
            buffer.position(ipHeaderLen)
            buffer.putShort(srcPort.toShort())
            buffer.putShort(dstPort.toShort())
            buffer.putShort(udpTotalLen.toShort())
            buffer.putShort(0) // Checksum placeholder

            buffer.put(payload)

            // UDP Checksum over IPv4
            val transportBytes = ByteArray(udpTotalLen)
            buffer.position(ipHeaderLen)
            buffer.get(transportBytes)

            val udpChecksum = ChecksumUtils.calculateTransportChecksumIPv4(
                srcBytes, dstBytes, PacketParser.PROTOCOL_UDP, transportBytes
            )
            buffer.putShort(ipHeaderLen + 6, udpChecksum)

            return buffer.array()
        } else {
            val ipHeaderLen = 40
            val totalLength = ipHeaderLen + udpTotalLen
            val buffer = ByteBuffer.allocate(totalLength)

            // IPv6 Header
            buffer.putInt(0x60000000)
            buffer.putShort(udpTotalLen.toShort())
            buffer.put(PacketParser.PROTOCOL_UDP.toByte())
            buffer.put(64.toByte())

            val srcBytes = InetAddress.getByName(srcIp).address
            val dstBytes = InetAddress.getByName(dstIp).address
            buffer.put(srcBytes)
            buffer.put(dstBytes)

            // UDP Header
            buffer.position(ipHeaderLen)
            buffer.putShort(srcPort.toShort())
            buffer.putShort(dstPort.toShort())
            buffer.putShort(udpTotalLen.toShort())
            buffer.putShort(0) // Checksum placeholder

            buffer.put(payload)

            // UDP Checksum over IPv6 (Mandatory for IPv6!)
            val transportBytes = ByteArray(udpTotalLen)
            buffer.position(ipHeaderLen)
            buffer.get(transportBytes)

            val udpChecksum = ChecksumUtils.calculateTransportChecksumIPv6(
                srcBytes, dstBytes, PacketParser.PROTOCOL_UDP, transportBytes
            )
            buffer.putShort(ipHeaderLen + 6, udpChecksum)

            return buffer.array()
        }
    }

    fun cleanStaleSessions() {
        val now = System.currentTimeMillis()
        activeSessions.forEach { (key, session) ->
            val timeout = if (key.dstPort == PORT_DNS || key.srcPort == PORT_DNS) DNS_TIMEOUT_MS else DEFAULT_TIMEOUT_MS
            if (now - session.lastActivityTime > timeout) {
                session.job.cancel()
                try {
                    session.socket.close()
                } catch (e: Exception) {
                    // Ignore
                }
                activeSessions.remove(key)
            }
        }
    }

    fun stop() {
        activeSessions.values.forEach { session ->
            session.job.cancel()
            try {
                session.socket.close()
            } catch (e: Exception) {
                // Ignore
            }
        }
        activeSessions.clear()
    }
}
