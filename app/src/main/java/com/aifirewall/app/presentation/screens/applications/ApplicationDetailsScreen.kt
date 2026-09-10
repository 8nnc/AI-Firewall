package com.aifirewall.app.presentation.screens.applications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aifirewall.app.R
import com.aifirewall.app.core.utils.AppIconLoader
import com.aifirewall.app.core.utils.Formatters
import com.aifirewall.app.data.repository.AppRuleRepository
import com.aifirewall.app.data.repository.DataUsageRepository
import com.aifirewall.app.data.repository.InstalledAppsRepository
import com.aifirewall.app.domain.model.InstalledApp
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.domain.model.TimeRange
import com.aifirewall.app.domain.model.FirewallState
import com.aifirewall.app.engine.FirewallStateManager
import com.aifirewall.app.engine.network.NetworkMonitor
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.Dimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationDetailsScreen(
    navController: NavController,
    packageName: String
) {
    val context = LocalContext.current
    var installedApp by remember { mutableStateOf<InstalledApp?>(null) }
    
    LaunchedEffect(packageName) {
        val repo = InstalledAppsRepository(context)
        withContext(Dispatchers.IO) {
            installedApp = repo.getInstalledApps().find { it.packageName == packageName }
        }
    }

    com.aifirewall.app.presentation.screens.security.RequireAuthentication(
        onAuthenticated = {}, // allow access
        onCancel = { navController.popBackStack() } // exit screen if cancelled
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Application Details") },
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
            installedApp?.let { app ->
                val iconState = produceState<ImageBitmap?>(initialValue = null, app.packageName) {
                    value = AppIconLoader.getAppIcon(context, app.packageName)
                }
                
                val ruleRepo = remember { AppRuleRepository(context) }
                val usageRepo = remember { DataUsageRepository(context) }
                val eventRepo = remember { com.aifirewall.app.data.repository.EventRepository(context) }
                val viewModel: ApplicationDetailsViewModel = viewModel(
                    factory = ApplicationDetailsViewModel.Factory(app.packageName, app.uid, ruleRepo, usageRepo, eventRepo)
                )
                val rule by viewModel.networkRule.collectAsState()
                val dataUsage by viewModel.dataUsage.collectAsState()
                val hasUsagePermission by viewModel.hasUsagePermission.collectAsState()
                
                val firewallState by FirewallStateManager.firewallState.collectAsState()
                val currentNetwork by NetworkMonitor.currentNetwork.collectAsState()
                val isWifiActive = currentNetwork == NetworkMonitor.NetworkType.WIFI

                val lifecycleOwner = LocalLifecycleOwner.current
                LaunchedEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            viewModel.loadDataUsage(TimeRange.TODAY)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = iconState.value
                    if (icon != null) {
                        Image(
                            bitmap = icon,
                            contentDescription = app.appName,
                            modifier = Modifier.size(80.dp).clip(CircleShape)
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        )
                    }

                    Column(modifier = Modifier.padding(start = Dimens.SpaceLarge)) {
                        Text(
                            text = app.appName,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (app.isSystemApp) "System Application" else "User Application",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))
                
                // Real Network Rules Configuration
                Text(
                    text = stringResource(id = R.string.internet_access),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.SpaceSmall)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(Dimens.SpaceStandard)) {
                        // Wi-Fi Rule
                        PolicyRow(
                            label = stringResource(id = R.string.wifi),
                            iconRes = R.drawable.ic_wifi,
                            policy = rule?.wifiPolicy ?: NetworkPolicy.ALLOW,
                            onPolicyChange = { viewModel.updateWifiPolicy(it) }
                        )
                        
                        Divider(modifier = Modifier.padding(vertical = Dimens.SpaceSmall), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
                        
                        // Mobile Data Rule
                        PolicyRow(
                            label = stringResource(id = R.string.mobile_data),
                            iconRes = R.drawable.ic_cellular,
                            policy = rule?.mobileDataPolicy ?: NetworkPolicy.ALLOW,
                            onPolicyChange = { viewModel.updateMobilePolicy(it) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))
                
                // Real Enforcement Status
                Text(
                    text = stringResource(id = R.string.enforcement_status),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.SpaceSmall)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                ) {
                    val activePolicy = if (isWifiActive) rule?.wifiPolicy else rule?.mobileDataPolicy
                    val activePolicyDefaulted = activePolicy ?: NetworkPolicy.ALLOW
                    
                    val (statusText, statusColor) = if (firewallState != FirewallState.ACTIVE) {
                        Pair(stringResource(id = R.string.not_active), MaterialTheme.colorScheme.onSurfaceVariant)
                    } else if (activePolicyDefaulted == NetworkPolicy.BLOCK) {
                        Pair(stringResource(id = R.string.enforced), BlockedColor)
                    } else {
                        Pair(stringResource(id = R.string.configured), AllowedColor)
                    }

                    Row(
                        modifier = Modifier.padding(Dimens.SpaceStandard),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = statusColor)
                        Spacer(modifier = Modifier.size(Dimens.SpaceMedium))
                        Column {
                            Text(
                                text = statusText,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                            val transportText = if (isWifiActive) stringResource(id = R.string.wifi) else stringResource(id = R.string.mobile_data)
                            Text(
                                text = "Current Transport: $transportText",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))

                // Real Data Usage Section
                Text(
                    text = stringResource(id = R.string.nav_data_usage),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.SpaceSmall)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(Dimens.SpaceStandard)) {
                        Text(
                            text = stringResource(id = R.string.time_today),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = Dimens.SpaceSmall)
                        )

                        if (!hasUsagePermission) {
                            Text(
                                text = stringResource(id = R.string.data_unavailable),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            val wifiBytes = dataUsage?.totalWifi ?: 0L
                            val mobileBytes = dataUsage?.totalMobile ?: 0L
                            
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.SpaceSmall),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_wifi),
                                        contentDescription = "Wi-Fi",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = stringResource(id = R.string.wifi),
                                        modifier = Modifier.padding(start = Dimens.SpaceMedium),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = Formatters.formatBytes(wifiBytes),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Divider(modifier = Modifier.padding(vertical = Dimens.SpaceSmall), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.SpaceSmall),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_cellular),
                                        contentDescription = "Mobile Data",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = stringResource(id = R.string.mobile_data),
                                        modifier = Modifier.padding(start = Dimens.SpaceMedium),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = Formatters.formatBytes(mobileBytes),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Divider(modifier = Modifier.padding(vertical = Dimens.SpaceSmall), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.SpaceSmall),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.total_usage),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = Formatters.formatBytes(wifiBytes + mobileBytes),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))

                DetailItem(label = "Package Name", value = app.packageName)
                DetailItem(label = "UID", value = app.uid.toString())
                DetailItem(label = "Version", value = app.versionName ?: "Unknown")
                DetailItem(label = "Status", value = if (app.isEnabled) "Enabled" else "Disabled")
                
                Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))

                Text(
                    text = "Recent Network Activity",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.SpaceSmall)
                )

                val recentEvents by viewModel.recentEvents.collectAsState()
                
                if (recentEvents.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "No recent blocked activity",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(Dimens.SpaceStandard)
                        )
                    }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column {
                            recentEvents.take(5).forEachIndexed { index, event ->
                                com.aifirewall.app.presentation.screens.activity.EventRow(event = event, onClick = {})
                                if (index < minOf(recentEvents.size, 5) - 1) {
                                    Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))
            }
        }
    }
}
}

@Composable
fun PolicyRow(
    label: String,
    iconRes: Int,
    policy: NetworkPolicy,
    onPolicyChange: (NetworkPolicy) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpaceSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            modifier = Modifier
                .padding(start = Dimens.SpaceMedium)
                .weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Row {
            PolicyToggleButton(
                text = stringResource(id = R.string.allowed),
                icon = Icons.Default.CheckCircle,
                isSelected = policy == NetworkPolicy.ALLOW,
                activeColor = AllowedColor,
                onClick = { onPolicyChange(NetworkPolicy.ALLOW) }
            )
            Spacer(modifier = Modifier.size(Dimens.SpaceSmall))
            PolicyToggleButton(
                text = stringResource(id = R.string.configured_to_block),
                icon = Icons.Default.Clear,
                isSelected = policy == NetworkPolicy.BLOCK,
                activeColor = BlockedColor,
                onClick = { onPolicyChange(NetworkPolicy.BLOCK) }
            )
        }
    }
}

@Composable
fun PolicyToggleButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) activeColor.copy(alpha = 0.15f) else Color.Transparent,
        border = if (isSelected) BorderStroke(1.dp, activeColor.copy(alpha = 0.4f)) else null,
        contentColor = if (isSelected) activeColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.SpaceMedium, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = text, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.SpaceMedium),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(Dimens.SpaceStandard)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
