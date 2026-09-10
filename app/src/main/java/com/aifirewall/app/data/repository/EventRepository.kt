package com.aifirewall.app.data.repository

import android.content.Context
import com.aifirewall.app.data.local.db.FirewallDatabase
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class EventRepository(context: Context) {
    private val dao = FirewallDatabase.getDatabase(context).firewallEventDao()

    suspend fun insertEvent(event: FirewallEventEntity): Long {
        return dao.insert(event)
    }
    
    suspend fun updateEvent(event: FirewallEventEntity) {
        dao.update(event)
    }

    suspend fun getRecentSimilarBlockEvent(
        packageName: String, 
        destinationAddress: String, 
        destinationPort: Int, 
        timeWindowMs: Long = 60_000 // 1 minute aggregation window
    ): FirewallEventEntity? {
        val threshold = System.currentTimeMillis() - timeWindowMs
        return dao.getRecentSimilarBlockEvent(packageName, destinationAddress, destinationPort, threshold)
    }

    fun getEventsFlow(): Flow<List<FirewallEventEntity>> {
        return dao.getEventsFlow()
    }
    
    fun getEventsForPackageFlow(packageName: String): Flow<List<FirewallEventEntity>> {
        return dao.getEventsForPackageFlow(packageName)
    }

    fun getBlockedTodayCountFlow(): Flow<Int> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getBlockedTodayCountFlow(calendar.timeInMillis)
    }

    suspend fun clearOldEvents(retentionDays: Int = 30) {
        val threshold = System.currentTimeMillis() - (retentionDays * 24L * 60L * 60L * 1000L)
        dao.deleteOldEvents(threshold)
    }
    
    suspend fun clearAll() {
        dao.clearAll()
    }
}
