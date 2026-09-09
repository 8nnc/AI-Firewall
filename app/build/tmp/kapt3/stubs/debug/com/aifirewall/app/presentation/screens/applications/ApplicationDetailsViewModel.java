package com.aifirewall.app.presentation.screens.applications;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\'B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"J\u000e\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020%J\u000e\u0010&\u001a\u00020 2\u0006\u0010$\u001a\u00020%R\u0016\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015R\u0019\u0010\u0018\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u001c0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0015R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/aifirewall/app/presentation/screens/applications/ApplicationDetailsViewModel;", "Landroidx/lifecycle/ViewModel;", "packageName", "", "uid", "", "ruleRepository", "Lcom/aifirewall/app/data/repository/AppRuleRepository;", "usageRepository", "Lcom/aifirewall/app/data/repository/DataUsageRepository;", "eventRepository", "Lcom/aifirewall/app/data/repository/EventRepository;", "(Ljava/lang/String;ILcom/aifirewall/app/data/repository/AppRuleRepository;Lcom/aifirewall/app/data/repository/DataUsageRepository;Lcom/aifirewall/app/data/repository/EventRepository;)V", "_dataUsage", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/aifirewall/app/domain/model/AppDataUsage;", "_hasUsagePermission", "", "dataUsage", "Lkotlinx/coroutines/flow/StateFlow;", "getDataUsage", "()Lkotlinx/coroutines/flow/StateFlow;", "hasUsagePermission", "getHasUsagePermission", "networkRule", "Lcom/aifirewall/app/domain/model/AppNetworkRule;", "getNetworkRule", "recentEvents", "", "Lcom/aifirewall/app/data/local/db/entity/FirewallEventEntity;", "getRecentEvents", "loadDataUsage", "", "timeRange", "Lcom/aifirewall/app/domain/model/TimeRange;", "updateMobilePolicy", "policy", "Lcom/aifirewall/app/domain/model/NetworkPolicy;", "updateWifiPolicy", "Factory", "app_debug"})
public final class ApplicationDetailsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String packageName = null;
    private final int uid = 0;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.repository.AppRuleRepository ruleRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.repository.DataUsageRepository usageRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.repository.EventRepository eventRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.domain.model.AppNetworkRule> networkRule = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.aifirewall.app.domain.model.AppDataUsage> _dataUsage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.domain.model.AppDataUsage> dataUsage = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _hasUsagePermission = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> hasUsagePermission = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.aifirewall.app.data.local.db.entity.FirewallEventEntity>> recentEvents = null;
    
    public ApplicationDetailsViewModel(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, int uid, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.repository.AppRuleRepository ruleRepository, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.repository.DataUsageRepository usageRepository, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.repository.EventRepository eventRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.domain.model.AppNetworkRule> getNetworkRule() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.aifirewall.app.domain.model.AppDataUsage> getDataUsage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getHasUsagePermission() {
        return null;
    }
    
    public final void loadDataUsage(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.TimeRange timeRange) {
    }
    
    public final void updateWifiPolicy(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy policy) {
    }
    
    public final void updateMobilePolicy(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy policy) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.aifirewall.app.data.local.db.entity.FirewallEventEntity>> getRecentEvents() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ%\u0010\r\u001a\u0002H\u000e\"\b\b\u0000\u0010\u000e*\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0011H\u0016\u00a2\u0006\u0002\u0010\u0012R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/aifirewall/app/presentation/screens/applications/ApplicationDetailsViewModel$Factory;", "Landroidx/lifecycle/ViewModelProvider$Factory;", "packageName", "", "uid", "", "ruleRepository", "Lcom/aifirewall/app/data/repository/AppRuleRepository;", "usageRepository", "Lcom/aifirewall/app/data/repository/DataUsageRepository;", "eventRepository", "Lcom/aifirewall/app/data/repository/EventRepository;", "(Ljava/lang/String;ILcom/aifirewall/app/data/repository/AppRuleRepository;Lcom/aifirewall/app/data/repository/DataUsageRepository;Lcom/aifirewall/app/data/repository/EventRepository;)V", "create", "T", "Landroidx/lifecycle/ViewModel;", "modelClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Landroidx/lifecycle/ViewModel;", "app_debug"})
    public static final class Factory implements androidx.lifecycle.ViewModelProvider.Factory {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String packageName = null;
        private final int uid = 0;
        @org.jetbrains.annotations.NotNull()
        private final com.aifirewall.app.data.repository.AppRuleRepository ruleRepository = null;
        @org.jetbrains.annotations.NotNull()
        private final com.aifirewall.app.data.repository.DataUsageRepository usageRepository = null;
        @org.jetbrains.annotations.NotNull()
        private final com.aifirewall.app.data.repository.EventRepository eventRepository = null;
        
        public Factory(@org.jetbrains.annotations.NotNull()
        java.lang.String packageName, int uid, @org.jetbrains.annotations.NotNull()
        com.aifirewall.app.data.repository.AppRuleRepository ruleRepository, @org.jetbrains.annotations.NotNull()
        com.aifirewall.app.data.repository.DataUsageRepository usageRepository, @org.jetbrains.annotations.NotNull()
        com.aifirewall.app.data.repository.EventRepository eventRepository) {
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