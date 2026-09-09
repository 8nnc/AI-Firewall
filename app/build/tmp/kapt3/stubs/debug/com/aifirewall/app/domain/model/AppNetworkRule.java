package com.aifirewall.app.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0015\b\u0086\b\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J1\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u000b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\n\u001a\u00020\u000b8F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\fR\u0011\u0010\r\u001a\u00020\u000b8F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0011\u0010\u000e\u001a\u00020\u000b8F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010\u00a8\u0006 "}, d2 = {"Lcom/aifirewall/app/domain/model/AppNetworkRule;", "", "packageName", "", "uid", "", "wifiPolicy", "Lcom/aifirewall/app/domain/model/NetworkPolicy;", "mobileDataPolicy", "(Ljava/lang/String;ILcom/aifirewall/app/domain/model/NetworkPolicy;Lcom/aifirewall/app/domain/model/NetworkPolicy;)V", "isCompletelyBlocked", "", "()Z", "isMobileBlocked", "isWifiBlocked", "getMobileDataPolicy", "()Lcom/aifirewall/app/domain/model/NetworkPolicy;", "getPackageName", "()Ljava/lang/String;", "getUid", "()I", "getWifiPolicy", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "toString", "Companion", "app_debug"})
public final class AppNetworkRule {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String packageName = null;
    private final int uid = 0;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.domain.model.NetworkPolicy wifiPolicy = null;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.domain.model.NetworkPolicy mobileDataPolicy = null;
    public static final int UNKNOWN_UID = -1;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String UNKNOWN_PACKAGE = "UNKNOWN_APPLICATION";
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.domain.model.AppNetworkRule.Companion Companion = null;
    
    public AppNetworkRule(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, int uid, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy wifiPolicy, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy mobileDataPolicy) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPackageName() {
        return null;
    }
    
    public final int getUid() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.NetworkPolicy getWifiPolicy() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.NetworkPolicy getMobileDataPolicy() {
        return null;
    }
    
    public final boolean isWifiBlocked() {
        return false;
    }
    
    public final boolean isMobileBlocked() {
        return false;
    }
    
    public final boolean isCompletelyBlocked() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.NetworkPolicy component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.NetworkPolicy component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.aifirewall.app.domain.model.AppNetworkRule copy(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, int uid, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy wifiPolicy, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy mobileDataPolicy) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/aifirewall/app/domain/model/AppNetworkRule$Companion;", "", "()V", "UNKNOWN_PACKAGE", "", "UNKNOWN_UID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}