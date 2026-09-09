package com.aifirewall.app.data.local.db.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\t\bg\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J \u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J \u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\'J\u001c\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u001c\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\r0\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001c\u0010\u0013\u001a\u00020\u00032\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\t0\rH\u00a7@\u00a2\u0006\u0002\u0010\u0015\u00a8\u0006\u0016"}, d2 = {"Lcom/aifirewall/app/data/local/db/dao/AppRuleDao;", "", "deleteRule", "", "profileId", "", "packageName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRule", "Lcom/aifirewall/app/data/local/db/entity/AppRuleEntity;", "getRuleFlow", "Lkotlinx/coroutines/flow/Flow;", "getRulesForProfile", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRulesForProfileFlow", "insertRule", "rule", "(Lcom/aifirewall/app/data/local/db/entity/AppRuleEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertRules", "rules", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface AppRuleDao {
    
    @androidx.room.Query(value = "SELECT * FROM app_rules WHERE profileId = :profileId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.aifirewall.app.data.local.db.entity.AppRuleEntity>> getRulesForProfileFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String profileId);
    
    @androidx.room.Query(value = "SELECT * FROM app_rules WHERE profileId = :profileId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRulesForProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String profileId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.aifirewall.app.data.local.db.entity.AppRuleEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM app_rules WHERE profileId = :profileId AND packageName = :packageName LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRule(@org.jetbrains.annotations.NotNull()
    java.lang.String profileId, @org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.aifirewall.app.data.local.db.entity.AppRuleEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM app_rules WHERE profileId = :profileId AND packageName = :packageName LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.aifirewall.app.data.local.db.entity.AppRuleEntity> getRuleFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String profileId, @org.jetbrains.annotations.NotNull()
    java.lang.String packageName);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertRule(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.AppRuleEntity rule, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertRules(@org.jetbrains.annotations.NotNull()
    java.util.List<com.aifirewall.app.data.local.db.entity.AppRuleEntity> rules, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM app_rules WHERE profileId = :profileId AND packageName = :packageName")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteRule(@org.jetbrains.annotations.NotNull()
    java.lang.String profileId, @org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}