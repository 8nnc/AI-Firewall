package com.aifirewall.app.presentation.screens.datausage

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aifirewall.app.R
import com.aifirewall.app.core.utils.AppIconLoader
import com.aifirewall.app.core.utils.Formatters
import com.aifirewall.app.data.repository.DataUsageRepository
import com.aifirewall.app.data.repository.InstalledAppsRepository
import com.aifirewall.app.domain.model.TimeRange
import com.aifirewall.app.presentation.components.EmptyStateView
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import com.aifirewall.app.presentation.theme.PurpleAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataUsageScreen() {
    val context = LocalContext.current
    val usageRepo = remember { DataUsageRepository(context) }
    val appRepo = remember { InstalledAppsRepository(context) }

    val viewModel: DataUsageViewModel = viewModel(
        factory = DataUsageViewModel.Factory(usageRepo, appRepo)
    )

    val uiState by viewModel.uiState.collectAsState()

    // Refresh data when returning from settings
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    val tabs = listOf(
        TimeRange.TODAY to R.string.time_today,
        TimeRange.THIS_WEEK to R.string.time_this_week,
        TimeRange.THIS_MONTH to R.string.time_this_month
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.nav_data_usage),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = stringResource(R.string.network_traffic),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is DataUsageUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
                        Text(
                            text = stringResource(R.string.splash_check_network),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                is DataUsageUiState.PermissionRequired -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.SpaceExtraLarge),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
                        Text(
                            text = stringResource(id = R.string.permission_required),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
                        Text(
                            text = stringResource(id = R.string.usage_permission_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))
                        Button(
                            onClick = {
                                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.grant_permission),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                is DataUsageUiState.Error -> {
                    EmptyStateView(
                        message = state.message,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is DataUsageUiState.Success -> {
                    // Time Range Selector Segment
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.SpaceStandard, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tabs.forEach { tabInfo ->
                            val isSelected = tabInfo.first == state.timeRange
                            Surface(
                                onClick = { viewModel.setTimeRange(tabInfo.first) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = tabInfo.second),
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 12.sp
                                        ),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    val totalBytes = state.totalWifi + state.totalMobile
                    val wifiRatio = if (totalBytes > 0) (state.totalWifi.toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f) else 0f
                    val mobileRatio = if (totalBytes > 0) (state.totalMobile.toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f) else 0f

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = Dimens.SpaceExtraLarge)
                    ) {
                        // Executive Telemetry Overview Card
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimens.SpaceStandard),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)),
                                tonalElevation = 1.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    // Header label
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.total_usage).uppercase(),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            ),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = CyanGlow.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.status_protected),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 0.5.sp
                                                ),
                                                color = CyanGlow,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Big Total Bytes Counter
                                    Text(
                                        text = Formatters.formatBytes(totalBytes),
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 32.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Traffic Distribution Proportional Bar
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(R.string.traffic_distribution),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            val wifiPercent = (wifiRatio * 100).toInt()
                                            val mobilePercent = 100 - wifiPercent
                                            Text(
                                                text = "$wifiPercent% Wi-Fi  •  $mobilePercent% Cellular",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Segmented colored track bar
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(8.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                        ) {
                                            Row(modifier = Modifier.fillMaxSize()) {
                                                if (wifiRatio > 0f) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(wifiRatio.coerceAtLeast(0.01f))
                                                            .background(CyanGlow)
                                                    )
                                                }
                                                if (mobileRatio > 0f) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .weight(mobileRatio.coerceAtLeast(0.01f))
                                                            .background(PurpleAccent)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Dual Transport Metrics Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Wi-Fi metric tile
                                        TransportMetricTile(
                                            iconRes = R.drawable.ic_wifi,
                                            label = stringResource(id = R.string.wifi_traffic),
                                            value = Formatters.formatBytes(state.totalWifi),
                                            accentColor = CyanGlow,
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Cellular metric tile
                                        TransportMetricTile(
                                            iconRes = R.drawable.ic_cellular,
                                            label = stringResource(id = R.string.cellular_traffic),
                                            value = Formatters.formatBytes(state.totalMobile),
                                            accentColor = PurpleAccent,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        // Top Applications Section Header
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Dimens.SpaceStandard, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.top_applications).uppercase(),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = stringResource(R.string.active_consumers_count, state.topApps.size),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        if (state.topApps.isEmpty()) {
                            item {
                                EmptyStateView(
                                    message = stringResource(id = R.string.data_unavailable),
                                    modifier = Modifier.padding(top = Dimens.SpaceExtraLarge)
                                )
                            }
                        } else {
                            val maxUsage = state.topApps.maxOfOrNull { it.usage.totalBytes } ?: 1L

                            itemsIndexed(state.topApps, key = { _, record -> record.app.packageName }) { index, record ->
                                val iconState = produceState<ImageBitmap?>(initialValue = null, record.app.packageName) {
                                    value = AppIconLoader.getAppIcon(context, record.app.packageName)
                                }

                                val usageFraction = if (maxUsage > 0L) {
                                    (record.usage.totalBytes.toFloat() / maxUsage.toFloat()).coerceIn(0f, 1f)
                                } else 0f

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Dimens.SpaceStandard, vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
                                    tonalElevation = 1.dp
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Rank Pill Badge
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = if (index < 3) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = "${index + 1}",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 11.sp
                                                        ),
                                                        color = if (index < 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(10.dp))

                                            // App Icon
                                            val icon = iconState.value
                                            if (icon != null) {
                                                Image(
                                                    bitmap = icon,
                                                    contentDescription = record.app.appName,
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                )
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = record.app.appName.take(1).uppercase(),
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            // App Name & Package
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(horizontal = 10.dp)
                                            ) {
                                                Text(
                                                    text = record.app.appName,
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 14.sp
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = record.app.packageName,
                                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            // Total Bytes
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text(
                                                    text = Formatters.formatBytes(record.usage.totalBytes),
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                    ),
                                                    color = CyanGlow
                                                )
                                                Text(
                                                    text = "${(usageFraction * 100).toInt()}%",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Progress Track
                                        LinearProgressIndicator(
                                            progress = { usageFraction },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp)),
                                            color = CyanGlow,
                                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Per-app Wi-Fi & Cellular breakdown
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_wifi),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = Formatters.formatBytes(record.usage.totalWifi),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_cellular),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = Formatters.formatBytes(record.usage.totalMobile),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransportMetricTile(
    iconRes: Int,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = accentColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
