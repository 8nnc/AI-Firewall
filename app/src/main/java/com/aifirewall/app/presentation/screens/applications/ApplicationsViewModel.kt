package com.aifirewall.app.presentation.screens.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aifirewall.app.data.repository.AppRuleRepository
import com.aifirewall.app.data.repository.DataUsageRepository
import com.aifirewall.app.data.repository.InstalledAppsRepository
import com.aifirewall.app.domain.model.AppNetworkRule
import com.aifirewall.app.domain.model.AppDataUsage
import com.aifirewall.app.domain.model.InstalledApp
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.domain.model.TimeRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppStatusFilter {
    ALL, ALLOWED, BLOCKED, WIFI_BLOCKED, MOBILE_BLOCKED
}

enum class AppTypeFilter {
    ALL, USER, SYSTEM
}

enum class AppSortOption {
    NAME_ASC, NAME_DESC, DATA_USAGE_DESC
}

data class AppWithRule(
    val app: InstalledApp,
    val rule: AppNetworkRule,
    val usage: AppDataUsage?
)

sealed class ApplicationsUiState {
    object Loading : ApplicationsUiState()
    data class Success(val apps: List<AppWithRule>) : ApplicationsUiState()
    data class Error(val message: String) : ApplicationsUiState()
}

class ApplicationsViewModel(
    private val appRepository: InstalledAppsRepository,
    private val ruleRepository: AppRuleRepository,
    private val dataUsageRepository: DataUsageRepository
) : ViewModel() {

    private val _allApps = MutableStateFlow<List<InstalledApp>>(emptyList())
    private val _dataUsage = MutableStateFlow<Map<Int, AppDataUsage>>(emptyMap())
    private val _searchQuery = MutableStateFlow("")
    private val _statusFilter = MutableStateFlow(AppStatusFilter.ALL)
    private val _typeFilter = MutableStateFlow(AppTypeFilter.ALL)
    private val _sortOption = MutableStateFlow(AppSortOption.NAME_ASC)
    
    private val _selectedPackages = MutableStateFlow<Set<String>>(emptySet())
    val selectedPackages: StateFlow<Set<String>> = _selectedPackages

    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val searchQuery: StateFlow<String> = _searchQuery
    val statusFilter: StateFlow<AppStatusFilter> = _statusFilter
    val typeFilter: StateFlow<AppTypeFilter> = _typeFilter
    val sortOption: StateFlow<AppSortOption> = _sortOption

    val uiState: StateFlow<ApplicationsUiState> = combine(
        _allApps,
        ruleRepository.getAllRulesFlow(),
        _dataUsage,
        _searchQuery,
        _statusFilter,
        _typeFilter,
        _sortOption,
        _isLoading,
        _error
    ) { args ->
        val apps = args[0] as List<InstalledApp>
        val rulesMap = args[1] as Map<String, AppNetworkRule>
        val dataUsage = args[2] as Map<Int, AppDataUsage>
        val query = args[3] as String
        val statusFilter = args[4] as AppStatusFilter
        val typeFilter = args[5] as AppTypeFilter
        val sortOption = args[6] as AppSortOption
        val isLoading = args[7] as Boolean
        val error = args[8] as String?

        when {
            isLoading -> ApplicationsUiState.Loading
            error != null -> ApplicationsUiState.Error(error)
            else -> {
                val mappedApps = apps.map { app ->
                    val rule = rulesMap[app.packageName] ?: AppNetworkRule(app.packageName, app.uid)
                    AppWithRule(app, rule, dataUsage[app.uid])
                }

                val filtered = mappedApps.filter { item ->
                    val matchesSearch = query.isBlank() || 
                        item.app.appName.contains(query, ignoreCase = true) ||
                        item.app.packageName.contains(query, ignoreCase = true)
                    
                    val matchesStatus = when (statusFilter) {
                        AppStatusFilter.ALL -> true
                        AppStatusFilter.ALLOWED -> !item.rule.isWifiBlocked && !item.rule.isMobileBlocked
                        AppStatusFilter.BLOCKED -> item.rule.isCompletelyBlocked
                        AppStatusFilter.WIFI_BLOCKED -> item.rule.isWifiBlocked
                        AppStatusFilter.MOBILE_BLOCKED -> item.rule.isMobileBlocked
                    }
                    
                    val matchesType = when (typeFilter) {
                        AppTypeFilter.ALL -> true
                        AppTypeFilter.USER -> !item.app.isSystemApp
                        AppTypeFilter.SYSTEM -> item.app.isSystemApp
                    }
                    
                    matchesSearch && matchesStatus && matchesType
                }
                
                val sorted = when(sortOption) {
                    AppSortOption.NAME_ASC -> filtered.sortedBy { it.app.appName.lowercase() }
                    AppSortOption.NAME_DESC -> filtered.sortedByDescending { it.app.appName.lowercase() }
                    AppSortOption.DATA_USAGE_DESC -> filtered.sortedByDescending { it.usage?.totalBytes ?: 0L }
                }
                
                ApplicationsUiState.Success(sorted)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ApplicationsUiState.Loading
    )

    init {
        loadApplications()
    }

    fun loadApplications() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val apps = appRepository.getInstalledApps()
                _allApps.value = apps
                
                // Load usage
                val usage = dataUsageRepository.getUsageForTimeRange(TimeRange.THIS_MONTH)
                _dataUsage.value = usage.associateBy { it.uid }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Failed to load applications"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateStatusFilter(filter: AppStatusFilter) {
        _statusFilter.value = filter
    }
    
    fun updateTypeFilter(filter: AppTypeFilter) {
        _typeFilter.value = filter
    }
    
    fun updateSortOption(option: AppSortOption) {
        _sortOption.value = option
    }
    
    fun toggleSelection(packageName: String) {
        val current = _selectedPackages.value.toMutableSet()
        if (current.contains(packageName)) {
            current.remove(packageName)
        } else {
            current.add(packageName)
        }
        _selectedPackages.value = current
    }
    
    fun selectAll(apps: List<AppWithRule>) {
        _selectedPackages.value = apps.map { it.app.packageName }.toSet()
    }
    
    fun clearSelection() {
        _selectedPackages.value = emptySet()
    }

    fun toggleWifiPolicy(item: AppWithRule) {
        val newWifiPolicy = if (item.rule.wifiPolicy == NetworkPolicy.ALLOW) NetworkPolicy.BLOCK else NetworkPolicy.ALLOW
        val updatedRule = item.rule.copy(wifiPolicy = newWifiPolicy)
        viewModelScope.launch {
            ruleRepository.saveRule(updatedRule)
        }
    }

    fun toggleMobilePolicy(item: AppWithRule) {
        val newMobilePolicy = if (item.rule.mobileDataPolicy == NetworkPolicy.ALLOW) NetworkPolicy.BLOCK else NetworkPolicy.ALLOW
        val updatedRule = item.rule.copy(mobileDataPolicy = newMobilePolicy)
        viewModelScope.launch {
            ruleRepository.saveRule(updatedRule)
        }
    }
    
    fun applyBulkPolicy(wifiPolicy: NetworkPolicy, mobilePolicy: NetworkPolicy) {
        val packages = _selectedPackages.value
        val allAppsMap = _allApps.value.associateBy { it.packageName }
        
        viewModelScope.launch {
            val rulesToSave = packages.mapNotNull { pkg ->
                allAppsMap[pkg]?.let { app ->
                    AppNetworkRule(
                        packageName = pkg,
                        uid = app.uid,
                        wifiPolicy = wifiPolicy,
                        mobileDataPolicy = mobilePolicy
                    )
                }
            }
            if (rulesToSave.isNotEmpty()) {
                ruleRepository.saveRules(rulesToSave)
            }
            clearSelection()
        }
    }

    class Factory(
        private val appRepository: InstalledAppsRepository,
        private val ruleRepository: AppRuleRepository,
        private val dataUsageRepository: DataUsageRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ApplicationsViewModel::class.java)) {
                return ApplicationsViewModel(appRepository, ruleRepository, dataUsageRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
