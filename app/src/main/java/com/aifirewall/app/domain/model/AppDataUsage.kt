package com.aifirewall.app.domain.model

enum class TimeRange {
    TODAY,
    THIS_WEEK,
    THIS_MONTH
}

data class AppDataUsage(
    val packageName: String,
    val uid: Int,
    val wifiRxBytes: Long = 0L,
    val wifiTxBytes: Long = 0L,
    val mobileRxBytes: Long = 0L,
    val mobileTxBytes: Long = 0L
) {
    val totalWifi: Long
        get() = wifiRxBytes + wifiTxBytes

    val totalMobile: Long
        get() = mobileRxBytes + mobileTxBytes

    val totalBytes: Long
        get() = totalWifi + totalMobile
}
