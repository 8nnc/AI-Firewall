package com.aifirewall.app.engine

import com.aifirewall.app.domain.model.FirewallState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FirewallStateManager {
    private val _firewallState = MutableStateFlow(FirewallState.INACTIVE)
    val firewallState: StateFlow<FirewallState> = _firewallState.asStateFlow()

    fun setState(state: FirewallState) {
        _firewallState.value = state
    }
}
