package com.aifirewall.app.data.local.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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
public final class FirewallEventDao_Impl implements FirewallEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FirewallEventEntity> __insertionAdapterOfFirewallEventEntity;

  private final EntityDeletionOrUpdateAdapter<FirewallEventEntity> __updateAdapterOfFirewallEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldEvents;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public FirewallEventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFirewallEventEntity = new EntityInsertionAdapter<FirewallEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `firewall_events` (`id`,`timestamp`,`packageName`,`uid`,`eventType`,`action`,`protocol`,`transport`,`sourceAddress`,`sourcePort`,`destinationAddress`,`destinationPort`,`bytes`,`reason`,`direction`,`attempts`,`lastSeenTimestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FirewallEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        if (entity.getPackageName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPackageName());
        }
        statement.bindLong(4, entity.getUid());
        if (entity.getEventType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEventType());
        }
        if (entity.getAction() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAction());
        }
        if (entity.getProtocol() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getProtocol());
        }
        if (entity.getTransport() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getTransport());
        }
        if (entity.getSourceAddress() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSourceAddress());
        }
        statement.bindLong(10, entity.getSourcePort());
        if (entity.getDestinationAddress() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDestinationAddress());
        }
        statement.bindLong(12, entity.getDestinationPort());
        statement.bindLong(13, entity.getBytes());
        if (entity.getReason() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getReason());
        }
        if (entity.getDirection() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getDirection());
        }
        statement.bindLong(16, entity.getAttempts());
        statement.bindLong(17, entity.getLastSeenTimestamp());
      }
    };
    this.__updateAdapterOfFirewallEventEntity = new EntityDeletionOrUpdateAdapter<FirewallEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `firewall_events` SET `id` = ?,`timestamp` = ?,`packageName` = ?,`uid` = ?,`eventType` = ?,`action` = ?,`protocol` = ?,`transport` = ?,`sourceAddress` = ?,`sourcePort` = ?,`destinationAddress` = ?,`destinationPort` = ?,`bytes` = ?,`reason` = ?,`direction` = ?,`attempts` = ?,`lastSeenTimestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FirewallEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        if (entity.getPackageName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPackageName());
        }
        statement.bindLong(4, entity.getUid());
        if (entity.getEventType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEventType());
        }
        if (entity.getAction() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAction());
        }
        if (entity.getProtocol() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getProtocol());
        }
        if (entity.getTransport() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getTransport());
        }
        if (entity.getSourceAddress() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSourceAddress());
        }
        statement.bindLong(10, entity.getSourcePort());
        if (entity.getDestinationAddress() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getDestinationAddress());
        }
        statement.bindLong(12, entity.getDestinationPort());
        statement.bindLong(13, entity.getBytes());
        if (entity.getReason() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getReason());
        }
        if (entity.getDirection() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getDirection());
        }
        statement.bindLong(16, entity.getAttempts());
        statement.bindLong(17, entity.getLastSeenTimestamp());
        statement.bindLong(18, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteOldEvents = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM firewall_events WHERE timestamp < ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM firewall_events";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FirewallEventEntity event,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFirewallEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final FirewallEventEntity event,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFirewallEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldEvents(final long timestampThreshold,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldEvents.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestampThreshold);
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
          __preparedStmtOfDeleteOldEvents.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
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
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecentSimilarBlockEvent(final String packageName,
      final String destinationAddress, final int destinationPort, final long timeThreshold,
      final Continuation<? super FirewallEventEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM firewall_events \n"
            + "        WHERE packageName = ? \n"
            + "        AND destinationAddress = ? \n"
            + "        AND destinationPort = ?\n"
            + "        AND action = 'BLOCK'\n"
            + "        AND timestamp >= ?\n"
            + "        ORDER BY timestamp DESC \n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (packageName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, packageName);
    }
    _argIndex = 2;
    if (destinationAddress == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, destinationAddress);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, destinationPort);
    _argIndex = 4;
    _statement.bindLong(_argIndex, timeThreshold);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FirewallEventEntity>() {
      @Override
      @Nullable
      public FirewallEventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfSourceAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAddress");
          final int _cursorIndexOfSourcePort = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePort");
          final int _cursorIndexOfDestinationAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAddress");
          final int _cursorIndexOfDestinationPort = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationPort");
          final int _cursorIndexOfBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "bytes");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final int _cursorIndexOfLastSeenTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenTimestamp");
          final FirewallEventEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpAction;
            if (_cursor.isNull(_cursorIndexOfAction)) {
              _tmpAction = null;
            } else {
              _tmpAction = _cursor.getString(_cursorIndexOfAction);
            }
            final String _tmpProtocol;
            if (_cursor.isNull(_cursorIndexOfProtocol)) {
              _tmpProtocol = null;
            } else {
              _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            }
            final String _tmpTransport;
            if (_cursor.isNull(_cursorIndexOfTransport)) {
              _tmpTransport = null;
            } else {
              _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            }
            final String _tmpSourceAddress;
            if (_cursor.isNull(_cursorIndexOfSourceAddress)) {
              _tmpSourceAddress = null;
            } else {
              _tmpSourceAddress = _cursor.getString(_cursorIndexOfSourceAddress);
            }
            final int _tmpSourcePort;
            _tmpSourcePort = _cursor.getInt(_cursorIndexOfSourcePort);
            final String _tmpDestinationAddress;
            if (_cursor.isNull(_cursorIndexOfDestinationAddress)) {
              _tmpDestinationAddress = null;
            } else {
              _tmpDestinationAddress = _cursor.getString(_cursorIndexOfDestinationAddress);
            }
            final int _tmpDestinationPort;
            _tmpDestinationPort = _cursor.getInt(_cursorIndexOfDestinationPort);
            final long _tmpBytes;
            _tmpBytes = _cursor.getLong(_cursorIndexOfBytes);
            final String _tmpReason;
            if (_cursor.isNull(_cursorIndexOfReason)) {
              _tmpReason = null;
            } else {
              _tmpReason = _cursor.getString(_cursorIndexOfReason);
            }
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            final long _tmpLastSeenTimestamp;
            _tmpLastSeenTimestamp = _cursor.getLong(_cursorIndexOfLastSeenTimestamp);
            _result = new FirewallEventEntity(_tmpId,_tmpTimestamp,_tmpPackageName,_tmpUid,_tmpEventType,_tmpAction,_tmpProtocol,_tmpTransport,_tmpSourceAddress,_tmpSourcePort,_tmpDestinationAddress,_tmpDestinationPort,_tmpBytes,_tmpReason,_tmpDirection,_tmpAttempts,_tmpLastSeenTimestamp);
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
  public Object getEventsPaginated(final int limit, final int offset,
      final Continuation<? super List<FirewallEventEntity>> $completion) {
    final String _sql = "SELECT * FROM firewall_events ORDER BY timestamp DESC LIMIT ? OFFSET ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 2;
    _statement.bindLong(_argIndex, offset);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<FirewallEventEntity>>() {
      @Override
      @NonNull
      public List<FirewallEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfSourceAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAddress");
          final int _cursorIndexOfSourcePort = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePort");
          final int _cursorIndexOfDestinationAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAddress");
          final int _cursorIndexOfDestinationPort = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationPort");
          final int _cursorIndexOfBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "bytes");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final int _cursorIndexOfLastSeenTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenTimestamp");
          final List<FirewallEventEntity> _result = new ArrayList<FirewallEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FirewallEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpAction;
            if (_cursor.isNull(_cursorIndexOfAction)) {
              _tmpAction = null;
            } else {
              _tmpAction = _cursor.getString(_cursorIndexOfAction);
            }
            final String _tmpProtocol;
            if (_cursor.isNull(_cursorIndexOfProtocol)) {
              _tmpProtocol = null;
            } else {
              _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            }
            final String _tmpTransport;
            if (_cursor.isNull(_cursorIndexOfTransport)) {
              _tmpTransport = null;
            } else {
              _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            }
            final String _tmpSourceAddress;
            if (_cursor.isNull(_cursorIndexOfSourceAddress)) {
              _tmpSourceAddress = null;
            } else {
              _tmpSourceAddress = _cursor.getString(_cursorIndexOfSourceAddress);
            }
            final int _tmpSourcePort;
            _tmpSourcePort = _cursor.getInt(_cursorIndexOfSourcePort);
            final String _tmpDestinationAddress;
            if (_cursor.isNull(_cursorIndexOfDestinationAddress)) {
              _tmpDestinationAddress = null;
            } else {
              _tmpDestinationAddress = _cursor.getString(_cursorIndexOfDestinationAddress);
            }
            final int _tmpDestinationPort;
            _tmpDestinationPort = _cursor.getInt(_cursorIndexOfDestinationPort);
            final long _tmpBytes;
            _tmpBytes = _cursor.getLong(_cursorIndexOfBytes);
            final String _tmpReason;
            if (_cursor.isNull(_cursorIndexOfReason)) {
              _tmpReason = null;
            } else {
              _tmpReason = _cursor.getString(_cursorIndexOfReason);
            }
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            final long _tmpLastSeenTimestamp;
            _tmpLastSeenTimestamp = _cursor.getLong(_cursorIndexOfLastSeenTimestamp);
            _item = new FirewallEventEntity(_tmpId,_tmpTimestamp,_tmpPackageName,_tmpUid,_tmpEventType,_tmpAction,_tmpProtocol,_tmpTransport,_tmpSourceAddress,_tmpSourcePort,_tmpDestinationAddress,_tmpDestinationPort,_tmpBytes,_tmpReason,_tmpDirection,_tmpAttempts,_tmpLastSeenTimestamp);
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
  public Flow<List<FirewallEventEntity>> getEventsFlow() {
    final String _sql = "SELECT * FROM firewall_events ORDER BY timestamp DESC LIMIT 500";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"firewall_events"}, new Callable<List<FirewallEventEntity>>() {
      @Override
      @NonNull
      public List<FirewallEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfSourceAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAddress");
          final int _cursorIndexOfSourcePort = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePort");
          final int _cursorIndexOfDestinationAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAddress");
          final int _cursorIndexOfDestinationPort = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationPort");
          final int _cursorIndexOfBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "bytes");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final int _cursorIndexOfLastSeenTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenTimestamp");
          final List<FirewallEventEntity> _result = new ArrayList<FirewallEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FirewallEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpAction;
            if (_cursor.isNull(_cursorIndexOfAction)) {
              _tmpAction = null;
            } else {
              _tmpAction = _cursor.getString(_cursorIndexOfAction);
            }
            final String _tmpProtocol;
            if (_cursor.isNull(_cursorIndexOfProtocol)) {
              _tmpProtocol = null;
            } else {
              _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            }
            final String _tmpTransport;
            if (_cursor.isNull(_cursorIndexOfTransport)) {
              _tmpTransport = null;
            } else {
              _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            }
            final String _tmpSourceAddress;
            if (_cursor.isNull(_cursorIndexOfSourceAddress)) {
              _tmpSourceAddress = null;
            } else {
              _tmpSourceAddress = _cursor.getString(_cursorIndexOfSourceAddress);
            }
            final int _tmpSourcePort;
            _tmpSourcePort = _cursor.getInt(_cursorIndexOfSourcePort);
            final String _tmpDestinationAddress;
            if (_cursor.isNull(_cursorIndexOfDestinationAddress)) {
              _tmpDestinationAddress = null;
            } else {
              _tmpDestinationAddress = _cursor.getString(_cursorIndexOfDestinationAddress);
            }
            final int _tmpDestinationPort;
            _tmpDestinationPort = _cursor.getInt(_cursorIndexOfDestinationPort);
            final long _tmpBytes;
            _tmpBytes = _cursor.getLong(_cursorIndexOfBytes);
            final String _tmpReason;
            if (_cursor.isNull(_cursorIndexOfReason)) {
              _tmpReason = null;
            } else {
              _tmpReason = _cursor.getString(_cursorIndexOfReason);
            }
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            final long _tmpLastSeenTimestamp;
            _tmpLastSeenTimestamp = _cursor.getLong(_cursorIndexOfLastSeenTimestamp);
            _item = new FirewallEventEntity(_tmpId,_tmpTimestamp,_tmpPackageName,_tmpUid,_tmpEventType,_tmpAction,_tmpProtocol,_tmpTransport,_tmpSourceAddress,_tmpSourcePort,_tmpDestinationAddress,_tmpDestinationPort,_tmpBytes,_tmpReason,_tmpDirection,_tmpAttempts,_tmpLastSeenTimestamp);
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
  public Flow<List<FirewallEventEntity>> getEventsForPackageFlow(final String packageName) {
    final String _sql = "SELECT * FROM firewall_events WHERE packageName = ? ORDER BY timestamp DESC LIMIT 500";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (packageName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, packageName);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"firewall_events"}, new Callable<List<FirewallEventEntity>>() {
      @Override
      @NonNull
      public List<FirewallEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfProtocol = CursorUtil.getColumnIndexOrThrow(_cursor, "protocol");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfSourceAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceAddress");
          final int _cursorIndexOfSourcePort = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePort");
          final int _cursorIndexOfDestinationAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationAddress");
          final int _cursorIndexOfDestinationPort = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationPort");
          final int _cursorIndexOfBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "bytes");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final int _cursorIndexOfLastSeenTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeenTimestamp");
          final List<FirewallEventEntity> _result = new ArrayList<FirewallEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FirewallEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final int _tmpUid;
            _tmpUid = _cursor.getInt(_cursorIndexOfUid);
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpAction;
            if (_cursor.isNull(_cursorIndexOfAction)) {
              _tmpAction = null;
            } else {
              _tmpAction = _cursor.getString(_cursorIndexOfAction);
            }
            final String _tmpProtocol;
            if (_cursor.isNull(_cursorIndexOfProtocol)) {
              _tmpProtocol = null;
            } else {
              _tmpProtocol = _cursor.getString(_cursorIndexOfProtocol);
            }
            final String _tmpTransport;
            if (_cursor.isNull(_cursorIndexOfTransport)) {
              _tmpTransport = null;
            } else {
              _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            }
            final String _tmpSourceAddress;
            if (_cursor.isNull(_cursorIndexOfSourceAddress)) {
              _tmpSourceAddress = null;
            } else {
              _tmpSourceAddress = _cursor.getString(_cursorIndexOfSourceAddress);
            }
            final int _tmpSourcePort;
            _tmpSourcePort = _cursor.getInt(_cursorIndexOfSourcePort);
            final String _tmpDestinationAddress;
            if (_cursor.isNull(_cursorIndexOfDestinationAddress)) {
              _tmpDestinationAddress = null;
            } else {
              _tmpDestinationAddress = _cursor.getString(_cursorIndexOfDestinationAddress);
            }
            final int _tmpDestinationPort;
            _tmpDestinationPort = _cursor.getInt(_cursorIndexOfDestinationPort);
            final long _tmpBytes;
            _tmpBytes = _cursor.getLong(_cursorIndexOfBytes);
            final String _tmpReason;
            if (_cursor.isNull(_cursorIndexOfReason)) {
              _tmpReason = null;
            } else {
              _tmpReason = _cursor.getString(_cursorIndexOfReason);
            }
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            final long _tmpLastSeenTimestamp;
            _tmpLastSeenTimestamp = _cursor.getLong(_cursorIndexOfLastSeenTimestamp);
            _item = new FirewallEventEntity(_tmpId,_tmpTimestamp,_tmpPackageName,_tmpUid,_tmpEventType,_tmpAction,_tmpProtocol,_tmpTransport,_tmpSourceAddress,_tmpSourcePort,_tmpDestinationAddress,_tmpDestinationPort,_tmpBytes,_tmpReason,_tmpDirection,_tmpAttempts,_tmpLastSeenTimestamp);
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
  public Flow<Integer> getBlockedTodayCountFlow(final long startOfDay) {
    final String _sql = "SELECT COUNT(*) FROM firewall_events WHERE action = 'BLOCK' AND timestamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"firewall_events"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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
