package com.aifirewall.app.data.local.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aifirewall.app.data.local.db.entity.GroupMemberEntity;
import com.aifirewall.app.data.local.db.entity.RuleGroupEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RuleGroupDao_Impl implements RuleGroupDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RuleGroupEntity> __insertionAdapterOfRuleGroupEntity;

  private final EntityInsertionAdapter<GroupMemberEntity> __insertionAdapterOfGroupMemberEntity;

  private final EntityDeletionOrUpdateAdapter<RuleGroupEntity> __deletionAdapterOfRuleGroupEntity;

  private final SharedSQLiteStatement __preparedStmtOfRemoveMember;

  public RuleGroupDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRuleGroupEntity = new EntityInsertionAdapter<RuleGroupEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rule_groups` (`id`,`name`,`description`,`wifiPolicy`,`mobileDataPolicy`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RuleGroupEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        if (entity.getWifiPolicy() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getWifiPolicy());
        }
        if (entity.getMobileDataPolicy() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMobileDataPolicy());
        }
      }
    };
    this.__insertionAdapterOfGroupMemberEntity = new EntityInsertionAdapter<GroupMemberEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `group_members` (`id`,`groupId`,`packageName`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupMemberEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getGroupId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getGroupId());
        }
        if (entity.getPackageName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPackageName());
        }
      }
    };
    this.__deletionAdapterOfRuleGroupEntity = new EntityDeletionOrUpdateAdapter<RuleGroupEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rule_groups` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RuleGroupEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__preparedStmtOfRemoveMember = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_members WHERE groupId = ? AND packageName = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertGroup(final RuleGroupEntity group,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRuleGroupEntity.insert(group);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addMember(final GroupMemberEntity member,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupMemberEntity.insert(member);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteGroup(final RuleGroupEntity group,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRuleGroupEntity.handle(group);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addMembers(final List<GroupMemberEntity> members,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> RuleGroupDao.DefaultImpls.addMembers(RuleGroupDao_Impl.this, members, __cont), $completion);
  }

  @Override
  public Object removeMember(final String groupId, final String packageName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveMember.acquire();
        int _argIndex = 1;
        if (groupId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, groupId);
        }
        _argIndex = 2;
        if (packageName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, packageName);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRemoveMember.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RuleGroupEntity>> getAllGroupsFlow() {
    final String _sql = "SELECT * FROM rule_groups";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rule_groups"}, new Callable<List<RuleGroupEntity>>() {
      @Override
      @NonNull
      public List<RuleGroupEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWifiPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiPolicy");
          final int _cursorIndexOfMobileDataPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "mobileDataPolicy");
          final List<RuleGroupEntity> _result = new ArrayList<RuleGroupEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RuleGroupEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpWifiPolicy;
            if (_cursor.isNull(_cursorIndexOfWifiPolicy)) {
              _tmpWifiPolicy = null;
            } else {
              _tmpWifiPolicy = _cursor.getString(_cursorIndexOfWifiPolicy);
            }
            final String _tmpMobileDataPolicy;
            if (_cursor.isNull(_cursorIndexOfMobileDataPolicy)) {
              _tmpMobileDataPolicy = null;
            } else {
              _tmpMobileDataPolicy = _cursor.getString(_cursorIndexOfMobileDataPolicy);
            }
            _item = new RuleGroupEntity(_tmpId,_tmpName,_tmpDescription,_tmpWifiPolicy,_tmpMobileDataPolicy);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<GroupMemberEntity>> getAllGroupMembersFlow() {
    final String _sql = "SELECT * FROM group_members";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_members"}, new Callable<List<GroupMemberEntity>>() {
      @Override
      @NonNull
      public List<GroupMemberEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final List<GroupMemberEntity> _result = new ArrayList<GroupMemberEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupMemberEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpGroupId;
            if (_cursor.isNull(_cursorIndexOfGroupId)) {
              _tmpGroupId = null;
            } else {
              _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            }
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            _item = new GroupMemberEntity(_tmpId,_tmpGroupId,_tmpPackageName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllGroups(final Continuation<? super List<RuleGroupEntity>> $completion) {
    final String _sql = "SELECT * FROM rule_groups";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RuleGroupEntity>>() {
      @Override
      @NonNull
      public List<RuleGroupEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWifiPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiPolicy");
          final int _cursorIndexOfMobileDataPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "mobileDataPolicy");
          final List<RuleGroupEntity> _result = new ArrayList<RuleGroupEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RuleGroupEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpWifiPolicy;
            if (_cursor.isNull(_cursorIndexOfWifiPolicy)) {
              _tmpWifiPolicy = null;
            } else {
              _tmpWifiPolicy = _cursor.getString(_cursorIndexOfWifiPolicy);
            }
            final String _tmpMobileDataPolicy;
            if (_cursor.isNull(_cursorIndexOfMobileDataPolicy)) {
              _tmpMobileDataPolicy = null;
            } else {
              _tmpMobileDataPolicy = _cursor.getString(_cursorIndexOfMobileDataPolicy);
            }
            _item = new RuleGroupEntity(_tmpId,_tmpName,_tmpDescription,_tmpWifiPolicy,_tmpMobileDataPolicy);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<String>> getGroupMembersFlow(final String groupId) {
    final String _sql = "SELECT packageName FROM group_members WHERE groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (groupId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, groupId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_members"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getGroupMembers(final String groupId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT packageName FROM group_members WHERE groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (groupId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, groupId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
