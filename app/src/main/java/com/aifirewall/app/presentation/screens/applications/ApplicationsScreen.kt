package com.aifirewall.app.presentation.screens.applications

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aifirewall.app.R
import com.aifirewall.app.core.utils.AppIconLoader
import com.aifirewall.app.data.repository.AppRuleRepository
import com.aifirewall.app.data.repository.DataUsageRepository
import com.aifirewall.app.data.repository.InstalledAppsRepository
import com.aifirewall.app.domain.model.NetworkPolicy
import com.aifirewall.app.presentation.components.AppNetworkState
import com.aifirewall.app.presentation.components.ApplicationCard
import com.aifirewall.app.presentation.components.EmptyStateView
import com.aifirewall.app.presentation.theme.Dimens

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
    val selectedPackages by viewModel.selectedPackages.collectAsState()

    val isSelectionMode = selectedPackages.isNotEmpty()

    BackHandler(enabled = isSelectionMode) {
        viewModel.clearSelection()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSelectionMode) {
                        Text("${selectedPackages.size} Selected", fontWeight = FontWeight.Bold)
                    } else {
                        Text(
                            text = stringResource(id = R.string.nav_applications),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = { viewModel.clearSelection() }) {
                            Icon(Icons.Default.Clear, contentDescription = "Close Selection")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSelectionMode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background,
                    titleContentColor = if (isSelectionMode) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            if (isSelectionMode) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(Dimens.SpaceStandard)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)) {
                            Button(
                                onClick = { viewModel.applyBulkPolicy(NetworkPolicy.BLOCK, NetworkPolicy.ALLOW) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Block Wi-Fi")
                            }
                            Button(
                                onClick = { viewModel.applyBulkPolicy(NetworkPolicy.ALLOW, NetworkPolicy.BLOCK) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Block Mobile")
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)) {
                            Button(
                                onClick = { viewModel.applyBulkPolicy(NetworkPolicy.ALLOW, NetworkPolicy.ALLOW) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Allow All")
                            }
                            Button(
                                onClick = { viewModel.applyBulkPolicy(NetworkPolicy.BLOCK, NetworkPolicy.BLOCK) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Block Both")
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpaceStandard, vertical = Dimens.SpaceSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(text = stringResource(id = R.string.search_hint)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium
                    )
                    IconButton(onClick = { 
                        if (uiState is ApplicationsUiState.Success) {
                            val firstApp = (uiState as ApplicationsUiState.Success).apps.firstOrNull()?.app?.packageName
                            if (firstApp != null) {
                                viewModel.toggleSelection(firstApp)
                            }
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Select Multiple")
                    }
                }

                // Type Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = Dimens.SpaceStandard),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
                ) {
                    AppTypeFilter.values().forEach { filter ->
                        FilterChip(
                            selected = typeFilter == filter,
                            onClick = { viewModel.updateTypeFilter(filter) },
                            label = { Text(filter.name) }
                        )
                    }
                }

                // Status Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = Dimens.SpaceStandard, vertical = Dimens.SpaceSmall),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
                ) {
                    AppStatusFilter.values().forEach { filter ->
                        val labelRes = when (filter) {
                            AppStatusFilter.ALL -> R.string.filter_all
                            AppStatusFilter.ALLOWED -> R.string.filter_allowed
                            AppStatusFilter.BLOCKED -> R.string.filter_blocked
                            AppStatusFilter.WIFI_BLOCKED -> R.string.filter_wifi_blocked
                            AppStatusFilter.MOBILE_BLOCKED -> R.string.filter_mobile_blocked
                        }
                        FilterChip(
                            selected = statusFilter == filter,
                            onClick = { viewModel.updateStatusFilter(filter) },
                            label = { Text(stringResource(id = labelRes)) }
                        )
                    }
                }
            }
            
            when (val state = uiState) {
                is ApplicationsUiState.Loading -> {
                    Column(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentPadding = PaddingValues(
                                horizontal = Dimens.SpaceStandard,
                                vertical = Dimens.SpaceSmall
                            ),
                            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
                        ) {
                            items(state.apps, key = { it.app.packageName }) { item ->
                                val iconState = produceState<ImageBitmap?>(initialValue = null, item.app.packageName) {
                                    value = AppIconLoader.getAppIcon(context, item.app.packageName)
                                }
                                val isSelected = selectedPackages.contains(item.app.packageName)
                                
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                ) {
                                    ApplicationCard(
                                        appName = item.app.appName,
                                        wifiState = if (item.rule.wifiPolicy == NetworkPolicy.ALLOW) AppNetworkState.ALLOWED else AppNetworkState.BLOCKED,
                                        mobileState = if (item.rule.mobileDataPolicy == NetworkPolicy.ALLOW) AppNetworkState.ALLOWED else AppNetworkState.BLOCKED,
                                        appIcon = iconState.value,
                                        onClick = {
                                            if (isSelectionMode) {
                                                viewModel.toggleSelection(item.app.packageName)
                                            } else {
                                                navController.navigate("app_details/${item.app.packageName}")
                                            }
                                        }
                                    )
                                    // Make it long-clickable implicitly via a transparent overlay or similar, 
                                    // but for simplicity we will just let user click a "Select Mode" button, or we can use combinedClickable.
                                    // Actually, we'll just add a "Select" button to the app card if it was easy, or just use click when in select mode.
                                    // Since Compose Foundation's combinedClickable requires OptIn, we will add a small checkbox or just use longClick.
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
