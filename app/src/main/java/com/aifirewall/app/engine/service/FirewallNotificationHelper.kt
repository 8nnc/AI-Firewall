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

/**
 * Enterprise Firewall Notification Dispatcher.
 *
 * Dispatches critical security alerts (e.g. firewall starting/stopping errors, critical exceptions).
 * Blocked network connection/packet events intentionally do NOT generate user-facing notifications.
 */
class FirewallNotificationHelper(private val context: Context) {

    companion object {
        private const val SECURITY_EVENTS_CHANNEL_ID = "security_events_channel"
        private const val SECURITY_NOTIFICATION_ID = 10102
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

            // Security Events Channel (High Importance for critical errors/lifecycle alerts)
            val securityChannel = NotificationChannel(
                SECURITY_EVENTS_CHANNEL_ID,
                context.getString(R.string.security_events_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.security_events_channel_desc)
                setSound(soundUri, audioAttributes)
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(securityChannel)
        }
    }

    /**
     * Dispatch critical security alert (e.g. firewall error, service failure).
     * These are meaningful system events that alert the user.
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
     * Blocked connection/packet events intentionally do NOT generate user-facing notifications.
     * All blocked events continue to be logged to the database for statistics, "Blocked Today",
     * and Activity history without spamming the user.
     */
    @Suppress("UNUSED_PARAMETER")
    fun notifyBlockedConnection(event: FirewallEventEntity) {
        // No-op: Block events must never generate user-facing notifications.
    }
}
