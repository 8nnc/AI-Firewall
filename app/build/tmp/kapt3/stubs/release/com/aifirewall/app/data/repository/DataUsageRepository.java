package com.aifirewall.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\rH\u0002J\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0006\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/aifirewall/app/data/repository/DataUsageRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "appOpsManager", "Landroid/app/AppOpsManager;", "networkStatsManager", "Landroid/app/usage/NetworkStatsManager;", "getTimeBounds", "Lkotlin/Pair;", "", "timeRange", "Lcom/aifirewall/app/domain/model/TimeRange;", "getUsageForTimeRange", "", "Lcom/aifirewall/app/domain/model/AppDataUsage;", "(Lcom/aifirewall/app/domain/model/TimeRange;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hasUsagePermission", "", "app_release"})
public final class DataUsageRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.usage.NetworkStatsManager networkStatsManager = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.AppOpsManager appOpsManager = null;
    
    public DataUsageRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final boolean hasUsagePermission() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUsageForTimeRange(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.TimeRange timeRange, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.aifirewall.app.domain.model.AppDataUsage>> $completion) {
        return null;
    }
    
    private final kotlin.Pair<java.lang.Long, java.lang.Long> getTimeBounds(com.aifirewall.app.domain.model.TimeRange timeRange) {
        return null;
    }
}