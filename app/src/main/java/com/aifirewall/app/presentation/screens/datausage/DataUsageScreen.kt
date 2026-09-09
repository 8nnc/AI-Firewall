package com.aifirewall.app.presentation.screens.datausage

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.aifirewall.app.presentation.theme.Dimens

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
                    Text(
                        text = stringResource(id = R.string.nav_data_usage),
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
        ) {
            when (val state = uiState) {
                is DataUsageUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
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
                            }
                        ) {
                            Text(text = stringResource(id = R.string.grant_permission))
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
                    TabRow(
                        selectedTabIndex = tabs.indexOfFirst { it.first == state.timeRange },
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        tabs.forEachIndexed { index, tabInfo ->
                            Tab(
                                selected = tabs.indexOfFirst { it.first == state.timeRange } == index,
                                onClick = { viewModel.setTimeRange(tabInfo.first) },
                                text = { Text(text = stringResource(id = tabInfo.second)) }
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = Dimens.SpaceExtraLarge)
                    ) {
                        // Total Overview Header
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimens.SpaceStandard),
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Column(modifier = Modifier.padding(Dimens.SpaceLarge)) {
                                    Text(
                                        text = stringResource(id = R.string.total_usage),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(Dimens.SpaceSmall))
                                    Text(
                                        text = Formatters.formatBytes(state.totalWifi + state.totalMobile),
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Divider(modifier = Modifier.padding(vertical = Dimens.SpaceMedium), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Share, contentDescription = "Wi-Fi", tint = MaterialTheme.colorScheme.primary)
                                            Spacer(modifier = Modifier.size(Dimens.SpaceSmall))
                                            Text(
                                                text = Formatters.formatBytes(state.totalWifi),
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Build, contentDescription = "Mobile Data", tint = MaterialTheme.colorScheme.secondary)
                                            Spacer(modifier = Modifier.size(Dimens.SpaceSmall))
                                            Text(
                                                text = Formatters.formatBytes(state.totalMobile),
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Top Applications List
                        item {
                            Text(
                                text = stringResource(id = R.string.top_applications),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = Dimens.SpaceStandard, vertical = Dimens.SpaceMedium),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        if (state.topApps.isEmpty()) {
                            item {
                                EmptyStateView(
                                    message = stringResource(id = R.string.data_unavailable),
                                    modifier = Modifier.padding(top = Dimens.SpaceExtraLarge)
                                )
                            }
                        } else {
                            items(state.topApps, key = { it.app.packageName }) { record ->
                                val iconState = produceState<ImageBitmap?>(initialValue = null, record.app.packageName) {
                                    value = AppIconLoader.getAppIcon(context, record.app.packageName)
                                }
                                
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Dimens.SpaceStandard, vertical = 4.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    color = MaterialTheme.colorScheme.surface
                                ) {
                                    Row(
                                        modifier = Modifier.padding(Dimens.SpaceStandard),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val icon = iconState.value
                                        if (icon != null) {
                                            Image(
                                                bitmap = icon,
                                                contentDescription = record.app.appName,
                                                modifier = Modifier.size(40.dp).clip(CircleShape)
                                            )
                                        } else {
                                            Spacer(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                            )
                                        }

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = Dimens.SpaceMedium)
                                        ) {
                                            Text(
                                                text = record.app.appName,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = Formatters.formatBytes(record.usage.totalBytes),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.primary
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
