package com.aifirewall.app.engine.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import android.system.OsConstants
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aifirewall.app.MainActivity
import com.aifirewall.app.R
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.domain.model.AppNetworkRule
import com.aifirewall.app.domain.model.FirewallState
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.engine.FirewallStateManager
import com.aifirewall.app.engine.network.NetworkMonitor
import com.aifirewall.app.engine.network.TcpForwarder
import com.aifirewall.app.engine.network.UdpForwarder
import com.aifirewall.app.engine.parser.PacketParser
import com.aifirewall.app.engine.policy.FirewallPolicyEngine
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

class FirewallVpnService : VpnService() {

    companion object {
        private const val TAG = "FirewallVpnService"
        private const val NOTIFICATION_ID = 10101
        private const val CHANNEL_ID = "firewall_vpn_channel"
        private const val VPN_ADDRESS = "10.1.10.1"
        private const val VPN_ROUTE = "0.0.0.0"
        private const val MTU = 1500

        const val ACTION_START = "com.aifirewall.app.START_VPN"
        const val ACTION_STOP = "com.aifirewall.app.STOP_VPN"
    }

    private var serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var vpnInterface: ParcelFileDescriptor? = null
    private var vpnJob: Job? = null
    private var monitorJob: Job? = null
    private var cleanupJob: Job? = null
    private val vpnMutex = Mutex()

    private val isShuttingDown = AtomicBoolean(false)
    @Volatile
    private var vpnInputStream: FileInputStream? = null
    @Volatile
    private var vpnOutputStream: FileOutputStream? = null

    private var tcpForwarder: TcpForwarder? = null
    private var udpForwarder: UdpForwarder? = null
    
    private lateinit var policyEngine: FirewallPolicyEngine
    private lateinit var packetParser: PacketParser

    override fun onCreate() {
        super.onCreate()
        policyEngine = FirewallPolicyEngine(this)
        packetParser = PacketParser()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startFirewall()
            ACTION_STOP -> stopFirewall()
        }
        return START_NOT_STICKY
    }

    private fun startFirewall() {
        if (!serviceScope.isActive) {
            serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }
        if (!::policyEngine.isInitialized || policyEngine.isClosed) {
            policyEngine = FirewallPolicyEngine(this)
        }
        isShuttingDown.set(false)
        serviceScope.launch {
            vpnMutex.withLock {
                if (isShuttingDown.get()) {
                    return@launch
                }
                if (FirewallStateManager.firewallState.value == FirewallState.ACTIVE ||
                    FirewallStateManager.firewallState.value == FirewallState.STARTING
                ) {
                    if (vpnJob != null && vpnJob?.isActive == true) {
                        return@launch // Already starting or active
                    }
                }

                // Explicitly cancel previous jobs before starting new workers to prevent duplicates
                monitorJob?.cancel()
                cleanupJob?.cancel()
                vpnJob?.cancel()
                monitorJob = null
                cleanupJob = null
                vpnJob = null

                FirewallEventManager.init(this@FirewallVpnService)
                FirewallStateManager.setState(FirewallState.STARTING)

                withContext(Dispatchers.Main) {
                    if (Build.VERSION.SDK_INT >= 34) {
                        startForeground(
                            NOTIFICATION_ID,
                            createNotification(getString(R.string.firewall_starting)),
                            android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                        )
                    } else {
                        startForeground(
                            NOTIFICATION_ID,
                            createNotification(getString(R.string.firewall_starting))
                        )
                    }
                }

                NetworkMonitor.startMonitoring(this@FirewallVpnService)

                vpnJob = launch {
                    try {
                        if (isShuttingDown.get()) return@launch
                        val managedPackages = policyEngine.getManagedPackages()
                        if (isShuttingDown.get()) return@launch
                        establishVpn(managedPackages)

                        if (vpnInterface == null || isShuttingDown.get()) {
                            if (isShuttingDown.get()) {
                                closeVpnResources()
                                return@launch
                            }
                            throw IllegalStateException("VPN establishment failed. Descriptor is null.")
                        }

                        // Hot-reload VPN rules when managed packages change
                        monitorJob = launch {
                            policyEngine.rulesFlow
                                .map { policyEngine.getManagedPackages() }
                                .distinctUntilChanged()
                                .collect { newPackages ->
                                    if (isShuttingDown.get() || !isActive) return@collect
                                    vpnMutex.withLock {
                                        if (isActive && !isShuttingDown.get() &&
                                            FirewallStateManager.firewallState.value == FirewallState.ACTIVE
                                        ) {
                                            establishVpn(newPackages)
                                        }
                                    }
                                }
                        }

                        // Connection cleanup loop
                        cleanupJob = launch {
                            while (isActive && !isShuttingDown.get()) {
                                delay(10000)
                                if (isShuttingDown.get() || !isActive) break
                                tcpForwarder?.cleanStaleConnections()
                                udpForwarder?.cleanStaleSessions()
                            }
                        }

                        if (isShuttingDown.get()) {
                            closeVpnResources()
                            return@launch
                        }

                        FirewallStateManager.setState(FirewallState.ACTIVE)
                        updateNotification(getString(R.string.firewall_active))

                        val currentNetwork = NetworkMonitor.currentNetwork.value
                        val networkStr = currentNetwork.name

                        FirewallEventManager.dispatchEvent(
                            FirewallEventEntity(
                                timestamp = System.currentTimeMillis(),
                                packageName = packageName,
                                uid = android.os.Process.myUid(),
                                eventType = "FIREWALL_STARTED",
                                action = "ALLOW",
                                protocol = "SYSTEM",
                                transport = networkStr,
                                sourceAddress = "",
                                sourcePort = 0,
                                destinationAddress = "",
                                destinationPort = 0,
                                bytes = 0,
                                reason = "Firewall protection active with real TCP/UDP engine",
                                direction = "INTERNAL"
                            )
                        )

                        processPackets()
                    } catch (e: Exception) {
                        if (e is CancellationException || isShuttingDown.get()) {
                            Log.d(TAG, "Firewall stopped or cancelled normally")
                            return@launch
                        }
                        Log.e(TAG, "Error starting firewall", e)
                        FirewallStateManager.setState(FirewallState.ERROR)

                        FirewallEventManager.dispatchEvent(
                            FirewallEventEntity(
                                timestamp = System.currentTimeMillis(),
                                packageName = packageName,
                                uid = android.os.Process.myUid(),
                                eventType = "FIREWALL_ERROR",
                                action = "BLOCK",
                                protocol = "SYSTEM",
                                transport = "UNKNOWN",
                                sourceAddress = "",
                                sourcePort = 0,
                                destinationAddress = "",
                                destinationPort = 0,
                                bytes = 0,
                                reason = e.message ?: "Unknown Establishment Error",
                                direction = "INTERNAL"
                            )
                        )

                        withContext(Dispatchers.Main) {
                            updateNotification("Firewall Error: ${e.localizedMessage ?: "Start Failed"}")
                            stopForeground(true)
                            stopSelf()
                        }
                    }
                }
            }
        }
    }

    private suspend fun establishVpn(managedPackages: List<String>) = withContext(Dispatchers.IO) {
        if (isShuttingDown.get()) return@withContext

        val builder = Builder()
            .setSession(getString(R.string.app_name))
            .setMtu(MTU)
            .addAddress(VPN_ADDRESS, 24)
            .addRoute(VPN_ROUTE, 0)
            .addAddress("fd00:1:fd00:1:fd00:1:fd00:1", 128)
            .addRoute("::", 0)
            .addDnsServer("8.8.8.8")
            .addDnsServer("1.1.1.1")
            .addDnsServer("2001:4860:4860::8888")
            .setBlocking(true)

        var hasAddedApp = false
        for (pkg in managedPackages) {
            try {
                builder.addAllowedApplication(pkg)
                hasAddedApp = true
            } catch (e: PackageManager.NameNotFoundException) {
                Log.w(TAG, "Managed package not found: $pkg")
            }
        }

        if (!hasAddedApp) {
            try {
                builder.addAllowedApplication(packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                // Ignore
            }
        }

        val oldInterface = vpnInterface
        val newInterface = builder.establish()
            ?: throw IllegalStateException("Failed to establish VPN interface. Permission may be revoked.")

        if (isShuttingDown.get()) {
            try {
                newInterface.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing newly established interface during shutdown", e)
            }
            return@withContext
        }

        vpnInterface = newInterface

        try {
            oldInterface?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing old VPN interface", e)
        }
    }

    private suspend fun processPackets() = withContext(Dispatchers.IO) {
        val buffer = ByteBuffer.allocate(MTU)
        
        val cm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        } else null

        try {
            var currentFd: java.io.FileDescriptor? = null

            while (isActive && !isShuttingDown.get()) {
                val vpnFd = vpnInterface?.fileDescriptor
                if (vpnFd == null) {
                    if (isShuttingDown.get()) break
                    delay(100)
                    continue
                }

                if (currentFd != vpnFd || vpnInputStream == null) {
                    currentFd = vpnFd
                    val inStream = FileInputStream(vpnFd)
                    val outStream = FileOutputStream(vpnFd)
                    vpnInputStream = inStream
                    vpnOutputStream = outStream
                    
                    tcpForwarder?.stop()
                    udpForwarder?.stop()
                    tcpForwarder = TcpForwarder(this@FirewallVpnService, outStream)
                    udpForwarder = UdpForwarder(this@FirewallVpnService, outStream)
                }

                val inStream = vpnInputStream ?: break
                
                buffer.clear()
                val length = try {
                    inStream.read(buffer.array())
                } catch (e: Exception) {
                    if (isShuttingDown.get() || !isActive) {
                        break
                    }
                    vpnInputStream = null
                    vpnOutputStream = null
                    currentFd = null
                    continue
                }

                if (length <= 0) {
                    if (isShuttingDown.get() || !isActive) {
                        break
                    }
                    continue
                }
                
                val packet = packetParser.parse(buffer, length)
                if (packet != null) {
                    var resolvedUid = AppNetworkRule.UNKNOWN_UID
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cm != null) {
                        try {
                            val proto = if (packet.protocol == PacketParser.PROTOCOL_TCP) OsConstants.IPPROTO_TCP 
                                        else if (packet.protocol == PacketParser.PROTOCOL_UDP) OsConstants.IPPROTO_UDP
                                        else 0
                            
                            if (proto != 0) {
                                val src = InetSocketAddress(packet.sourceAddress, packet.sourcePort)
                                val dst = InetSocketAddress(packet.destinationAddress, packet.destinationPort)
                                val uidFromCm = cm.getConnectionOwnerUid(proto, src, dst)
                                if (uidFromCm > 0) {
                                    resolvedUid = uidFromCm
                                }
                            }
                        } catch (e: Exception) {
                            // Fallback to UNKNOWN_UID on error
                        }
                    }
                    
                    val resolvedPackage = policyEngine.resolvePackageForUid(resolvedUid)
                    val currentNetwork = NetworkMonitor.currentNetwork.value
                    val policyDecision = policyEngine.evaluate(resolvedUid, currentNetwork)
                    
                    val networkStr = currentNetwork.name
                    val protocolStr = when (packet.protocol) {
                        PacketParser.PROTOCOL_TCP -> "TCP"
                        PacketParser.PROTOCOL_UDP -> "UDP"
                        PacketParser.PROTOCOL_ICMP -> "ICMP"
                        PacketParser.PROTOCOL_ICMPV6 -> "ICMPv6"
                        else -> "OTHER"
                    }
                    
                    if (policyDecision == NetworkPolicy.BLOCK) {
                        if (packet.protocol == PacketParser.PROTOCOL_TCP) {
                            tcpForwarder?.handleTcpPacket(packet, isAllowed = false, onBlocked = {
                                dispatchBlockEvent(resolvedPackage, resolvedUid, protocolStr, networkStr, packet)
                            })
                        } else {
                            dispatchBlockEvent(resolvedPackage, resolvedUid, protocolStr, networkStr, packet)
                        }
                    } else {
                        if (packet.protocol == PacketParser.PROTOCOL_TCP) {
                            tcpForwarder?.handleTcpPacket(packet, isAllowed = true, onBlocked = {
                                dispatchBlockEvent(resolvedPackage, resolvedUid, protocolStr, networkStr, packet)
                            })
                        } else if (packet.protocol == PacketParser.PROTOCOL_UDP && packet.payload != null) {
                            udpForwarder?.forwardUdpPacket(
                                srcIp = packet.sourceAddress,
                                dstIp = packet.destinationAddress,
                                srcPort = packet.sourcePort,
                                dstPort = packet.destinationPort,
                                payload = packet.payload,
                                isIPv6 = (packet.ipVersion == 6)
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (e !is CancellationException && !isShuttingDown.get()) {
                Log.e(TAG, "Packet processing loop interrupted", e)
            }
        } finally {
            tcpForwarder?.stop()
            udpForwarder?.stop()
        }
    }

    private fun dispatchBlockEvent(
        packageName: String,
        uid: Int,
        protocolStr: String,
        networkStr: String,
        packet: PacketParser.ParsedPacket
    ) {
        FirewallEventManager.dispatchEvent(
            FirewallEventEntity(
                timestamp = System.currentTimeMillis(),
                packageName = packageName,
                uid = uid,
                eventType = "CONNECTION_BLOCKED",
                action = "BLOCK",
                protocol = protocolStr,
                transport = networkStr,
                sourceAddress = packet.sourceAddress,
                sourcePort = packet.sourcePort,
                destinationAddress = packet.destinationAddress,
                destinationPort = packet.destinationPort,
                bytes = packet.payloadLength.toLong(),
                reason = "Policy evaluated to BLOCK for network $networkStr",
                direction = "OUTBOUND"
            )
        )
    }

    private fun closeVpnResources() {
        try {
            vpnInputStream?.close()
        } catch (e: IOException) {
            Log.d(TAG, "VPN input stream closed")
        } catch (e: Exception) {
            Log.w(TAG, "Error closing VPN input stream", e)
        } finally {
            vpnInputStream = null
        }

        try {
            vpnOutputStream?.close()
        } catch (e: IOException) {
            Log.d(TAG, "VPN output stream closed")
        } catch (e: Exception) {
            Log.w(TAG, "Error closing VPN output stream", e)
        } finally {
            vpnOutputStream = null
        }

        try {
            vpnInterface?.close()
        } catch (e: IOException) {
            Log.d(TAG, "VPN interface closed")
        } catch (e: Exception) {
            Log.w(TAG, "Error closing VPN interface", e)
        } finally {
            vpnInterface = null
        }
    }

    private fun cleanupResources() {
        isShuttingDown.set(true)

        // Close actual VPN descriptor and streams FIRST to unblock blocking I/O
        closeVpnResources()

        // Cancel jobs
        monitorJob?.cancel()
        cleanupJob?.cancel()
        vpnJob?.cancel()
        monitorJob = null
        cleanupJob = null
        vpnJob = null

        // Stop forwarders
        tcpForwarder?.stop()
        tcpForwarder = null
        udpForwarder?.stop()
        udpForwarder = null

        // Stop network monitor
        NetworkMonitor.stopMonitoring()
    }

    private fun stopFirewall() {
        if (FirewallStateManager.firewallState.value == FirewallState.INACTIVE &&
            vpnInterface == null && vpnJob == null
        ) {
            return
        }

        if (!isShuttingDown.compareAndSet(false, true)) {
            // Already shutting down
            return
        }

        FirewallStateManager.setState(FirewallState.STOPPING)

        // Release blocking I/O and cancel jobs immediately
        cleanupResources()

        if (!serviceScope.isActive) {
            serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }

        serviceScope.launch {
            try {
                vpnMutex.withLock {
                    cleanupResources()

                    FirewallStateManager.setState(FirewallState.INACTIVE)

                    FirewallEventManager.dispatchEvent(
                        FirewallEventEntity(
                            timestamp = System.currentTimeMillis(),
                            packageName = packageName,
                            uid = android.os.Process.myUid(),
                            eventType = "FIREWALL_STOPPED",
                            action = "ALLOW",
                            protocol = "SYSTEM",
                            transport = "UNKNOWN",
                            sourceAddress = "",
                            sourcePort = 0,
                            destinationAddress = "",
                            destinationPort = 0,
                            bytes = 0,
                            reason = "Firewall protection disabled",
                            direction = "INTERNAL"
                        )
                    )

                    withContext(Dispatchers.Main) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            stopForeground(STOP_FOREGROUND_REMOVE)
                        } else {
                            @Suppress("DEPRECATION")
                            stopForeground(true)
                        }
                        stopSelf()
                    }
                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    Log.e(TAG, "Error stopping firewall", e)
                }
                FirewallStateManager.setState(FirewallState.INACTIVE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanupResources()

        if (::policyEngine.isInitialized) {
            policyEngine.close()
        }

        serviceScope.cancel()
        FirewallStateManager.setState(FirewallState.INACTIVE)
    }

    override fun onRevoke() {
        super.onRevoke()
        stopFirewall()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.firewall_active)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(contentText: String): Notification {
        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, FirewallVpnService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openPendingIntent)
            .addAction(
                R.drawable.ic_launcher,
                "Stop Firewall",
                stopPendingIntent
            )
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(contentText: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, createNotification(contentText))
    }
}

