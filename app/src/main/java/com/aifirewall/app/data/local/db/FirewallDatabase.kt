package com.aifirewall.app.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aifirewall.app.data.local.db.dao.AppRuleDao
import com.aifirewall.app.data.local.db.dao.FirewallEventDao
import com.aifirewall.app.data.local.db.dao.FirewallProfileDao
import com.aifirewall.app.data.local.db.dao.RuleGroupDao
import com.aifirewall.app.data.local.db.entity.AppRuleEntity
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.data.local.db.entity.FirewallProfileEntity
import com.aifirewall.app.data.local.db.entity.GroupMemberEntity
import com.aifirewall.app.data.local.db.entity.RuleGroupEntity

@Database(
    entities = [
        FirewallEventEntity::class,
        FirewallProfileEntity::class,
        AppRuleEntity::class,
        RuleGroupEntity::class,
        GroupMemberEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class FirewallDatabase : RoomDatabase() {

    abstract fun firewallEventDao(): FirewallEventDao
    abstract fun firewallProfileDao(): FirewallProfileDao
    abstract fun appRuleDao(): AppRuleDao
    abstract fun ruleGroupDao(): RuleGroupDao

    companion object {
        @Volatile
        private var INSTANCE: FirewallDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `firewall_profiles` (
                        `id` TEXT NOT NULL, 
                        `name` TEXT NOT NULL, 
                        `description` TEXT NOT NULL, 
                        `isActive` INTEGER NOT NULL, 
                        `createdAt` INTEGER NOT NULL, 
                        `updatedAt` INTEGER NOT NULL, 
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent())

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `app_rules` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `profileId` TEXT NOT NULL, 
                        `packageName` TEXT NOT NULL, 
                        `uid` INTEGER NOT NULL, 
                        `wifiPolicy` TEXT NOT NULL, 
                        `mobileDataPolicy` TEXT NOT NULL, 
                        FOREIGN KEY(`profileId`) REFERENCES `firewall_profiles`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """.trimIndent())
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_app_rules_profileId` ON `app_rules` (`profileId`)")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_app_rules_profileId_packageName` ON `app_rules` (`profileId`, `packageName`)")

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `rule_groups` (
                        `id` TEXT NOT NULL, 
                        `name` TEXT NOT NULL, 
                        `description` TEXT NOT NULL, 
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent())

                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `group_members` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `groupId` TEXT NOT NULL, 
                        `packageName` TEXT NOT NULL, 
                        FOREIGN KEY(`groupId`) REFERENCES `rule_groups`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """.trimIndent())
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_group_members_groupId` ON `group_members` (`groupId`)")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_group_members_groupId_packageName` ON `group_members` (`groupId`, `packageName`)")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `rule_groups` ADD COLUMN `wifiPolicy` TEXT NOT NULL DEFAULT 'UNSET'")
                database.execSQL("ALTER TABLE `rule_groups` ADD COLUMN `mobileDataPolicy` TEXT NOT NULL DEFAULT 'UNSET'")
            }
        }

        fun getDatabase(context: Context): FirewallDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FirewallDatabase::class.java,
                    "firewall_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
