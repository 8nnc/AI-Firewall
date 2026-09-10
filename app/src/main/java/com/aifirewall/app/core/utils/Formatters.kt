package com.aifirewall.app.core.utils

import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

object Formatters {
    fun formatBytes(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(bytes.toDouble()) / log10(1024.0)).toInt()
        val size = bytes / 1024.0.pow(digitGroups.toDouble())
        
        // If it's just Bytes, don't show decimals
        return if (digitGroups == 0) {
            String.format(Locale.getDefault(), "%.0f %s", size, units[digitGroups])
        } else {
            String.format(Locale.getDefault(), "%.2f %s", size, units[digitGroups])
        }
    }
}
