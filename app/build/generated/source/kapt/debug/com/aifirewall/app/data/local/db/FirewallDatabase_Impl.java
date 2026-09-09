package com.aifirewall.app.data.local.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.aifirewall.app.data.local.db.dao.AppRuleDao;
import com.aifirewall.app.data.local.db.dao.AppRuleDao_Impl;
import com.aifirewall.app.data.local.db.dao.FirewallEventDao;
import com.aifirewall.app.data.local.db.dao.FirewallEventDao_Impl;
import com.aifirewall.app.data.local.db.dao.FirewallProfileDao;
import com.aifirewall.app.data.local.db.dao.FirewallProfileDao_Impl;
import com.aifirewall.app.data.local.db.dao.RuleGroupDao;
import com.aifirewall.app.data.local.db.dao.RuleGroupDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FirewallDatabase_Impl extends FirewallDatabase {
  private volatile FirewallEventDao _firewallEventDao;

  private volatile FirewallProfileDao _firewallProfileDao;

  private volatile AppRuleDao _appRuleDao;

  private volatile RuleGroupDao _ruleGroupDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `firewall_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `packageName` TEXT NOT NULL, `uid` INTEGER NOT NULL, `eventType` TEXT NOT NULL, `action` TEXT NOT NULL, `protocol` TEXT NOT NULL, `transport` TEXT NOT NULL, `sourceAddress` TEXT NOT NULL, `sourcePort` INTEGER NOT NULL, `destinationAddress` TEXT NOT NULL, `destinationPort` INTEGER NOT NULL, `bytes` INTEGER NOT NULL, `reason` TEXT NOT NULL, `direction` TEXT NOT NULL, `attempts` INTEGER NOT NULL, `lastSeenTimestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_firewall_events_timestamp` ON `firewall_events` (`timestamp`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_firewall_events_packageName` ON `firewall_events` (`packageName`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_firewall_events_eventType` ON `firewall_events` (`eventType`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_firewall_events_action` ON `firewall_events` (`action`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `firewall_profiles` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_rules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `profileId` TEXT NOT NULL, `packageName` TEXT NOT NULL, `uid` INTEGER NOT NULL, `wifiPolicy` TEXT NOT NULL, `mobileDataPolicy` TEXT NOT NULL, FOREIGN KEY(`profileId`) REFERENCES `firewall_profiles`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_app_rules_profileId` ON `app_rules` (`profileId`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_app_rules_profileId_packageName` ON `app_rules` (`profileId`, `packageName`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rule_groups` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `wifiPolicy` TEXT NOT NULL, `mobileDataPolicy` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `group_members` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` TEXT NOT NULL, `packageName` TEXT NOT NULL, FOREIGN KEY(`groupId`) REFERENCES `rule_groups`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_group_members_groupId` ON `group_members` (`groupId`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_group_members_groupId_packageName` ON `group_members` (`groupId`, `packageName`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bd503b599c949175c56aefc7cf21104a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `firewall_events`");
        db.execSQL("DROP TABLE IF EXISTS `firewall_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `app_rules`");
        db.execSQL("DROP TABLE IF EXISTS `rule_groups`");
        db.execSQL("DROP TABLE IF EXISTS `group_members`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFirewallEvents = new HashMap<String, TableInfo.Column>(17);
        _columnsFirewallEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("uid", new TableInfo.Column("uid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("eventType", new TableInfo.Column("eventType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("action", new TableInfo.Column("action", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("protocol", new TableInfo.Column("protocol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("transport", new TableInfo.Column("transport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("sourceAddress", new TableInfo.Column("sourceAddress", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("sourcePort", new TableInfo.Column("sourcePort", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("destinationAddress", new TableInfo.Column("destinationAddress", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("destinationPort", new TableInfo.Column("destinationPort", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("bytes", new TableInfo.Column("bytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("reason", new TableInfo.Column("reason", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("direction", new TableInfo.Column("direction", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("attempts", new TableInfo.Column("attempts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallEvents.put("lastSeenTimestamp", new TableInfo.Column("lastSeenTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFirewallEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFirewallEvents = new HashSet<TableInfo.Index>(4);
        _indicesFirewallEvents.add(new TableInfo.Index("index_firewall_events_timestamp", false, Arrays.asList("timestamp"), Arrays.asList("ASC")));
        _indicesFirewallEvents.add(new TableInfo.Index("index_firewall_events_packageName", false, Arrays.asList("packageName"), Arrays.asList("ASC")));
        _indicesFirewallEvents.add(new TableInfo.Index("index_firewall_events_eventType", false, Arrays.asList("eventType"), Arrays.asList("ASC")));
        _indicesFirewallEvents.add(new TableInfo.Index("index_firewall_events_action", false, Arrays.asList("action"), Arrays.asList("ASC")));
        final TableInfo _infoFirewallEvents = new TableInfo("firewall_events", _columnsFirewallEvents, _foreignKeysFirewallEvents, _indicesFirewallEvents);
        final TableInfo _existingFirewallEvents = TableInfo.read(db, "firewall_events");
        if (!_infoFirewallEvents.equals(_existingFirewallEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "firewall_events(com.aifirewall.app.data.local.db.entity.FirewallEventEntity).\n"
                  + " Expected:\n" + _infoFirewallEvents + "\n"
                  + " Found:\n" + _existingFirewallEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsFirewallProfiles = new HashMap<String, TableInfo.Column>(6);
        _columnsFirewallProfiles.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallProfiles.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallProfiles.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallProfiles.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallProfiles.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFirewallProfiles.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFirewallProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFirewallProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFirewallProfiles = new TableInfo("firewall_profiles", _columnsFirewallProfiles, _foreignKeysFirewallProfiles, _indicesFirewallProfiles);
        final TableInfo _existingFirewallProfiles = TableInfo.read(db, "firewall_profiles");
        if (!_infoFirewallProfiles.equals(_existingFirewallProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "firewall_profiles(com.aifirewall.app.data.local.db.entity.FirewallProfileEntity).\n"
                  + " Expected:\n" + _infoFirewallProfiles + "\n"
                  + " Found:\n" + _existingFirewallProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsAppRules = new HashMap<String, TableInfo.Column>(6);
        _columnsAppRules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppRules.put("profileId", new TableInfo.Column("profileId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppRules.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppRules.put("uid", new TableInfo.Column("uid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppRules.put("wifiPolicy", new TableInfo.Column("wifiPolicy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppRules.put("mobileDataPolicy", new TableInfo.Column("mobileDataPolicy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppRules = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysAppRules.add(new TableInfo.ForeignKey("firewall_profiles", "CASCADE", "NO ACTION", Arrays.asList("profileId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAppRules = new HashSet<TableInfo.Index>(2);
        _indicesAppRules.add(new TableInfo.Index("index_app_rules_profileId", false, Arrays.asList("profileId"), Arrays.asList("ASC")));
        _indicesAppRules.add(new TableInfo.Index("index_app_rules_profileId_packageName", true, Arrays.asList("profileId", "packageName"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoAppRules = new TableInfo("app_rules", _columnsAppRules, _foreignKeysAppRules, _indicesAppRules);
        final TableInfo _existingAppRules = TableInfo.read(db, "app_rules");
        if (!_infoAppRules.equals(_existingAppRules)) {
          return new RoomOpenHelper.ValidationResult(false, "app_rules(com.aifirewall.app.data.local.db.entity.AppRuleEntity).\n"
                  + " Expected:\n" + _infoAppRules + "\n"
                  + " Found:\n" + _existingAppRules);
        }
        final HashMap<String, TableInfo.Column> _columnsRuleGroups = new HashMap<String, TableInfo.Column>(5);
        _columnsRuleGroups.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRuleGroups.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRuleGroups.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRuleGroups.put("wifiPolicy", new TableInfo.Column("wifiPolicy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRuleGroups.put("mobileDataPolicy", new TableInfo.Column("mobileDataPolicy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRuleGroups = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRuleGroups = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRuleGroups = new TableInfo("rule_groups", _columnsRuleGroups, _foreignKeysRuleGroups, _indicesRuleGroups);
        final TableInfo _existingRuleGroups = TableInfo.read(db, "rule_groups");
        if (!_infoRuleGroups.equals(_existingRuleGroups)) {
          return new RoomOpenHelper.ValidationResult(false, "rule_groups(com.aifirewall.app.data.local.db.entity.RuleGroupEntity).\n"
                  + " Expected:\n" + _infoRuleGroups + "\n"
                  + " Found:\n" + _existingRuleGroups);
        }
        final HashMap<String, TableInfo.Column> _columnsGroupMembers = new HashMap<String, TableInfo.Column>(3);
        _columnsGroupMembers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("groupId", new TableInfo.Column("groupId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGroupMembers.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGroupMembers = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysGroupMembers.add(new TableInfo.ForeignKey("rule_groups", "CASCADE", "NO ACTION", Arrays.asList("groupId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesGroupMembers = new HashSet<TableInfo.Index>(2);
        _indicesGroupMembers.add(new TableInfo.Index("index_group_members_groupId", false, Arrays.asList("groupId"), Arrays.asList("ASC")));
        _indicesGroupMembers.add(new TableInfo.Index("index_group_members_groupId_packageName", true, Arrays.asList("groupId", "packageName"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoGroupMembers = new TableInfo("group_members", _columnsGroupMembers, _foreignKeysGroupMembers, _indicesGroupMembers);
        final TableInfo _existingGroupMembers = TableInfo.read(db, "group_members");
        if (!_infoGroupMembers.equals(_existingGroupMembers)) {
          return new RoomOpenHelper.ValidationResult(false, "group_members(com.aifirewall.app.data.local.db.entity.GroupMemberEntity).\n"
                  + " Expected:\n" + _infoGroupMembers + "\n"
                  + " Found:\n" + _existingGroupMembers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bd503b599c949175c56aefc7cf21104a", "faa95f15e7c48409b2c96e0062720434");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "firewall_events","firewall_profiles","app_rules","rule_groups","group_members");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `firewall_events`");
      _db.execSQL("DELETE FROM `firewall_profiles`");
      _db.execSQL("DELETE FROM `app_rules`");
      _db.execSQL("DELETE FROM `rule_groups`");
      _db.execSQL("DELETE FROM `group_members`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FirewallEventDao.class, FirewallEventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FirewallProfileDao.class, FirewallProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AppRuleDao.class, AppRuleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RuleGroupDao.class, RuleGroupDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FirewallEventDao firewallEventDao() {
    if (_firewallEventDao != null) {
      return _firewallEventDao;
    } else {
      synchronized(this) {
        if(_firewallEventDao == null) {
          _firewallEventDao = new FirewallEventDao_Impl(this);
        }
        return _firewallEventDao;
      }
    }
  }

  @Override
  public FirewallProfileDao firewallProfileDao() {
    if (_firewallProfileDao != null) {
      return _firewallProfileDao;
    } else {
      synchronized(this) {
        if(_firewallProfileDao == null) {
          _firewallProfileDao = new FirewallProfileDao_Impl(this);
        }
        return _firewallProfileDao;
      }
    }
  }

  @Override
  public AppRuleDao appRuleDao() {
    if (_appRuleDao != null) {
      return _appRuleDao;
    } else {
      synchronized(this) {
        if(_appRuleDao == null) {
          _appRuleDao = new AppRuleDao_Impl(this);
        }
        return _appRuleDao;
      }
    }
  }

  @Override
  public RuleGroupDao ruleGroupDao() {
    if (_ruleGroupDao != null) {
      return _ruleGroupDao;
    } else {
      synchronized(this) {
        if(_ruleGroupDao == null) {
          _ruleGroupDao = new RuleGroupDao_Impl(this);
        }
        return _ruleGroupDao;
      }
    }
  }
}
