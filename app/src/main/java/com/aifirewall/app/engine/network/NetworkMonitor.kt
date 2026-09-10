package com.aifirewall.app.engine.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NetworkMonitor {
    private var connectivityManager: ConnectivityManager? = null

    enum class NetworkType {
        WIFI,
        MOBILE,
        NONE,
        UNKNOWN
    }

    private val _currentNetwork = MutableStateFlow(NetworkType.UNKNOWN)
    val currentNetwork: StateFlow<NetworkType> = _currentNetwork.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            updateNetworkState()
        }
        
        override fun onLost(network: Network) {
            super.onLost(network)
            updateNetworkState()
        }
        
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            updateNetworkState()
        }
    }

    private fun updateNetworkState() {
        val cm = connectivityManager ?: return
        val activeNetwork = cm.activeNetwork
        if (activeNetwork == null) {
            _currentNetwork.value = NetworkType.NONE
            return
        }
        
        var capabilities = cm.getNetworkCapabilities(activeNetwork)
        if (capabilities == null) {
            _currentNetwork.value = NetworkType.NONE
            return
        }

        // If the active network is our VPN, inspect non-VPN networks to determine underlying physical transport
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
            var physicalCaps: NetworkCapabilities? = null
            for (network in cm.allNetworks) {
                if (network != activeNetwork) {
                    val caps = cm.getNetworkCapabilities(network)
                    if (caps != null && !caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        physicalCaps = caps
                        break
                    }
                }
            }
            if (physicalCaps != null) {
                capabilities = physicalCaps
            }
        }

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            _currentNetwork.value = NetworkType.WIFI
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            _currentNetwork.value = NetworkType.MOBILE
        } else {
            _currentNetwork.value = NetworkType.UNKNOWN
        }
    }

    @Synchronized
    fun startMonitoring(context: Context) {
        if (connectivityManager != null) return // Already monitoring
        
        connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        updateNetworkState()

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        
        try {
            connectivityManager?.registerNetworkCallback(request, networkCallback)
        } catch (e: Exception) {
            // Ignore registration error if already registered
        }
    }

    @Synchronized
    fun stopMonitoring() {
        try {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
            // Ignore
        } finally {
            connectivityManager = null
        }
    }
}
