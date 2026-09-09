package com.aifirewall.app.presentation.screens.datausage;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u0015B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bH\u0002J\u0006\u0010\u0013\u001a\u00020\u0011J\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/aifirewall/app/presentation/screens/datausage/DataUsageViewModel;", "Landroidx/lifecycle/ViewModel;", "usageRepo", "Lcom/aifirewall/app/data/repository/DataUsageRepository;", "appRepo", "Lcom/aifirewall/app/data/repository/InstalledAppsRepository;", "(Lcom/aifirewall/app/data/repository/DataUsageRepository;Lcom/aifirewall/app/data/repository/InstalledAppsRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/aifirewall/app/presentation/screens/datausage/DataUsageUiState;", "currentTimeRange", "Lcom/aifirewall/app/domain/model/TimeRange;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadUsageData", "", "timeRange", "refresh", "setTimeRange", "Factory", "app_release"})
public final class DataUsageViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.repository.DataUsageRepository usageRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.repository.InstalledAppsRepository appRepo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.aifirewall.app.presentation.screens.datausage.DataUsageUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.presentation.screens.datausage.DataUsageUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private com.aifirewall.app.domain.model.TimeRange currentTimeRange = com.aifirewall.app.domain.model.TimeRange.TODAY;
    
    public DataUsageViewModel(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.repository.DataUsageRepository usageRepo, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.repository.InstalledAppsRepository appRepo) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.presentation.screens.datausage.DataUsageUiState> getUiState() {
        return null;
    }
    
    public final void setTimeRange(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.TimeRange timeRange) {
    }
    
    public final void refresh() {
    }
    
    private final void loadUsageData(com.aifirewall.app.domain.model.TimeRange timeRange) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J%\u0010\u0007\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\b0\u000bH\u0016\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/aifirewall/app/presentation/screens/datausage/DataUsageViewModel$Factory;", "Landroidx/lifecycle/ViewModelProvider$Factory;", "usageRepo", "Lcom/aifirewall/app/data/repository/DataUsageRepository;", "appRepo", "Lcom/aifirewall/app/data/repository/InstalledAppsRepository;", "(Lcom/aifirewall/app/data/repository/DataUsageRepository;Lcom/aifirewall/app/data/repository/InstalledAppsRepository;)V", "create", "T", "Landroidx/lifecycle/ViewModel;", "modelClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Landroidx/lifecycle/ViewModel;", "app_release"})
    public static final class Factory implements androidx.lifecycle.ViewModelProvider.Factory {
        @org.jetbrains.annotations.NotNull()
        private final com.aifirewall.app.data.repository.DataUsageRepository usageRepo = null;
        @org.jetbrains.annotations.NotNull()
        private final com.aifirewall.app.data.repository.InstalledAppsRepository appRepo = null;
        
        public Factory(@org.jetbrains.annotations.NotNull()
        com.aifirewall.app.data.repository.DataUsageRepository usageRepo, @org.jetbrains.annotations.NotNull()
        com.aifirewall.app.data.repository.InstalledAppsRepository appRepo) {
            super();
        }
        
        @java.lang.Override()
        @kotlin.Suppress(names = {"UNCHECKED_CAST"})
        @org.jetbrains.annotations.NotNull()
        public <T extends androidx.lifecycle.ViewModel>T create(@org.jetbrains.annotations.NotNull()
        java.lang.Class<T> modelClass) {
            return null;
        }
    }
}