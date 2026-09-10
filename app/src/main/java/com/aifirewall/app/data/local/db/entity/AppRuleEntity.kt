package com.aifirewall.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "app_rules",
    foreignKeys = [
        ForeignKey(
            entity = FirewallProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["profileId"]),
        Index(value = ["profileId", "packageName"], unique = true)
    ]
)
data class AppRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: String,
    val packageName: String,
    val uid: Int,
    val wifiPolicy: String,       // NetworkPolicy enum name
    val mobileDataPolicy: String  // NetworkPolicy enum name
)
