package com.aifirewall.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aifirewall.app.presentation.navigation.AIFirewallNavigation
import com.aifirewall.app.presentation.theme.AIFirewallTheme

import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the Android 12+ OS-level splash screen before super.onCreate
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        setContent {
            AIFirewallTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AIFirewallNavigation()
                }
            }
        }
    }
}
