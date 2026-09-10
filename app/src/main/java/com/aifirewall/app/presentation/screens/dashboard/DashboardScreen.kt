package com.aifirewall.app.presentation.screens.dashboard

import android.app.Activity
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aifirewall.app.R
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.domain.model.FirewallState
import com.aifirewall.app.engine.network.NetworkMonitor
import com.aifirewall.app.presentation.components.EnterpriseProtectionCenterpiece
import com.aifirewall.app.presentation.navigation.Destinations
import com.aifirewall.app.presentation.screens.activity.EventDetailsDialog
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import com.aifirewall.app.presentation.theme.ErrorColor
import com.aifirewall.app.presentation.theme.SuccessColor
import com.aifirewall.app.presentation.theme.WarningColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val viewModel: DashboardViewModel = viewModel()
    val context = LocalContext.current
    val firewallState by viewModel.firewallState.collectAsState()
    val blockedAppsCount by viewModel.blockedAppsCount.collectAsState()
    val blockedTodayCount by viewModel.blockedTodayCount.collectAsState()
    val configuredRulesCount by viewModel.configuredRulesCount.collectAsState()
    val currentNetwork by viewModel.currentNetwork.collectAsState()
    val recentEvents by viewModel.recentEvents.collectAsState()

    var selectedEvent by remember { mutableStateOf<FirewallEventEntity?>(null) }

    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.startVpnService(context)
        } else {
            Toast.makeText(context, R.string.vpn_permission_required, Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        val vpnIntent = try {
            VpnService.prepare(context)
        } catch (e: Exception) {
            null
        }
        if (vpnIntent != null) {
            vpnPermissionLauncher.launch(vpnIntent)
        } else {
            viewModel.startVpnService(context)
        }
    }

    val securityManager = remember { com.aifirewall.app.core.security.SecurityManager.getInstance(context) }

    val handleToggle: () -> Unit = {
        if (firewallState == FirewallState.INACTIVE || firewallState == FirewallState.ERROR) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                val vpnIntent = try {
                    VpnService.prepare(context)
                } catch (e: Exception) {
                    null
                }
                if (vpnIntent != null) {
                    vpnPermissionLauncher.launch(vpnIntent)
                } else {
                    viewModel.startVpnService(context)
                }
            }
        } else if (firewallState == FirewallState.ACTIVE) {
            viewModel.stopVpnService(context)
        }
    }

    var showAuthForToggle by remember { mutableStateOf(false) }

    if (showAuthForToggle) {
        com.aifirewall.app.presentation.screens.security.RequireAuthentication(
            onAuthenticated = {
                showAuthForToggle = false
                handleToggle()
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.enterprise_tier),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            letterSpacing = 0.5.sp
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("diagnostics") }) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = stringResource(R.string.diagnostics_title),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { navController.navigate("security_settings") }) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.settings_security),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { navController.navigate(Destinations.SETTINGS) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.nav_settings),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
                .verticalScroll(scrollState)
                .padding(horizontal = Dimens.SpaceStandard)
                .padding(bottom = Dimens.SpaceExtraLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceLarge)
        ) {
            // 1. Primary Protection Status Centerpiece
            EnterpriseProtectionCenterpiece(
                state = firewallState,
                onToggle = {
                    if (securityManager.isLocked()) {
                        showAuthForToggle = true
                    } else {
                        handleToggle()
                    }
                }
            )

            // 2. Real Security Metrics Row
            SectionHeader(title = stringResource(id = R.string.protection_metrics))
            MetricsSummaryRow(
                blockedToday = blockedTodayCount,
                enforcedApps = blockedAppsCount,
                configuredRules = configuredRulesCount
            )

            // 3. Security Telemetry Overview Grid (2x2)
            SectionHeader(title = stringResource(id = R.string.security_telemetry))
            TelemetryGrid(
                firewallState = firewallState,
                networkType = currentNetwork,
                blockedAppsCount = blockedAppsCount
            )

            // 4. Application Protection Quick Hub Card
            ApplicationProtectionCard(
                configuredCount = configuredRulesCount,
                onClick = { navController.navigate(Destinations.APPLICATIONS) }
            )

            // 5. Recent Security Activity Feed
            RecentActivitySection(
                events = recentEvents,
                onViewAll = { navController.navigate(Destinations.ACTIVITY) },
                onSelectEvent = { selectedEvent = it }
            )

            // 6. Diagnostics Quick Banner
            DiagnosticsBanner(
                onClick = { navController.navigate("diagnostics") }
            )
        }
    }

    selectedEvent?.let { event ->
        EventDetailsDialog(event = event) {
            selectedEvent = null
        }
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        modifier = modifier.padding(top = Dimens.SpaceSmall)
    )
}

@Composable
private fun MetricsSummaryRow(
    blockedToday: Int,
    enforcedApps: Int,
    configuredRules: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MetricCard(
            title = stringResource(id = R.string.blocked_connections),
            value = blockedToday.toString(),
            valueColor = if (blockedToday > 0) BlockedColor else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            title = stringResource(id = R.string.applications_blocked),
            value = enforcedApps.toString(),
            valueColor = if (enforcedApps > 0) SuccessColor else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            title = stringResource(id = R.string.configured_rules_count),
            value = configuredRules.toString(),
            valueColor = CyanGlow,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = valueColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TelemetryGrid(
    firewallState: FirewallState,
    networkType: NetworkMonitor.NetworkType,
    blockedAppsCount: Int
) {
    val fwActive = firewallState == FirewallState.ACTIVE
    val fwColor = when (firewallState) {
        FirewallState.ACTIVE -> SuccessColor
        FirewallState.STARTING -> CyanGlow
        FirewallState.STOPPING -> WarningColor
        FirewallState.ERROR -> ErrorColor
        FirewallState.INACTIVE -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Tile 1: Firewall Core
            TelemetryTile(
                name = stringResource(R.string.firewall_protection),
                status = when (firewallState) {
                    FirewallState.ACTIVE -> stringResource(R.string.status_active)
                    FirewallState.STARTING -> stringResource(R.string.status_starting)
                    FirewallState.STOPPING -> stringResource(R.string.status_stopping)
                    FirewallState.ERROR -> stringResource(R.string.status_error)
                    FirewallState.INACTIVE -> stringResource(R.string.status_inactive)
                },
                subtext = stringResource(R.string.traffic_inspected),
                statusColor = fwColor,
                icon = Icons.Default.CheckCircle,
                modifier = Modifier.weight(1f)
            )

            // Tile 2: VPN Virtual Tunnel
            TelemetryTile(
                name = stringResource(R.string.vpn_tunnel),
                status = if (fwActive) stringResource(R.string.vpn_active) else stringResource(R.string.vpn_standby),
                subtext = "TUN: 10.1.10.1",
                statusColor = if (fwActive) SuccessColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                icon = Icons.Default.Lock,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Tile 3: Network Physical Uplink
            val (netStatus, netColor) = when (networkType) {
                NetworkMonitor.NetworkType.WIFI -> stringResource(R.string.network_wifi) to SuccessColor
                NetworkMonitor.NetworkType.MOBILE -> stringResource(R.string.network_cellular) to CyanGlow
                NetworkMonitor.NetworkType.NONE -> stringResource(R.string.network_disconnected) to ErrorColor
                NetworkMonitor.NetworkType.UNKNOWN -> stringResource(R.string.network_unknown) to MaterialTheme.colorScheme.onSurfaceVariant
            }
            TelemetryTile(
                name = stringResource(R.string.network_status),
                status = netStatus,
                subtext = if (networkType == NetworkMonitor.NetworkType.NONE) "No Internet" else "Physical Transport",
                statusColor = netColor,
                icon = Icons.Default.Info,
                modifier = Modifier.weight(1f)
            )

            // Tile 4: Policy Engine Status
            val isEnforcing = fwActive && blockedAppsCount > 0
            TelemetryTile(
                name = stringResource(R.string.policy_engine),
                status = if (isEnforcing) stringResource(R.string.policy_enforcing) else stringResource(R.string.policy_standby),
                subtext = if (isEnforcing) "$blockedAppsCount rules locked" else "Standard pass-through",
                statusColor = if (isEnforcing) CyanGlow else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                icon = Icons.Default.Build,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TelemetryTile(
    name: String,
    status: String,
    subtext: String,
    statusColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ApplicationProtectionCard(
    configuredCount: Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceStandard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CyanGlow.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, CyanGlow.copy(alpha = 0.25f)),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        tint = CyanGlow,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Dimens.SpaceStandard))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.app_protection_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(id = R.string.app_protection_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(Dimens.SpaceSmall))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = stringResource(R.string.manage_apps),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun RecentActivitySection(
    events: List<FirewallEventEntity>,
    onViewAll: () -> Unit,
    onSelectEvent: (FirewallEventEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(title = stringResource(id = R.string.recent_activity))
            TextButton(onClick = onViewAll) {
                Text(
                    text = stringResource(id = R.string.view_all_activity),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (events.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.SpaceLarge, horizontal = Dimens.SpaceStandard),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = SuccessColor.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpaceSmall))
                    Text(
                        text = stringResource(R.string.no_recent_activity),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.no_recent_activity_desc),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                events.forEach { event ->
                    RecentEventItem(event = event, onClick = { onSelectEvent(event) })
                }
            }
        }
    }
}

@Composable
private fun RecentEventItem(
    event: FirewallEventEntity,
    onClick: () -> Unit
) {
    val isBlocked = event.action == "BLOCK"
    val badgeColor = if (isBlocked) BlockedColor else AllowedColor
    val timeFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpaceStandard, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = badgeColor.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.3f)),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isBlocked) Icons.Default.Clear else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Dimens.SpaceMedium))

            Column(modifier = Modifier.weight(1f)) {
                val appLabel = event.packageName.substringAfterLast(".")
                Text(
                    text = appLabel,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isBlocked) stringResource(R.string.blocked) else stringResource(R.string.allowed),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = badgeColor
                    )
                    Text(
                        text = " • ${event.transport} • ${event.protocol}:${event.destinationPort}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(Dimens.SpaceSmall))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = timeFormat.format(Date(event.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                if (event.attempts > 1) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeColor.copy(alpha = 0.15f),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "${event.attempts}x",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagnosticsBanner(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceStandard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Dimens.SpaceMedium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.diagnostics_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(id = R.string.diagnostics_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(Dimens.SpaceSmall))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
