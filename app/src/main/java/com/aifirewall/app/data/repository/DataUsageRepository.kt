package com.aifirewall.app.data.repository

import android.app.AppOpsManager
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Process
import com.aifirewall.app.domain.model.AppDataUsage
import com.aifirewall.app.domain.model.TimeRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class DataUsageRepository(private val context: Context) {

    private val networkStatsManager = context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
    private val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

    fun hasUsagePermission(): Boolean {
        val mode = appOpsManager.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    suspend fun getUsageForTimeRange(timeRange: TimeRange): List<AppDataUsage> = withContext(Dispatchers.IO) {
        if (!hasUsagePermission()) return@withContext emptyList()

        val (startTime, endTime) = getTimeBounds(timeRange)
        val usageMap = mutableMapOf<Int, AppDataUsage>()

        // Helper to query and aggregate
        fun queryAndAggregate(networkType: Int, isWifi: Boolean) {
            try {
                // networkType mapped to ConnectivityManager constants for NetworkStatsManager
                // TYPE_WIFI = 1, TYPE_MOBILE = 0
                val bucket = NetworkStats.Bucket()
                val stats = networkStatsManager.querySummary(networkType, null, startTime, endTime)
                
                while (stats.hasNextBucket()) {
                    stats.getNextBucket(bucket)
                    val uid = bucket.uid
                    // Skip system root operations or non-app traffic if needed, but here we collect everything
                    if (uid < 0) continue

                    val currentUsage = usageMap[uid] ?: AppDataUsage(packageName = "", uid = uid)
                    
                    val updatedUsage = if (isWifi) {
                        currentUsage.copy(
                            wifiRxBytes = currentUsage.wifiRxBytes + bucket.rxBytes,
                            wifiTxBytes = currentUsage.wifiTxBytes + bucket.txBytes
                        )
                    } else {
                        currentUsage.copy(
                            mobileRxBytes = currentUsage.mobileRxBytes + bucket.rxBytes,
                            mobileTxBytes = currentUsage.mobileTxBytes + bucket.txBytes
                        )
                    }
                    usageMap[uid] = updatedUsage
                }
                stats.close()
            } catch (e: Exception) {
                // Ignore instances where network stats throws RemoteException or SecurityException
            }
        }

        // Query Wi-Fi
        queryAndAggregate(ConnectivityManager.TYPE_WIFI, isWifi = true)
        
        // Query Mobile Data
        queryAndAggregate(ConnectivityManager.TYPE_MOBILE, isWifi = false)

        usageMap.values.toList()
    }

    private fun getTimeBounds(timeRange: TimeRange): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis

        when (timeRange) {
            TimeRange.TODAY -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            TimeRange.THIS_WEEK -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            TimeRange.THIS_MONTH -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
        }
        val startTime = calendar.timeInMillis
        return Pair(startTime, endTime)
    }
}
