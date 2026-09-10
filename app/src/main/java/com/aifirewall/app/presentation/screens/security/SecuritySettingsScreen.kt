package com.aifirewall.app.presentation.screens.security

import androidx.biometric.BiometricManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.aifirewall.app.R
import com.aifirewall.app.core.security.SecurityManager
import com.aifirewall.app.presentation.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecuritySettingsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val securityManager = remember { SecurityManager.getInstance(context) }
    
    val isLockEnabled by securityManager.isSecurityLockEnabled.collectAsState()
    val isBiometricEnabled by securityManager.isBiometricEnabled.collectAsState()
    val timeoutMinutes by securityManager.timeoutMinutes.collectAsState()
    
    val canUseBiometric = remember {
        val biometricManager = BiometricManager.from(context)
        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

    var showPinSetup by remember { mutableStateOf(false) }

    if (showPinSetup) {
        SecurityLockScreen(
            title = "Create PIN",
            isSetupMode = true,
            onSuccess = { newPin ->
                securityManager.setPin(newPin)
                securityManager.setSecurityLockEnabled(true)
                showPinSetup = false
            },
            onCancel = { showPinSetup = false }
        )
        return
    }

    RequireAuthentication(
        onAuthenticated = {}, // Do nothing, just unlock
        onCancel = { navController.popBackStack() } // Go back if they cancel
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Security Settings") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.SpaceStandard)
                .verticalScroll(scrollState)
        ) {
            
            Text(
                text = "Authentication",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = Dimens.SpaceSmall)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column {
                    // Security Lock Toggle
                    SettingToggleRow(
                        title = "Security Lock",
                        subtitle = "Protect sensitive firewall controls",
                        icon = Icons.Default.Lock,
                        isChecked = isLockEnabled,
                        onToggle = { enabled ->
                            if (enabled) {
                                showPinSetup = true
                            } else {
                                securityManager.setSecurityLockEnabled(false)
                            }
                        }
                    )
                    
                    if (isLockEnabled) {
                        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                        
                        // Change PIN
                        SettingActionRow(
                            title = "Change PIN",
                            subtitle = "Update your security PIN",
                            onClick = { showPinSetup = true }
                        )

                        // Biometric Toggle (only if supported)
                        if (canUseBiometric) {
                            Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                            SettingToggleRow(
                                title = "Biometric Authentication",
                                subtitle = "Use fingerprint or face unlock",
                                icon = null,
                                isChecked = isBiometricEnabled,
                                onToggle = { securityManager.setBiometricEnabled(it) }
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                        
                        // Authentication Timeout
                        TimeoutSelectionRow(
                            currentTimeout = timeoutMinutes,
                            onTimeoutSelected = { securityManager.setTimeoutMinutes(it) }
                        )
                    }
                }
            }
        }
    }
}
}

@Composable
fun SettingToggleRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isChecked) }
            .padding(Dimens.SpaceStandard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = Dimens.SpaceMedium)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { onToggle(it) }
        )
    }
}

@Composable
fun SettingActionRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(Dimens.SpaceStandard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TimeoutSelectionRow(
    currentTimeout: Int,
    onTimeoutSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = mapOf(
        0 to "Immediately",
        1 to "1 minute",
        5 to "5 minutes",
        15 to "15 minutes"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(Dimens.SpaceStandard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = Dimens.SpaceMedium)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Authentication Timeout",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = options[currentTimeout] ?: "Unknown",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (minutes, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onTimeoutSelected(minutes)
                        expanded = false
                    }
                )
            }
        }
    }
}
