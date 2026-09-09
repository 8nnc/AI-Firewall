package com.aifirewall.app.data.local.db.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\u0007H\'J\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\nH\'J\u001c\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\n2\u0006\u0010\u0011\u001a\u00020\u0012H\'J$\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0016J0\u0010\u0017\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\u001e\u00a8\u0006 "}, d2 = {"Lcom/aifirewall/app/data/local/db/dao/FirewallEventDao;", "", "clearAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteOldEvents", "timestampThreshold", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBlockedTodayCountFlow", "Lkotlinx/coroutines/flow/Flow;", "", "startOfDay", "getEventsFlow", "", "Lcom/aifirewall/app/data/local/db/entity/FirewallEventEntity;", "getEventsForPackageFlow", "packageName", "", "getEventsPaginated", "limit", "offset", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentSimilarBlockEvent", "destinationAddress", "destinationPort", "timeThreshold", "(Ljava/lang/String;Ljava/lang/String;IJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "event", "(Lcom/aifirewall/app/data/local/db/entity/FirewallEventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface FirewallEventDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.FirewallEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.FirewallEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT * FROM firewall_events \n        WHERE packageName = :packageName \n        AND destinationAddress = :destinationAddress \n        AND destinationPort = :destinationPort\n        AND action = \'BLOCK\'\n        AND timestamp >= :timeThreshold\n        ORDER BY timestamp DESC \n        LIMIT 1\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentSimilarBlockEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    java.lang.String destinationAddress, int destinationPort, long timeThreshold, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.aifirewall.app.data.local.db.entity.FirewallEventEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM firewall_events ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getEventsPaginated(int limit, int offset, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.aifirewall.app.data.local.db.entity.FirewallEventEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM firewall_events ORDER BY timestamp DESC LIMIT 500")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.aifirewall.app.data.local.db.entity.FirewallEventEntity>> getEventsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM firewall_events WHERE packageName = :packageName ORDER BY timestamp DESC LIMIT 500")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.aifirewall.app.data.local.db.entity.FirewallEventEntity>> getEventsForPackageFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName);
    
    @androidx.room.Query(value = "DELETE FROM firewall_events WHERE timestamp < :timestampThreshold")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOldEvents(long timestampThreshold, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM firewall_events WHERE action = \'BLOCK\' AND timestamp >= :startOfDay")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getBlockedTodayCountFlow(long startOfDay);
    
    @androidx.room.Query(value = "DELETE FROM firewall_events")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}