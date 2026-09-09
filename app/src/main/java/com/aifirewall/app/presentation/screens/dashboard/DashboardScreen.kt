package com.aifirewall.app.presentation.screens.dashboard

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aifirewall.app.R
import com.aifirewall.app.domain.model.FirewallState
import com.aifirewall.app.presentation.components.FirewallStatus
import com.aifirewall.app.presentation.components.FirewallStatusCard
import com.aifirewall.app.presentation.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: androidx.navigation.NavController) {
    val viewModel: DashboardViewModel = viewModel()
    val context = LocalContext.current
    val firewallState by viewModel.firewallState.collectAsState()

    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Permission granted
            viewModel.startVpnService(context)
        } else {
            Toast.makeText(context, R.string.vpn_permission_required, Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Proceed whether granted or denied. If denied, Android 13+ handles FGS silently.
        val vpnIntent = VpnService.prepare(context)
        if (vpnIntent != null) {
            vpnPermissionLauncher.launch(vpnIntent)
        } else {
            viewModel.startVpnService(context)
        }
    }

    var showAuthForToggle by remember { mutableStateOf(false) }

    if (showAuthForToggle) {
        com.aifirewall.app.presentation.screens.security.RequireAuthentication(
            onAuthenticated = {
                showAuthForToggle = false
                if (firewallState == FirewallState.INACTIVE || firewallState == FirewallState.ERROR) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        val vpnIntent = VpnService.prepare(context)
                        if (vpnIntent != null) {
                            vpnPermissionLauncher.launch(vpnIntent)
                        } else {
                            viewModel.startVpnService(context)
                        }
                    }
                } else {
                    viewModel.stopVpnService(context)
                }
            },
            onCancel = { showAuthForToggle = false }
        ) {
            // empty content for overlay
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Control Center",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.SpaceStandard),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceLarge)
        ) {
            val status = when (firewallState) {
                FirewallState.ACTIVE -> FirewallStatus.ACTIVE
                FirewallState.STARTING, FirewallState.STOPPING -> FirewallStatus.STARTING
                FirewallState.ERROR -> FirewallStatus.ERROR
                else -> FirewallStatus.INACTIVE
            }

            val statusText = when (firewallState) {
                FirewallState.ACTIVE -> stringResource(id = R.string.firewall_active)
                FirewallState.STARTING -> stringResource(id = R.string.firewall_starting)
                FirewallState.STOPPING -> "Stopping..."
                FirewallState.ERROR -> stringResource(id = R.string.firewall_error)
                else -> stringResource(id = R.string.firewall_inactive)
            }

            FirewallStatusCard(
                status = status,
                statusText = statusText,
                onToggle = { showAuthForToggle = true }
            )
            ProtectionSummarySection(viewModel = viewModel)
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Diagnostics Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                onClick = { navController.navigate("diagnostics") }
            ) {
                Row(
                    modifier = Modifier.padding(Dimens.SpaceStandard),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Diagnostics",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = Dimens.SpaceMedium)
                    )
                    Column {
                        Text(
                            text = "Firewall Diagnostics",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Run tests and verify health",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProtectionSummarySection(viewModel: DashboardViewModel) {
    val blockedAppsCount by viewModel.blockedAppsCount.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.firewall_protection),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = Dimens.SpaceMedium)
        )
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpaceMedium),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.applications_blocked),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = blockedAppsCount.toString(),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    val blockedToday by viewModel.blockedTodayCount.collectAsState()
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Blocked Today",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = blockedToday.toString(),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = com.aifirewall.app.presentation.theme.BlockedColor
                        )
                    }
                }
            }
        }
    }
}
