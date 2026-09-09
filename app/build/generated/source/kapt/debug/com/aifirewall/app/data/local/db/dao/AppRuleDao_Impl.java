package com.aifirewall.app.data.local.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aifirewall.app.data.local.db.entity.AppRuleEntity;
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
public final class AppRuleDao_Impl implements AppRuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppRuleEntity> __insertionAdapterOfAppRuleEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRule;

  public AppRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppRuleEntity = new EntityInsertionAdapter<AppRuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_rules` (`id`,`profileId`,`packageName`,`uid`,`wifiPolicy`,`mobileDataPolicy`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppRuleEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getProfileId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProfileId());
        }
        if (entity.getPackageName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPackageName());
        }
        statement.bindLong(4, entity.getUid());
        if (entity.getWifiPolicy() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getWifiPolicy());
        }
        if (entity.getMobileDataPolicy() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getMobileDataPolicy());
        }
      }
    };
    this.__preparedStmtOfDeleteRule = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM app_rules WHERE profileId = ? AND packageName = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertRule(final AppRuleEntity rule, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppRuleEntity.insert(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertRules(final List<AppRuleEntity> rules,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppRuleEntity.insert(rules);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRule(final String profileId, final String packageName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRule.acquire();
        int _argIndex = 1;
        if (profileId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, profileId);
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
          __preparedStmtOfDeleteRule.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AppRuleEntity>> getRulesForProfileFlow(final String profileId) {
    final String _sql = "SELECT * FROM app_rules WHERE profileId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (profileId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, profileId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_rules"}, new Callable<List<AppRuleEntity>>() {
      @Override
      @NonNull
      public List<AppRuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfWifiPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiPolicy");
          final int _cursorIndexOfMobileDataPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "mobileDataPolicy");
          final List<AppRuleEntity> _result = new ArrayList<AppRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppRuleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
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
            _item = new AppRuleEntity(_tmpId,_tmpProfileId,_tmpPackageName,_tmpUid,_tmpWifiPolicy,_tmpMobileDataPolicy);
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
  public Object getRulesForProfile(final String profileId,
      final Continuation<? super List<AppRuleEntity>> $completion) {
    final String _sql = "SELECT * FROM app_rules WHERE profileId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (profileId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, profileId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AppRuleEntity>>() {
      @Override
      @NonNull
      public List<AppRuleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfWifiPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiPolicy");
          final int _cursorIndexOfMobileDataPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "mobileDataPolicy");
          final List<AppRuleEntity> _result = new ArrayList<AppRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppRuleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
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
            _item = new AppRuleEntity(_tmpId,_tmpProfileId,_tmpPackageName,_tmpUid,_tmpWifiPolicy,_tmpMobileDataPolicy);
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
  public Object getRule(final String profileId, final String packageName,
      final Continuation<? super AppRuleEntity> $completion) {
    final String _sql = "SELECT * FROM app_rules WHERE profileId = ? AND packageName = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (profileId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, profileId);
    }
    _argIndex = 2;
    if (packageName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, packageName);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AppRuleEntity>() {
      @Override
      @Nullable
      public AppRuleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfWifiPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiPolicy");
          final int _cursorIndexOfMobileDataPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "mobileDataPolicy");
          final AppRuleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
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
            _result = new AppRuleEntity(_tmpId,_tmpProfileId,_tmpPackageName,_tmpUid,_tmpWifiPolicy,_tmpMobileDataPolicy);
          } else {
            _result = null;
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
  public Flow<AppRuleEntity> getRuleFlow(final String profileId, final String packageName) {
    final String _sql = "SELECT * FROM app_rules WHERE profileId = ? AND packageName = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (profileId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, profileId);
    }
    _argIndex = 2;
    if (packageName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, packageName);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_rules"}, new Callable<AppRuleEntity>() {
      @Override
      @Nullable
      public AppRuleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfWifiPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiPolicy");
          final int _cursorIndexOfMobileDataPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "mobileDataPolicy");
          final AppRuleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
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
            _result = new AppRuleEntity(_tmpId,_tmpProfileId,_tmpPackageName,_tmpUid,_tmpWifiPolicy,_tmpMobileDataPolicy);
          } else {
            _result = null;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
