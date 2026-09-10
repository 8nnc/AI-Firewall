package com.aifirewall.app.domain.model

enum class NetworkPolicy {
    ALLOW,
    BLOCK,
    UNSET
}

data class AppNetworkRule(
    val packageName: String,
    val uid: Int,
    val wifiPolicy: NetworkPolicy = NetworkPolicy.ALLOW,
    val mobileDataPolicy: NetworkPolicy = NetworkPolicy.ALLOW
) {
    val isWifiBlocked: Boolean
        get() = wifiPolicy == NetworkPolicy.BLOCK

    val isMobileBlocked: Boolean
        get() = mobileDataPolicy == NetworkPolicy.BLOCK

    val isCompletelyBlocked: Boolean
        get() = isWifiBlocked && isMobileBlocked

    companion object {
        const val UNKNOWN_UID = -1
        const val UNKNOWN_PACKAGE = "UNKNOWN_APPLICATION"
    }
}
