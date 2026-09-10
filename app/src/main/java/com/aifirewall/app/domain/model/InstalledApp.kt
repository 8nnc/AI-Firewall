package com.aifirewall.app.domain.model

data class InstalledApp(
    val packageName: String,
    val uid: Int,
    val appName: String,
    val isSystemApp: Boolean,
    val isEnabled: Boolean,
    val versionName: String?
)
