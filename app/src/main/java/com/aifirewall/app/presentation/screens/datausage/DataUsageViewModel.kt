package com.aifirewall.app.presentation.screens.datausage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aifirewall.app.data.repository.DataUsageRepository
import com.aifirewall.app.data.repository.InstalledAppsRepository
import com.aifirewall.app.domain.model.AppDataUsage
import com.aifirewall.app.domain.model.InstalledApp
import com.aifirewall.app.domain.model.TimeRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AppUsageRecord(
    val app: InstalledApp,
    val usage: AppDataUsage
)

sealed class DataUsageUiState {
    object Loading : DataUsageUiState()
    object PermissionRequired : DataUsageUiState()
    data class Success(
        val timeRange: TimeRange,
        val totalWifi: Long,
        val totalMobile: Long,
        val topApps: List<AppUsageRecord>
    ) : DataUsageUiState()
    data class Error(val message: String) : DataUsageUiState()
}

class DataUsageViewModel(
    private val usageRepo: DataUsageRepository,
    private val appRepo: InstalledAppsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataUsageUiState>(DataUsageUiState.Loading)
    val uiState: StateFlow<DataUsageUiState> = _uiState

    private var currentTimeRange = TimeRange.TODAY

    init {
        loadUsageData(currentTimeRange)
    }

    fun setTimeRange(timeRange: TimeRange) {
        currentTimeRange = timeRange
        loadUsageData(timeRange)
    }

    fun refresh() {
        loadUsageData(currentTimeRange)
    }

    private fun loadUsageData(timeRange: TimeRange) {
        viewModelScope.launch {
            _uiState.value = DataUsageUiState.Loading
            
            if (!usageRepo.hasUsagePermission()) {
                _uiState.value = DataUsageUiState.PermissionRequired
                return@launch
            }

            try {
                val usageData = usageRepo.getUsageForTimeRange(timeRange)
                val installedApps = appRepo.getInstalledApps()

                var totalWifi = 0L
                var totalMobile = 0L
                val validRecords = mutableListOf<AppUsageRecord>()

                for (usage in usageData) {
                    // Only sum total traffic for known apps and standard usages
                    totalWifi += usage.totalWifi
                    totalMobile += usage.totalMobile
                    
                    if (usage.totalBytes > 0) {
                        val app = installedApps.find { it.uid == usage.uid }
                        if (app != null) {
                            validRecords.add(AppUsageRecord(app, usage))
                        }
                    }
                }

                val sortedTopApps = validRecords.sortedByDescending { it.usage.totalBytes }

                _uiState.value = DataUsageUiState.Success(
                    timeRange = timeRange,
                    totalWifi = totalWifi,
                    totalMobile = totalMobile,
                    topApps = sortedTopApps
                )
            } catch (e: Exception) {
                _uiState.value = DataUsageUiState.Error(e.localizedMessage ?: "Failed to load data usage")
            }
        }
    }

    class Factory(
        private val usageRepo: DataUsageRepository,
        private val appRepo: InstalledAppsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DataUsageViewModel::class.java)) {
                return DataUsageViewModel(usageRepo, appRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
