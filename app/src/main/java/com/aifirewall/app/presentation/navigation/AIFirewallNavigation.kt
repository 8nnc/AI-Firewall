package com.aifirewall.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aifirewall.app.presentation.screens.shell.MainShellScreen
import com.aifirewall.app.presentation.screens.splash.AnimatedSplashScreen

object Destinations {
    const val SPLASH = "splash"
    const val DASHBOARD = "dashboard"
    const val APPLICATIONS = "applications"
    const val DATA_USAGE = "data_usage"
    const val ACTIVITY = "activity"
    const val SETTINGS = "settings"
}

@Composable
fun AIFirewallNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SPLASH
    ) {
        composable(Destinations.SPLASH) {
            AnimatedSplashScreen(
                onAnimationFinished = {
                    navController.navigate(Destinations.DASHBOARD) {
                        popUpTo(Destinations.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        
        composable(Destinations.DASHBOARD) {
            MainShellScreen()
        }
    }
}
