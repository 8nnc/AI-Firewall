package com.aifirewall.app.presentation.screens.dashboard

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.domain.model.FirewallState
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.engine.FirewallStateManager
import com.aifirewall.app.engine.network.NetworkMonitor
import com.aifirewall.app.engine.policy.FirewallPolicyEngine
import com.aifirewall.app.engine.service.FirewallVpnService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    // Single source of truth for the firewall state
    val firewallState: StateFlow<FirewallState> = FirewallStateManager.firewallState
    
    private val policyEngine = FirewallPolicyEngine(application)
    private val eventRepository = com.aifirewall.app.data.repository.EventRepository(application)

    val blockedTodayCount: StateFlow<Int> = eventRepository.getBlockedTodayCountFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Calculate how many apps have ANY blocking rule configured
    val blockedAppsCount: StateFlow<Int> = policyEngine.rulesFlow
        .combine(firewallState) { rules, state ->
            if (state == FirewallState.ACTIVE) {
                rules.count { it.wifiPolicy == NetworkPolicy.BLOCK || it.mobileDataPolicy == NetworkPolicy.BLOCK }
            } else {
                0
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Real count of apps configured with blocking rules
    val configuredRulesCount: StateFlow<Int> = policyEngine.rulesFlow
        .map { rules -> rules.count { it.wifiPolicy == NetworkPolicy.BLOCK || it.mobileDataPolicy == NetworkPolicy.BLOCK } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Real total count of all configured app rules
    val totalRulesCount: StateFlow<Int> = policyEngine.rulesFlow
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Real underlying physical network transport
    val currentNetwork: StateFlow<NetworkMonitor.NetworkType> = NetworkMonitor.currentNetwork

    // Recent 4 security events for dashboard audit feed
    val recentEvents: StateFlow<List<FirewallEventEntity>> = eventRepository.getEventsFlow()
        .map { it.take(4) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFirewall(context: Context) {
        val currentState = firewallState.value
        if (currentState == FirewallState.INACTIVE || currentState == FirewallState.ERROR) {
            // Permission is handled by UI via VpnService.prepare(context)
            // The UI will call startVpnService directly after getting permission
        } else if (currentState == FirewallState.ACTIVE || currentState == FirewallState.STARTING) {
            // Initiate Stop Flow
            stopVpnService(context)
        }
    }

    fun startVpnService(context: Context) {
        FirewallStateManager.setState(FirewallState.STARTING)
        val intent = Intent(context, FirewallVpnService::class.java).apply {
            action = FirewallVpnService.ACTION_START
        }
        androidx.core.content.ContextCompat.startForegroundService(context, intent)
    }

    fun stopVpnService(context: Context) {
        FirewallStateManager.setState(FirewallState.STOPPING)
        val intent = Intent(context, FirewallVpnService::class.java).apply {
            action = FirewallVpnService.ACTION_STOP
        }
        androidx.core.content.ContextCompat.startForegroundService(context, intent)
    }

    override fun onCleared() {
        super.onCleared()
        policyEngine.close()
    }
}
