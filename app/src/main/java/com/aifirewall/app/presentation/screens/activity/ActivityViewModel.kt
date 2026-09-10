package com.aifirewall.app.presentation.screens.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class EventActionFilter {
    ALL, BLOCKED, ALLOWED
}

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _actionFilter = MutableStateFlow(EventActionFilter.ALL)
    val actionFilter: StateFlow<EventActionFilter> = _actionFilter

    val events: StateFlow<List<FirewallEventEntity>> = repository.getEventsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredEvents: StateFlow<List<FirewallEventEntity>> = combine(
        events,
        _searchQuery,
        _actionFilter
    ) { allEvents, query, filter ->
        allEvents.filter { event ->
            val matchesQuery = query.isBlank() ||
                    event.packageName.contains(query, ignoreCase = true) ||
                    event.destinationAddress.contains(query, ignoreCase = true) ||
                    event.reason.contains(query, ignoreCase = true) ||
                    event.protocol.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                EventActionFilter.ALL -> true
                EventActionFilter.BLOCKED -> event.action == "BLOCK"
                EventActionFilter.ALLOWED -> event.action == "ALLOW"
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateActionFilter(filter: EventActionFilter) {
        _actionFilter.value = filter
    }

    fun clearEvents() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
