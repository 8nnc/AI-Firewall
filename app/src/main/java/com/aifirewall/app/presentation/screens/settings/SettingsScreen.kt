package com.aifirewall.app.presentation.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aifirewall.app.R
import com.aifirewall.app.data.repository.ConfigurationManager
import com.aifirewall.app.presentation.theme.Dimens
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: androidx.navigation.NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val configManager = remember { ConfigurationManager(context) }
    val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory(configManager))
    val snackbarHostState = remember { SnackbarHostState() }

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) {
            viewModel.exportConfiguration(uri)
        }
    }

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            viewModel.importConfiguration(uri)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.messageFlow.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.nav_settings),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            SettingsSection(title = stringResource(id = R.string.settings_appearance)) {
                SettingsItem(
                    icon = Icons.Default.Build,
                    title = stringResource(id = R.string.settings_theme),
                    subtitle = "System default" // To be wired to DataStore
                )
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(id = R.string.settings_language),
                    subtitle = "English / Arabic 🇾🇪" // To be wired to DataStore
                )
            }

            SettingsSection(title = stringResource(id = R.string.settings_security)) {
                SettingsItem(
                    icon = Icons.Default.Lock,
                    title = "Security Settings",
                    subtitle = "Manage PIN and Biometrics",
                    onClick = { navController.navigate("security_settings") }
                )
            }

            SettingsSection(title = stringResource(id = R.string.settings_notifications)) {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = stringResource(id = R.string.settings_notifications),
                    subtitle = "All alerts"
                )
            }

            SettingsSection(title = stringResource(id = R.string.settings_firewall)) {
                SettingsItem(
                    icon = Icons.Default.Lock,
                    title = "Firewall Profiles",
                    subtitle = "Manage and switch active firewall profiles",
                    onClick = { navController.navigate("profiles") }
                )
                SettingsItem(
                    icon = Icons.Default.Build,
                    title = "Rule Groups",
                    subtitle = "Manage rule groups for bulk application policies",
                    onClick = { navController.navigate("rule_groups") }
                )
            }

            SettingsSection(title = "Backup & Restore") {
                SettingsItem(
                    icon = Icons.Default.Refresh,
                    title = "Export Configuration",
                    subtitle = "Save profiles and rules to a file",
                    onClick = { exportLauncher.launch("aifirewall_backup.json") }
                )
                SettingsItem(
                    icon = Icons.Default.Refresh,
                    title = "Import Configuration",
                    subtitle = "Restore profiles and rules from a file",
                    onClick = { importLauncher.launch(arrayOf("application/json")) }
                )
            }

            SettingsSection(title = stringResource(id = R.string.settings_about)) {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(id = R.string.settings_version),
                    subtitle = "1.0.0"
                )
            }
            
            Spacer(modifier = Modifier.padding(bottom = Dimens.SpaceLarge))
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(
                horizontal = Dimens.SpaceStandard,
                vertical = Dimens.SpaceMedium
            )
        )
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpaceStandard, vertical = Dimens.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(Dimens.SpaceStandard))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
