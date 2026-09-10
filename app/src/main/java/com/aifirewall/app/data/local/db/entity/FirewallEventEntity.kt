package com.aifirewall.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "firewall_events",
    indices = [
        Index(value = ["timestamp"]),
        Index(value = ["packageName"]),
        Index(value = ["eventType"]),
        Index(value = ["action"])
    ]
)
data class FirewallEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val packageName: String,
    val uid: Int,
    val eventType: String, // FIREWALL_STARTED, NETWORK_CHANGED, CONNECTION_BLOCKED
    val action: String, // ALLOW, BLOCK
    val protocol: String, // TCP, UDP, ICMP, OTHER
    val transport: String, // WIFI, CELLULAR, UNKNOWN
    val sourceAddress: String,
    val sourcePort: Int,
    val destinationAddress: String,
    val destinationPort: Int,
    val bytes: Long,
    val reason: String,
    val direction: String, // OUTBOUND
    
    // Aggregation specific fields
    val attempts: Int = 1,
    val lastSeenTimestamp: Long = timestamp
)
