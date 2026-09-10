package com.aifirewall.app.presentation.screens.applications

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aifirewall.app.R
import com.aifirewall.app.core.utils.AppIconLoader
import com.aifirewall.app.core.utils.Formatters
import com.aifirewall.app.data.repository.AppRuleRepository
import com.aifirewall.app.data.repository.DataUsageRepository
import com.aifirewall.app.data.repository.InstalledAppsRepository
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.presentation.components.AppNetworkState
import com.aifirewall.app.presentation.components.ApplicationCard
import com.aifirewall.app.presentation.components.EmptyStateView
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import com.aifirewall.app.presentation.theme.WarningColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val appRepo = remember { InstalledAppsRepository(context) }
    val ruleRepo = remember { AppRuleRepository(context) }
    val usageRepo = remember { DataUsageRepository(context) }

    val viewModel: ApplicationsViewModel = viewModel(
        factory = ApplicationsViewModel.Factory(appRepo, ruleRepo, usageRepo)
    )
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()
    val typeFilter by viewModel.typeFilter.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val selectedPackages by viewModel.selectedPackages.collectAsState()

    val isSelectionMode = selectedPackages.isNotEmpty()
    var showSortMenu by remember { mutableStateOf(false) }

    BackHandler(enabled = isSelectionMode) {
        viewModel.clearSelection()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSelectionMode) {
                        Text(
                            text = stringResource(R.string.selected_count, selectedPackages.size),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    } else {
                        Column {
                            Text(
                                text = stringResource(id = R.string.nav_applications),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            if (uiState is ApplicationsUiState.Success) {
                                val apps = (uiState as ApplicationsUiState.Success).apps
                                val blockedCount = apps.count { it.rule.isCompletelyBlocked || it.rule.isWifiBlocked || it.rule.isMobileBlocked }
                                Text(
                                    text = "${apps.size} installed • $blockedCount rules",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = { viewModel.clearSelection() }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.deselect_all)
                            )
                        }
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        if (uiState is ApplicationsUiState.Success) {
                            val apps = (uiState as ApplicationsUiState.Success).apps
                            val allSelected = apps.isNotEmpty() && selectedPackages.size == apps.size
                            TextButton(
                                onClick = {
                                    if (allSelected) {
                                        viewModel.clearSelection()
                                    } else {
                                        viewModel.selectAll(apps)
                                    }
                                }
                            ) {
                                Text(
                                    text = if (allSelected) stringResource(R.string.deselect_all) else stringResource(R.string.select_all),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    } else {
                        // Multi-select toggle button
                        IconButton(
                            onClick = {
                                if (uiState is ApplicationsUiState.Success) {
                                    val firstApp = (uiState as ApplicationsUiState.Success).apps.firstOrNull()?.app?.packageName
                                    if (firstApp != null) {
                                        viewModel.toggleSelection(firstApp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Selection Mode",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Sort Menu Button
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_shield_check),
                                    contentDescription = stringResource(R.string.sort_by),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.sort_name_asc),
                                            fontWeight = if (sortOption == AppSortOption.NAME_ASC) FontWeight.Bold else FontWeight.Normal,
                                            color = if (sortOption == AppSortOption.NAME_ASC) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        viewModel.updateSortOption(AppSortOption.NAME_ASC)
                                        showSortMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.sort_name_desc),
                                            fontWeight = if (sortOption == AppSortOption.NAME_DESC) FontWeight.Bold else FontWeight.Normal,
                                            color = if (sortOption == AppSortOption.NAME_DESC) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        viewModel.updateSortOption(AppSortOption.NAME_DESC)
                                        showSortMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.sort_usage_desc),
                                            fontWeight = if (sortOption == AppSortOption.DATA_USAGE_DESC) FontWeight.Bold else FontWeight.Normal,
                                            color = if (sortOption == AppSortOption.DATA_USAGE_DESC) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        viewModel.updateSortOption(AppSortOption.DATA_USAGE_DESC)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isSelectionMode,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.SpaceStandard, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.applyBulkPolicy(NetworkPolicy.BLOCK, NetworkPolicy.ALLOW) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = WarningColor
                                ),
                                border = BorderStroke(1.dp, WarningColor.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = stringResource(R.string.block_wifi),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            OutlinedButton(
                                onClick = { viewModel.applyBulkPolicy(NetworkPolicy.ALLOW, NetworkPolicy.BLOCK) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = WarningColor
                                ),
                                border = BorderStroke(1.dp, WarningColor.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = stringResource(R.string.block_mobile),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.applyBulkPolicy(NetworkPolicy.ALLOW, NetworkPolicy.ALLOW) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AllowedColor.copy(alpha = 0.2f),
                                    contentColor = AllowedColor
                                ),
                                border = BorderStroke(1.dp, AllowedColor.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = stringResource(R.string.allow_all),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = { viewModel.applyBulkPolicy(NetworkPolicy.BLOCK, NetworkPolicy.BLOCK) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BlockedColor.copy(alpha = 0.2f),
                                    contentColor = BlockedColor
                                ),
                                border = BorderStroke(1.dp, BlockedColor.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = stringResource(R.string.block_both),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!isSelectionMode) {
                // Search Input Field
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpaceStandard, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.search_hint),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                        )
                    )
                }

                // Type Filter Chips (All, User, System)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = Dimens.SpaceStandard, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppTypeFilter.values().forEach { filter ->
                        val label = when (filter) {
                            AppTypeFilter.ALL -> stringResource(R.string.filter_type_all)
                            AppTypeFilter.USER -> stringResource(R.string.filter_type_user)
                            AppTypeFilter.SYSTEM -> stringResource(R.string.filter_type_system)
                        }
                        val isSelected = typeFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updateTypeFilter(filter) },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                selectedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                // Status Filter Chips (All, Allowed, Blocked, Wi-Fi Blocked, Mobile Blocked)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = Dimens.SpaceStandard, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppStatusFilter.values().forEach { filter ->
                        val labelRes = when (filter) {
                            AppStatusFilter.ALL -> R.string.filter_all
                            AppStatusFilter.ALLOWED -> R.string.filter_allowed
                            AppStatusFilter.BLOCKED -> R.string.filter_blocked
                            AppStatusFilter.WIFI_BLOCKED -> R.string.filter_wifi_blocked
                            AppStatusFilter.MOBILE_BLOCKED -> R.string.filter_mobile_blocked
                        }
                        val isSelected = statusFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updateStatusFilter(filter) },
                            label = {
                                Text(
                                    text = stringResource(id = labelRes),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                selectedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }

            when (val state = uiState) {
                is ApplicationsUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
                        Text(
                            text = stringResource(R.string.splash_load_policies),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                is ApplicationsUiState.Error -> {
                    EmptyStateView(
                        message = state.message,
                        modifier = Modifier.weight(1f)
                    )
                }
                is ApplicationsUiState.Success -> {
                    if (state.apps.isEmpty()) {
                        EmptyStateView(
                            message = stringResource(id = R.string.no_applications_found),
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(
                                horizontal = Dimens.SpaceStandard,
                                vertical = Dimens.SpaceSmall
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.apps, key = { it.app.packageName }) { item ->
                                val iconState = produceState<ImageBitmap?>(initialValue = null, item.app.packageName) {
                                    value = AppIconLoader.getAppIcon(context, item.app.packageName)
                                }
                                val isSelected = selectedPackages.contains(item.app.packageName)

                                val formattedUsage = item.usage?.totalBytes?.let { bytes ->
                                    if (bytes > 0) Formatters.formatBytes(bytes) else null
                                }

                                ApplicationCard(
                                    appName = item.app.appName,
                                    packageName = item.app.packageName,
                                    isSystemApp = item.app.isSystemApp,
                                    dataUsageFormatted = formattedUsage,
                                    isSelected = isSelected,
                                    isSelectionMode = isSelectionMode,
                                    wifiState = if (item.rule.wifiPolicy == NetworkPolicy.ALLOW) AppNetworkState.ALLOWED else AppNetworkState.BLOCKED,
                                    mobileState = if (item.rule.mobileDataPolicy == NetworkPolicy.ALLOW) AppNetworkState.ALLOWED else AppNetworkState.BLOCKED,
                                    appIcon = iconState.value,
                                    onWifiToggle = {
                                        viewModel.toggleWifiPolicy(item)
                                    },
                                    onMobileToggle = {
                                        viewModel.toggleMobilePolicy(item)
                                    },
                                    onLongClick = {
                                        viewModel.toggleSelection(item.app.packageName)
                                    },
                                    onClick = {
                                        if (isSelectionMode) {
                                            viewModel.toggleSelection(item.app.packageName)
                                        } else {
                                            navController.navigate("app_details/${item.app.packageName}")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
