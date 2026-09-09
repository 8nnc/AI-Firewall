package com.aifirewall.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FirewallEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: FirewallEventEntity): Long

    @Update
    suspend fun update(event: FirewallEventEntity)

    // For Flow aggregation: Find the most recent block event for the same app/IP/Port
    @Query("""
        SELECT * FROM firewall_events 
        WHERE packageName = :packageName 
        AND destinationAddress = :destinationAddress 
        AND destinationPort = :destinationPort
        AND action = 'BLOCK'
        AND timestamp >= :timeThreshold
        ORDER BY timestamp DESC 
        LIMIT 1
    """)
    suspend fun getRecentSimilarBlockEvent(
        packageName: String, 
        destinationAddress: String, 
        destinationPort: Int, 
        timeThreshold: Long
    ): FirewallEventEntity?

    @Query("SELECT * FROM firewall_events ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getEventsPaginated(limit: Int, offset: Int): List<FirewallEventEntity>

    // Observable flow for the UI, limits to 500 to avoid memory issues if no pagination is implemented initially
    @Query("SELECT * FROM firewall_events ORDER BY timestamp DESC LIMIT 500")
    fun getEventsFlow(): Flow<List<FirewallEventEntity>>
    
    @Query("SELECT * FROM firewall_events WHERE packageName = :packageName ORDER BY timestamp DESC LIMIT 500")
    fun getEventsForPackageFlow(packageName: String): Flow<List<FirewallEventEntity>>

    @Query("DELETE FROM firewall_events WHERE timestamp < :timestampThreshold")
    suspend fun deleteOldEvents(timestampThreshold: Long)
    
    @Query("SELECT COUNT(*) FROM firewall_events WHERE action = 'BLOCK' AND timestamp >= :startOfDay")
    fun getBlockedTodayCountFlow(startOfDay: Long): Flow<Int>
    
    @Query("DELETE FROM firewall_events")
    suspend fun clearAll()
}
