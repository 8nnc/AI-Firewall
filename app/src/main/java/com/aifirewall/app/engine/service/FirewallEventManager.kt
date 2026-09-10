package com.aifirewall.app.engine.service

import android.content.Context
import android.util.Log
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.data.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object FirewallEventManager {
    private const val TAG = "FirewallEventManager"
    
    // Non-blocking queue dropping oldest if burst occurs, protecting memory
    private val eventChannel = Channel<FirewallEventEntity>(
        capacity = 1000,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isInitialized = false
    private var notificationHelper: FirewallNotificationHelper? = null

    fun init(context: Context) {
        if (isInitialized) return
        isInitialized = true
        
        val repository = EventRepository(context)
        notificationHelper = FirewallNotificationHelper(context)

        scope.launch {
            try {
                for (event in eventChannel) {
                    processEvent(repository, event)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Event processing loop interrupted", e)
            }
        }
    }

    /**
     * Dispatch an event from the fast-path VPN loop to the background processing queue.
     */
    fun dispatchEvent(event: FirewallEventEntity) {
        if (!isInitialized) return
        // trySend won't block the caller. If the channel is full, it drops according to BufferOverflow strategy.
        eventChannel.trySend(event)
    }

    private suspend fun processEvent(repository: EventRepository, event: FirewallEventEntity) {
        try {
            if (event.action == "BLOCK") {
                // Flow Aggregation: Check if we recently blocked this same flow
                val recentEvent = repository.getRecentSimilarBlockEvent(
                    packageName = event.packageName,
                    destinationAddress = event.destinationAddress,
                    destinationPort = event.destinationPort,
                    timeWindowMs = 60_000 // Aggregate blocks within 1 minute
                )

                if (recentEvent != null) {
                    // Update existing event instead of creating a new row
                    val updatedEvent = recentEvent.copy(
                        attempts = recentEvent.attempts + 1,
                        lastSeenTimestamp = event.timestamp,
                        bytes = recentEvent.bytes + event.bytes
                    )
                    repository.updateEvent(updatedEvent)
                } else {
                    // Insert new flow
                    repository.insertEvent(event)
                }
            } else {
                // Non-blocking events (like FIREWALL_STARTED) just insert directly
                repository.insertEvent(event)
                if (event.eventType == "FIREWALL_ERROR") {
                    notificationHelper?.showSecurityEvent("Firewall Error", event.reason)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to persist event to database", e)
        }
    }
}
