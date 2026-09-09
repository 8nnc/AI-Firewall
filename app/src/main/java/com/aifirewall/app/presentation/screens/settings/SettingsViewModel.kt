package com.aifirewall.app.presentation.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aifirewall.app.data.repository.ConfigurationManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val configurationManager: ConfigurationManager) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow

    fun exportConfiguration(uri: Uri) {
        viewModelScope.launch {
            val result = configurationManager.exportConfiguration(uri)
            if (result.isSuccess) {
                _messageFlow.emit("Configuration exported successfully")
            } else {
                _messageFlow.emit("Export failed: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun importConfiguration(uri: Uri) {
        viewModelScope.launch {
            val result = configurationManager.importConfiguration(uri)
            if (result.isSuccess) {
                _messageFlow.emit("Configuration imported successfully")
            } else {
                _messageFlow.emit("Import failed: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    class Factory(private val configManager: ConfigurationManager) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = SettingsViewModel(configManager) as T
    }
}
