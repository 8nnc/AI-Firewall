package com.aifirewall.app.presentation.screens.settings

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aifirewall.app.R
import com.aifirewall.app.data.preferences.PreferencesRepository
import com.aifirewall.app.data.repository.ConfigurationManager
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val configManager = remember { ConfigurationManager(context) }
    val prefRepo = remember { PreferencesRepository(context) }
    val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory(configManager))
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isDarkTheme by prefRepo.isDarkTheme.collectAsState(initial = null)
    val languageCode by prefRepo.languageCode.collectAsState(initial = null)

    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

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

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.settings_theme),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ThemeOptionRow(
                        title = stringResource(R.string.theme_system),
                        isSelected = isDarkTheme == null,
                        onClick = {
                            scope.launch {
                                // Default system theme
                                prefRepo.setDarkTheme(true)
                                showThemeDialog = false
                            }
                        }
                    )
                    ThemeOptionRow(
                        title = stringResource(R.string.theme_dark),
                        isSelected = isDarkTheme == true,
                        onClick = {
                            scope.launch {
                                prefRepo.setDarkTheme(true)
                                showThemeDialog = false
                            }
                        }
                    )
                    ThemeOptionRow(
                        title = stringResource(R.string.theme_light),
                        isSelected = isDarkTheme == false,
                        onClick = {
                            scope.launch {
                                prefRepo.setDarkTheme(false)
                                showThemeDialog = false
                            }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text(stringResource(R.string.close))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.settings_language),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ThemeOptionRow(
                        title = stringResource(R.string.lang_english),
                        isSelected = languageCode == "en",
                        onClick = {
                            scope.launch {
                                prefRepo.setLanguage("en")
                                showLanguageDialog = false
                            }
                        }
                    )
                    ThemeOptionRow(
                        title = stringResource(R.string.lang_arabic),
                        isSelected = languageCode == "ar",
                        onClick = {
                            scope.launch {
                                prefRepo.setLanguage("ar")
                                showLanguageDialog = false
                            }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(stringResource(R.string.close))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.nav_settings),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Configuration & System Security",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
                .padding(horizontal = Dimens.SpaceStandard)
                .padding(bottom = Dimens.SpaceExtraLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceLarge)
        ) {
            // Section 1: Security & Access Control
            SettingsSectionCard(title = stringResource(id = R.string.settings_security)) {
                SettingsItemRow(
                    icon = Icons.Default.Lock,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(R.string.settings_security),
                    subtitle = stringResource(R.string.settings_security_subtitle),
                    hasNavigation = true,
                    onClick = { navController.navigate("security_settings") }
                )
            }

            // Section 2: Firewall Policies & Diagnostics
            SettingsSectionCard(title = stringResource(id = R.string.settings_firewall)) {
                SettingsItemRow(
                    icon = Icons.Default.Lock,
                    iconTint = CyanGlow,
                    title = stringResource(R.string.settings_profiles_title),
                    subtitle = stringResource(R.string.settings_profiles_subtitle),
                    hasNavigation = true,
                    onClick = { navController.navigate("profiles") }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = Dimens.SpaceStandard),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                )
                SettingsItemRow(
                    icon = Icons.Default.Build,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(R.string.settings_rule_groups_title),
                    subtitle = stringResource(R.string.settings_rule_groups_subtitle),
                    hasNavigation = true,
                    onClick = { navController.navigate("rule_groups") }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = Dimens.SpaceStandard),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                )
                SettingsItemRow(
                    icon = Icons.Default.Build,
                    iconTint = CyanGlow,
                    title = stringResource(R.string.diagnostics_title),
                    subtitle = stringResource(R.string.settings_diagnostics_subtitle),
                    hasNavigation = true,
                    onClick = { navController.navigate("diagnostics") }
                )
            }

            // Section 3: Appearance & Localization
            SettingsSectionCard(title = stringResource(id = R.string.settings_appearance)) {
                val currentThemeLabel = when (isDarkTheme) {
                    true -> stringResource(R.string.theme_dark)
                    false -> stringResource(R.string.theme_light)
                    null -> stringResource(R.string.theme_system)
                }
                SettingsItemRow(
                    icon = Icons.Default.Build,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(id = R.string.settings_theme),
                    subtitle = currentThemeLabel,
                    badgeText = currentThemeLabel,
                    onClick = { showThemeDialog = true }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = Dimens.SpaceStandard),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                )
                val currentLangLabel = when (languageCode) {
                    "ar" -> stringResource(R.string.lang_arabic)
                    "en" -> stringResource(R.string.lang_english)
                    else -> "English / العربية 🇾🇪"
                }
                SettingsItemRow(
                    icon = Icons.Default.Info,
                    iconTint = CyanGlow,
                    title = stringResource(id = R.string.settings_language),
                    subtitle = currentLangLabel,
                    badgeText = currentLangLabel,
                    onClick = { showLanguageDialog = true }
                )
            }

            // Section 4: Notifications
            SettingsSectionCard(title = stringResource(id = R.string.settings_notifications)) {
                SettingsItemRow(
                    icon = Icons.Default.Notifications,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    title = stringResource(id = R.string.settings_notifications),
                    subtitle = stringResource(R.string.security_events_channel_desc),
                    badgeText = stringResource(R.string.status_active)
                )
            }

            // Section 5: Backup & Restore
            SettingsSectionCard(title = stringResource(R.string.settings_backup_title)) {
                SettingsItemRow(
                    icon = Icons.Default.Refresh,
                    iconTint = CyanGlow,
                    title = stringResource(R.string.settings_export_title),
                    subtitle = stringResource(R.string.settings_export_subtitle),
                    hasNavigation = true,
                    onClick = { exportLauncher.launch("aifirewall_backup.json") }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = Dimens.SpaceStandard),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                )
                SettingsItemRow(
                    icon = Icons.Default.Refresh,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(R.string.settings_import_title),
                    subtitle = stringResource(R.string.settings_import_subtitle),
                    hasNavigation = true,
                    onClick = { importLauncher.launch(arrayOf("application/json")) }
                )
            }

            // Section 6: About & Engine Status
            SettingsSectionCard(title = stringResource(id = R.string.settings_about)) {
                SettingsItemRow(
                    icon = Icons.Default.Info,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(id = R.string.app_name),
                    subtitle = "${stringResource(R.string.settings_version)} 1.0.0 • AI-Defense Core",
                    badgeText = stringResource(R.string.enterprise_tier)
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.22f)),
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItemRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String? = null,
    badgeText: String? = null,
    hasNavigation: Boolean = false,
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
                .padding(horizontal = Dimens.SpaceStandard, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = iconTint.copy(alpha = 0.12f),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title and Subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Trailing badge or Chevron
            if (badgeText != null) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            if (hasNavigation) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionRow(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
