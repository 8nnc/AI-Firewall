package com.aifirewall.app.presentation.screens.security

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.aifirewall.app.core.security.SecurityManager

@Composable
fun RequireAuthentication(
    onAuthenticated: () -> Unit,
    onCancel: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val securityManager = remember { SecurityManager.getInstance(context) }
    val isLocked = securityManager.isLocked()

    var showAuth by remember { mutableStateOf(isLocked) }

    LaunchedEffect(isLocked) {
        if (!isLocked) {
            showAuth = false
            onAuthenticated()
        } else {
            showAuth = true
        }
    }

    if (showAuth) {
        SecurityLockScreen(
            onSuccess = {
                securityManager.unlockSession()
                showAuth = false
                onAuthenticated()
            },
            onCancel = {
                showAuth = false
                onCancel()
            }
        )
    } else {
        content()
    }
}
