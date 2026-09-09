package com.aifirewall.app.data.local.db.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\b\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\tH\u0097@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\u0010H\'J\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\tH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\t0\u0010H\'J\u001c\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\t2\u0006\u0010\u0016\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u001c\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\t0\u00102\u0006\u0010\u0016\u001a\u00020\u0015H\'J\u0016\u0010\u0019\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u001e\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u001c\u00a8\u0006\u001d"}, d2 = {"Lcom/aifirewall/app/data/local/db/dao/RuleGroupDao;", "", "addMember", "", "member", "Lcom/aifirewall/app/data/local/db/entity/GroupMemberEntity;", "(Lcom/aifirewall/app/data/local/db/entity/GroupMemberEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addMembers", "members", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroup", "group", "Lcom/aifirewall/app/data/local/db/entity/RuleGroupEntity;", "(Lcom/aifirewall/app/data/local/db/entity/RuleGroupEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllGroupMembersFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllGroups", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllGroupsFlow", "getGroupMembers", "", "groupId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGroupMembersFlow", "insertGroup", "removeMember", "packageName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
@androidx.room.Dao()
public abstract interface RuleGroupDao {
    
    @androidx.room.Query(value = "SELECT * FROM rule_groups")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.aifirewall.app.data.local.db.entity.RuleGroupEntity>> getAllGroupsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM group_members")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.aifirewall.app.data.local.db.entity.GroupMemberEntity>> getAllGroupMembersFlow();
    
    @androidx.room.Query(value = "SELECT * FROM rule_groups")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllGroups(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.aifirewall.app.data.local.db.entity.RuleGroupEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGroup(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.RuleGroupEntity group, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteGroup(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.RuleGroupEntity group, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT packageName FROM group_members WHERE groupId = :groupId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<java.lang.String>> getGroupMembersFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId);
    
    @androidx.room.Query(value = "SELECT packageName FROM group_members WHERE groupId = :groupId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGroupMembers(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion);
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addMember(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.GroupMemberEntity member, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM group_members WHERE groupId = :groupId AND packageName = :packageName")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object removeMember(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addMembers(@org.jetbrains.annotations.NotNull()
    java.util.List<com.aifirewall.app.data.local.db.entity.GroupMemberEntity> members, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        @androidx.room.Transaction()
        @org.jetbrains.annotations.Nullable()
        public static java.lang.Object addMembers(@org.jetbrains.annotations.NotNull()
        com.aifirewall.app.data.local.db.dao.RuleGroupDao $this, @org.jetbrains.annotations.NotNull()
        java.util.List<com.aifirewall.app.data.local.db.entity.GroupMemberEntity> members, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
    }
}