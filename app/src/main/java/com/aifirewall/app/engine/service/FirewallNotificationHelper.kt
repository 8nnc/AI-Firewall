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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirewallNotificationHelper(private val context: Context) {

    companion object {
        private const val SECURITY_EVENTS_CHANNEL_ID = "security_events_channel"
        private const val BLOCKED_CONNECTIONS_CHANNEL_ID = "blocked_connections_channel"
        private const val SECURITY_NOTIFICATION_ID = 10102
        private const val BLOCK_NOTIFICATION_ID = 10103
        
        // Throttling state
        private var lastBlockNotificationTime = 0L
        private var pendingBlockCount = 0
        private var pendingBlockedPackage = ""
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

            // Security Events Channel (High Importance with Sound)
            val securityChannel = NotificationChannel(
                SECURITY_EVENTS_CHANNEL_ID,
                context.getString(R.string.security_events_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.security_events_channel_desc)
                setSound(soundUri, audioAttributes)
                enableVibration(true)
            }

            // Blocked Connections Channel (Low/Medium Importance)
            val blockChannel = NotificationChannel(
                BLOCKED_CONNECTIONS_CHANNEL_ID,
                context.getString(R.string.blocked_connections_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.blocked_connections_channel_desc)
            }

            notificationManager.createNotificationChannel(securityChannel)
            notificationManager.createNotificationChannel(blockChannel)
        }
    }

    fun showSecurityEvent(title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination", "activity")
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

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
            // Ignore if POST_NOTIFICATIONS permission is not granted
        }
    }

    private val helperScope = CoroutineScope(kotlinx.coroutines.SupervisorJob() + Dispatchers.Main)

    /**
     * Throttled block notification to prevent spamming the user.
     */
    fun notifyBlockedConnection(event: FirewallEventEntity) {
        synchronized(this) {
            pendingBlockCount++
            pendingBlockedPackage = event.packageName
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBlockNotificationTime > 5000) {
            // It's been more than 5 seconds since the last notification.
            // Wait briefly to batch any rapid subsequent events.
            lastBlockNotificationTime = currentTime
            
            helperScope.launch {
                delay(500)
                sendBatchedBlockNotification()
            }
        }
    }

    private fun sendBatchedBlockNotification() {
        val count: Int
        val pkg: String
        synchronized(this) {
            count = pendingBlockCount
            pkg = pendingBlockedPackage
            pendingBlockCount = 0
        }

        if (count == 0) return

        val title = context.getString(R.string.connection_blocked)
        val message = if (count == 1) {
            context.getString(R.string.app_blocked_single, pkg)
        } else {
            context.getString(R.string.apps_blocked_multiple, count)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("destination", "activity")
        }
        val pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, BLOCKED_CONNECTIONS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(BLOCK_NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            // Ignore if POST_NOTIFICATIONS permission is not granted
        }
    }
}
