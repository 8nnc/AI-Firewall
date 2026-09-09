package com.aifirewall.app.presentation.screens.datausage;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0003\u0004\u0005\u0006B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0004\u0007\b\t\n\u00a8\u0006\u000b"}, d2 = {"Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState;", "", "()V", "Error", "Loading", "PermissionRequired", "Success", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState$Error;", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState$Loading;", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState$PermissionRequired;", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState$Success;", "app_release"})
public abstract class DataUsageUiState {
    
    private DataUsageUiState() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState$Error;", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_release"})
    public static final class Error extends com.aifirewall.app.presentation.screens.datausage.DataUsageUiState {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String message = null;
        
        public Error(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMessage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.presentation.screens.datausage.DataUsageUiState.Error copy(@org.jetbrains.annotations.NotNull()
        java.lang.String message) {
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
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState$Loading;", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState;", "()V", "app_release"})
    public static final class Loading extends com.aifirewall.app.presentation.screens.datausage.DataUsageUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.aifirewall.app.presentation.screens.datausage.DataUsageUiState.Loading INSTANCE = null;
        
        private Loading() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState$PermissionRequired;", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState;", "()V", "app_release"})
    public static final class PermissionRequired extends com.aifirewall.app.presentation.screens.datausage.DataUsageUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.aifirewall.app.presentation.screens.datausage.DataUsageUiState.PermissionRequired INSTANCE = null;
        
        private PermissionRequired() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00c6\u0003J7\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u00d6\u0003J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u001eH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010\u00a8\u0006\u001f"}, d2 = {"Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState$Success;", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState;", "timeRange", "Lcom/aifirewall/app/domain/model/TimeRange;", "totalWifi", "", "totalMobile", "topApps", "", "Lcom/aifirewall/app/presentation/screens/datausage/AppUsageRecord;", "(Lcom/aifirewall/app/domain/model/TimeRange;JJLjava/util/List;)V", "getTimeRange", "()Lcom/aifirewall/app/domain/model/TimeRange;", "getTopApps", "()Ljava/util/List;", "getTotalMobile", "()J", "getTotalWifi", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class Success extends com.aifirewall.app.presentation.screens.datausage.DataUsageUiState {
        @org.jetbrains.annotations.NotNull()
        private final com.aifirewall.app.domain.model.TimeRange timeRange = null;
        private final long totalWifi = 0L;
        private final long totalMobile = 0L;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.aifirewall.app.presentation.screens.datausage.AppUsageRecord> topApps = null;
        
        public Success(@org.jetbrains.annotations.NotNull()
        com.aifirewall.app.domain.model.TimeRange timeRange, long totalWifi, long totalMobile, @org.jetbrains.annotations.NotNull()
        java.util.List<com.aifirewall.app.presentation.screens.datausage.AppUsageRecord> topApps) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.domain.model.TimeRange getTimeRange() {
            return null;
        }
        
        public final long getTotalWifi() {
            return 0L;
        }
        
        public final long getTotalMobile() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.aifirewall.app.presentation.screens.datausage.AppUsageRecord> getTopApps() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.domain.model.TimeRange component1() {
            return null;
        }
        
        public final long component2() {
            return 0L;
        }
        
        public final long component3() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.aifirewall.app.presentation.screens.datausage.AppUsageRecord> component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.presentation.screens.datausage.DataUsageUiState.Success copy(@org.jetbrains.annotations.NotNull()
        com.aifirewall.app.domain.model.TimeRange timeRange, long totalWifi, long totalMobile, @org.jetbrains.annotations.NotNull()
        java.util.List<com.aifirewall.app.presentation.screens.datausage.AppUsageRecord> topApps) {
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
    }
}