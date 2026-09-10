package com.aifirewall.app.engine.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aifirewall.app.MainActivity
import com.aifirewall.app.R
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Enterprise Firewall Notification Dispatcher & Rate-Limiting Engine.
 *
 * Enforces batched aggregation on blocked connection events to eliminate
 * notification storms, tray flooding, and audio spam while retaining immediate
 * alerts for critical security events.
 */
class FirewallNotificationHelper(private val context: Context) {

    companion object {
        private const val SECURITY_EVENTS_CHANNEL_ID = "security_events_channel"
        private const val BLOCKED_CONNECTIONS_CHANNEL_ID = "blocked_connections_channel"
        private const val SECURITY_NOTIFICATION_ID = 10102
        private const val BLOCK_NOTIFICATION_ID = 10103

        // Debounce batch window (collects rapid burst events)
        private const val BATCH_WINDOW_MS = 3000L
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val helperScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Thread-safe batch tracking state
    private val lock = Any()
    private var batchJob: Job? = null
    private var accumulatedBlockCount = 0
    private var lastBlockedPackage = ""
    private val uniqueBlockedPackages = mutableSetOf<String>()

    init {
        createChannels()
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            // 1. Security Events Channel (High Importance for critical lifecycle/errors)
            val securityChannel = NotificationChannel(
                SECURITY_EVENTS_CHANNEL_ID,
                context.getString(R.string.security_events_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.security_events_channel_desc)
                setSound(soundUri, audioAttributes)
                enableVibration(true)
            }

            // 2. Blocked Connections Channel (Low Importance: Silent, no intrusive popups or sounds)
            val blockChannel = NotificationChannel(
                BLOCKED_CONNECTIONS_CHANNEL_ID,
                context.getString(R.string.blocked_connections_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = context.getString(R.string.blocked_connections_channel_desc)
                setSound(null, null)
                enableVibration(false)
                setShowBadge(false)
            }

            notificationManager.createNotificationChannel(securityChannel)
            notificationManager.createNotificationChannel(blockChannel)
        }
    }

    /**
     * Dispatch critical security alert (e.g. firewall starting/stopping error, revoked permission).
     * These are meaningful events that alert the user with sound.
     */
    fun showSecurityEvent(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination", "activity")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, SECURITY_EVENTS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(SECURITY_NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            // Ignore if POST_NOTIFICATIONS permission not granted
        }
    }

    /**
     * Thread-safe batched aggregation for blocked connection events.
     * Prevents notification spam by collecting burst events over a 3-second window
     * and posting a single, silent aggregated notification.
     */
    fun notifyBlockedConnection(event: FirewallEventEntity) {
        synchronized(lock) {
            accumulatedBlockCount++
            lastBlockedPackage = event.packageName
            uniqueBlockedPackages.add(event.packageName)

            // If a batch collection job is already active, it will capture this event.
            if (batchJob?.isActive == true) {
                return
            }

            // Launch a single debouncing coroutine window
            batchJob = helperScope.launch {
                delay(BATCH_WINDOW_MS)
                sendBatchedBlockNotification()
            }
        }
    }

    private fun sendBatchedBlockNotification() {
        val count: Int
        val pkg: String
        val uniqueCount: Int

        synchronized(lock) {
            count = accumulatedBlockCount
            pkg = lastBlockedPackage
            uniqueCount = uniqueBlockedPackages.size

            // Reset batch accumulators
            accumulatedBlockCount = 0
            uniqueBlockedPackages.clear()
            batchJob = null
        }

        if (count <= 0) return

        val appLabel = pkg.substringAfterLast(".").replaceFirstChar { it.uppercase() }
        val title = context.getString(R.string.connection_blocked)
        val message = when {
            count == 1 -> context.getString(R.string.app_blocked_single, appLabel)
            uniqueCount == 1 -> "${context.getString(R.string.apps_blocked_multiple, count)} ($appLabel)"
            else -> context.getString(R.string.apps_blocked_multiple, count)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination", "activity")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Silent, single-alert aggregated notification
        val notification = NotificationCompat.Builder(context, BLOCKED_CONNECTIONS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(BLOCK_NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            // Ignore if POST_NOTIFICATIONS permission not granted
        }
    }
}
