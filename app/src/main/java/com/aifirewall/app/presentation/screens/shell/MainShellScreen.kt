package com.aifirewall.app.presentation.screens.shell

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.aifirewall.app.R
import com.aifirewall.app.presentation.navigation.Destinations
import com.aifirewall.app.presentation.screens.applications.ApplicationDetailsScreen
import com.aifirewall.app.presentation.screens.applications.ApplicationsScreen
import com.aifirewall.app.presentation.screens.dashboard.DashboardScreen
import com.aifirewall.app.presentation.screens.datausage.DataUsageScreen
import com.aifirewall.app.presentation.screens.settings.SettingsScreen
import com.aifirewall.app.presentation.screens.activity.ActivityScreen

sealed class BottomNavItem(
    val route: String,
    val titleResId: Int,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem(Destinations.DASHBOARD, R.string.nav_dashboard, Icons.Default.Lock)
    object Applications : BottomNavItem(Destinations.APPLICATIONS, R.string.nav_applications, Icons.Default.Build)
    object Activity : BottomNavItem(Destinations.ACTIVITY, R.string.nav_activity, Icons.Default.List)
    object DataUsage : BottomNavItem(Destinations.DATA_USAGE, R.string.nav_data_usage, Icons.Default.DateRange)
    object Settings : BottomNavItem(Destinations.SETTINGS, R.string.nav_settings, Icons.Default.Settings)
}

@Composable
fun MainShellScreen(
    navController: NavHostController = rememberNavController()
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Applications,
        BottomNavItem.Activity,
        BottomNavItem.DataUsage,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            
            // Only show bottom bar for main tabs, hide it on detail screens
            if (currentRoute in items.map { it.route }) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = stringResource(id = item.titleResId)) },
                            label = { Text(stringResource(id = item.titleResId)) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Dashboard.route) {
                DashboardScreen(navController = navController)
            }
            composable("diagnostics") {
                com.aifirewall.app.presentation.screens.diagnostics.DiagnosticsScreen(navController = navController)
            }
            composable("security_settings") {
                com.aifirewall.app.presentation.screens.security.SecuritySettingsScreen(navController = navController)
            }
            composable(BottomNavItem.Applications.route) {
                ApplicationsScreen(navController = navController)
            }
            composable(BottomNavItem.Activity.route) {
                ActivityScreen()
            }
            composable(BottomNavItem.DataUsage.route) {
                DataUsageScreen()
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen(navController = navController)
            }
            composable(
                route = "app_details/{packageName}",
                arguments = listOf(navArgument("packageName") { type = NavType.StringType })
            ) { backStackEntry ->
                val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
                ApplicationDetailsScreen(navController = navController, packageName = packageName)
            }
            composable("profiles") {
                com.aifirewall.app.presentation.screens.profiles.ProfilesScreen(navController = navController)
            }
            composable("rule_groups") {
                com.aifirewall.app.presentation.screens.profiles.RuleGroupsScreen(navController = navController)
            }
        }
    }
}
