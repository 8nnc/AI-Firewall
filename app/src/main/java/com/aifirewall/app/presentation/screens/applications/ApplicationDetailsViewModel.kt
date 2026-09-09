package com.aifirewall.app.presentation.screens.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aifirewall.app.data.repository.AppRuleRepository
import com.aifirewall.app.data.repository.DataUsageRepository
import com.aifirewall.app.domain.model.AppDataUsage
import com.aifirewall.app.domain.model.AppNetworkRule
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.domain.model.TimeRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ApplicationDetailsViewModel(
    private val packageName: String,
    private val uid: Int,
    private val ruleRepository: AppRuleRepository,
    private val usageRepository: DataUsageRepository,
    private val eventRepository: com.aifirewall.app.data.repository.EventRepository
) : ViewModel() {

    val networkRule: StateFlow<AppNetworkRule?> = ruleRepository.getRuleFlow(packageName, uid)
        .catch { emit(AppNetworkRule(packageName, uid)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _dataUsage = MutableStateFlow<AppDataUsage?>(null)
    val dataUsage = _dataUsage.asStateFlow()

    private val _hasUsagePermission = MutableStateFlow(false)
    val hasUsagePermission = _hasUsagePermission.asStateFlow()

    init {
        loadDataUsage(TimeRange.TODAY)
    }

    fun loadDataUsage(timeRange: TimeRange) {
        viewModelScope.launch {
            val hasPermission = usageRepository.hasUsagePermission()
            _hasUsagePermission.value = hasPermission
            
            if (hasPermission) {
                val usageList = usageRepository.getUsageForTimeRange(timeRange)
                _dataUsage.value = usageList.find { it.uid == uid } ?: AppDataUsage(packageName, uid)
            }
        }
    }

    fun updateWifiPolicy(policy: NetworkPolicy) {
        val currentRule = networkRule.value ?: AppNetworkRule(packageName, uid)
        viewModelScope.launch {
            ruleRepository.saveRule(currentRule.copy(wifiPolicy = policy))
        }
    }

    fun updateMobilePolicy(policy: NetworkPolicy) {
        val currentRule = networkRule.value ?: AppNetworkRule(packageName, uid)
        viewModelScope.launch {
            ruleRepository.saveRule(currentRule.copy(mobileDataPolicy = policy))
        }
    }

    val recentEvents: StateFlow<List<com.aifirewall.app.data.local.db.entity.FirewallEventEntity>> = eventRepository.getEventsForPackageFlow(packageName)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    class Factory(
        private val packageName: String,
        private val uid: Int,
        private val ruleRepository: AppRuleRepository,
        private val usageRepository: DataUsageRepository,
        private val eventRepository: com.aifirewall.app.data.repository.EventRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ApplicationDetailsViewModel::class.java)) {
                return ApplicationDetailsViewModel(packageName, uid, ruleRepository, usageRepository, eventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
