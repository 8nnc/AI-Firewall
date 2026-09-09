package com.aifirewall.app.data.local.db.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0097@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u0004\u0018\u00010\u000bH\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u000fH\'J\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0011H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00110\u000fH\'J\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0014\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0015\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001e\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0018H\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006\u001b"}, d2 = {"Lcom/aifirewall/app/data/local/db/dao/FirewallProfileDao;", "", "activateProfile", "", "profileId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deactivateAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteProfile", "profile", "Lcom/aifirewall/app/data/local/db/entity/FirewallProfileEntity;", "(Lcom/aifirewall/app/data/local/db/entity/FirewallProfileEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveProfile", "getActiveProfileFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllProfiles", "", "getAllProfilesFlow", "getProfileById", "id", "insertProfile", "setActive", "isActive", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProfile", "app_debug"})
@androidx.room.Dao()
public abstract interface FirewallProfileDao {
    
    @androidx.room.Query(value = "SELECT * FROM firewall_profiles ORDER BY createdAt ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.aifirewall.app.data.local.db.entity.FirewallProfileEntity>> getAllProfilesFlow();
    
    @androidx.room.Query(value = "SELECT * FROM firewall_profiles")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllProfiles(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.aifirewall.app.data.local.db.entity.FirewallProfileEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM firewall_profiles WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getProfileById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.aifirewall.app.data.local.db.entity.FirewallProfileEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM firewall_profiles WHERE isActive = 1 LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.aifirewall.app.data.local.db.entity.FirewallProfileEntity> getActiveProfileFlow();
    
    @androidx.room.Query(value = "SELECT * FROM firewall_profiles WHERE isActive = 1 LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveProfile(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.aifirewall.app.data.local.db.entity.FirewallProfileEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertProfile(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.FirewallProfileEntity profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateProfile(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.FirewallProfileEntity profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteProfile(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.FirewallProfileEntity profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object activateProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String profileId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE firewall_profiles SET isActive = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deactivateAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE firewall_profiles SET isActive = :isActive WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setActive(@org.jetbrains.annotations.NotNull()
    java.lang.String id, boolean isActive, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        @androidx.room.Transaction()
        @org.jetbrains.annotations.Nullable()
        public static java.lang.Object activateProfile(@org.jetbrains.annotations.NotNull()
        com.aifirewall.app.data.local.db.dao.FirewallProfileDao $this, @org.jetbrains.annotations.NotNull()
        java.lang.String profileId, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}