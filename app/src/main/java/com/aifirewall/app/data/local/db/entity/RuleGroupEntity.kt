package com.aifirewall.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "rule_groups")
data class RuleGroupEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val wifiPolicy: String = "UNSET",
    val mobileDataPolicy: String = "UNSET"
)
