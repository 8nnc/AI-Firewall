package com.aifirewall.app.presentation.screens.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.data.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    val events: StateFlow<List<FirewallEventEntity>> = repository.getEventsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun clearEvents() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
