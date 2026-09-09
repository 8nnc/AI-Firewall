package com.aifirewall.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\u000e\u0010 \u001a\u00020!H\u0086@\u00a2\u0006\u0002\u0010\"J\u0018\u0010#\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020&0%0$J\u001c\u0010\'\u001a\b\u0012\u0004\u0012\u00020&0$2\u0006\u0010(\u001a\u00020!2\u0006\u0010)\u001a\u00020*J\u000e\u0010+\u001a\u00020\u001fH\u0082@\u00a2\u0006\u0002\u0010\"JC\u0010,\u001a\u00020&2\u0006\u0010(\u001a\u00020!2\u0006\u0010)\u001a\u00020*2\b\u0010-\u001a\u0004\u0018\u00010.2\f\u0010/\u001a\b\u0012\u0004\u0012\u000201002\f\u00102\u001a\b\u0012\u0004\u0012\u00020300H\u0000\u00a2\u0006\u0002\b4J\u0016\u00105\u001a\u00020\u001f2\u0006\u00106\u001a\u00020&H\u0086@\u00a2\u0006\u0002\u00107J\u001c\u00108\u001a\u00020\u001f2\f\u00109\u001a\b\u0012\u0004\u0012\u00020&00H\u0086@\u00a2\u0006\u0002\u0010:R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\n\u001a\u0004\b\r\u0010\u000eR\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\n\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0019\u001a\u00020\u001a8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001d\u0010\n\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006;"}, d2 = {"Lcom/aifirewall/app/data/repository/AppRuleRepository;", "Ljava/io/Closeable;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "database", "Lcom/aifirewall/app/data/local/db/FirewallDatabase;", "getDatabase", "()Lcom/aifirewall/app/data/local/db/FirewallDatabase;", "database$delegate", "Lkotlin/Lazy;", "groupDao", "Lcom/aifirewall/app/data/local/db/dao/RuleGroupDao;", "getGroupDao", "()Lcom/aifirewall/app/data/local/db/dao/RuleGroupDao;", "groupDao$delegate", "migrationJob", "Lkotlinx/coroutines/Job;", "profileDao", "Lcom/aifirewall/app/data/local/db/dao/FirewallProfileDao;", "getProfileDao", "()Lcom/aifirewall/app/data/local/db/dao/FirewallProfileDao;", "profileDao$delegate", "repositoryScope", "Lkotlinx/coroutines/CoroutineScope;", "ruleDao", "Lcom/aifirewall/app/data/local/db/dao/AppRuleDao;", "getRuleDao", "()Lcom/aifirewall/app/data/local/db/dao/AppRuleDao;", "ruleDao$delegate", "close", "", "getActiveProfileId", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllRulesFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/aifirewall/app/domain/model/AppNetworkRule;", "getRuleFlow", "packageName", "uid", "", "migrateDataStoreToRoom", "resolvePolicy", "appRule", "Lcom/aifirewall/app/data/local/db/entity/AppRuleEntity;", "groups", "", "Lcom/aifirewall/app/data/local/db/entity/RuleGroupEntity;", "memberships", "Lcom/aifirewall/app/data/local/db/entity/GroupMemberEntity;", "resolvePolicy$app_release", "saveRule", "rule", "(Lcom/aifirewall/app/domain/model/AppNetworkRule;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveRules", "rules", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public final class AppRuleRepository implements java.io.Closeable {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy ruleDao$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy profileDao$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy groupDao$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope repositoryScope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job migrationJob;
    
    public AppRuleRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final com.aifirewall.app.data.local.db.FirewallDatabase getDatabase() {
        return null;
    }
    
    private final com.aifirewall.app.data.local.db.dao.AppRuleDao getRuleDao() {
        return null;
    }
    
    private final com.aifirewall.app.data.local.db.dao.FirewallProfileDao getProfileDao() {
        return null;
    }
    
    private final com.aifirewall.app.data.local.db.dao.RuleGroupDao getGroupDao() {
        return null;
    }
    
    @java.lang.Override()
    public void close() {
    }
    
    private final java.lang.Object migrateDataStoreToRoom(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getActiveProfileId(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.AppNetworkRule resolvePolicy$app_release(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, int uid, @org.jetbrains.annotations.Nullable()
    com.aifirewall.app.data.local.db.entity.AppRuleEntity appRule, @org.jetbrains.annotations.NotNull()
    java.util.List<com.aifirewall.app.data.local.db.entity.RuleGroupEntity> groups, @org.jetbrains.annotations.NotNull()
    java.util.List<com.aifirewall.app.data.local.db.entity.GroupMemberEntity> memberships) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.aifirewall.app.domain.model.AppNetworkRule> getRuleFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, int uid) {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.Map<java.lang.String, com.aifirewall.app.domain.model.AppNetworkRule>> getAllRulesFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveRule(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.AppNetworkRule rule, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveRules(@org.jetbrains.annotations.NotNull()
    java.util.List<com.aifirewall.app.domain.model.AppNetworkRule> rules, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}