package com.aifirewall.app.presentation.screens.security

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.aifirewall.app.R
import com.aifirewall.app.core.security.SecurityManager
import com.aifirewall.app.presentation.theme.Dimens
import kotlinx.coroutines.delay

@Composable
fun SecurityLockScreen(
    title: String = stringResource(id = R.string.security_lock),
    isSetupMode: Boolean = false,
    onSuccess: (String) -> Unit,
    onCancel: () -> Unit = {}
) {
    val context = LocalContext.current
    val securityManager = remember { SecurityManager.getInstance(context) }
    
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isErrorState by remember { mutableStateOf(false) }
    var lockoutSeconds by remember { mutableIntStateOf(0) }
    var failedAttempts by remember { mutableIntStateOf(0) }

    val canUseBiometric = remember {
        val biometricManager = BiometricManager.from(context)
        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }
    
    val showBiometric = !isSetupMode && securityManager.isBiometricEnabled.value && canUseBiometric

    LaunchedEffect(lockoutSeconds) {
        if (lockoutSeconds > 0) {
            delay(1000L)
            lockoutSeconds--
        }
    }

    LaunchedEffect(isErrorState) {
        if (isErrorState) {
            delay(500L) // Small delay to show error visually before clearing PIN
            pin = ""
            isErrorState = false
        }
    }

    val showBiometricPrompt = {
        if (context is FragmentActivity) {
            val executor = ContextCompat.getMainExecutor(context)
            val biometricPrompt = BiometricPrompt(context, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // Ignore cancellation, but show other errors
                        if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            error = errString.toString()
                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        securityManager.unlockSession()
                        onSuccess("")
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(context.getString(R.string.biometric_auth))
                .setSubtitle(context.getString(R.string.unlock_firewall))
                .setNegativeButtonText(context.getString(android.R.string.cancel))
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    LaunchedEffect(Unit) {
        if (showBiometric) {
            showBiometricPrompt()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.SpaceLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))

        // PIN Indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..4) {
                Box(
                    modifier = Modifier
                        .padding(Dimens.SpaceSmall)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isErrorState -> MaterialTheme.colorScheme.error
                                i <= pin.length -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                            }
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        AnimatedVisibility(visible = error != null || lockoutSeconds > 0) {
            Text(
                text = if (lockoutSeconds > 0) "Try again in $lockoutSeconds s" else error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceExtraLarge))

        // Numeric Keypad
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf(if (showBiometric) "BIO" else "", "0", "DEL")
        )

        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    KeypadButton(
                        text = key,
                        enabled = lockoutSeconds == 0,
                        onClick = {
                            error = null
                            when (key) {
                                "DEL" -> {
                                    if (pin.isNotEmpty()) pin = pin.dropLast(1)
                                }
                                "BIO" -> {
                                    showBiometricPrompt()
                                }
                                "" -> {}
                                else -> {
                                    if (pin.length < 4) {
                                        pin += key
                                        if (pin.length == 4) {
                                            if (isSetupMode) {
                                                onSuccess(pin)
                                            } else {
                                                if (securityManager.verifyPin(pin)) {
                                                    failedAttempts = 0
                                                    securityManager.unlockSession()
                                                    onSuccess(pin)
                                                } else {
                                                    isErrorState = true
                                                    error = context.getString(R.string.incorrect_pin)
                                                    failedAttempts++
                                                    if (failedAttempts >= 5) {
                                                        lockoutSeconds = 30 // 30s lockout after 5 fails
                                                        failedAttempts = 0 // Reset counter for next batch
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
        }
        
        if (!isSetupMode) {
            Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
            Text(
                text = stringResource(id = android.R.string.cancel),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.clickable { onCancel() }.padding(Dimens.SpaceMedium)
            )
        }
    }
}

@Composable
fun KeypadButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(
                if (text.isEmpty()) androidx.compose.ui.graphics.Color.Transparent
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable(enabled = enabled && text.isNotEmpty(), onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (text) {
            "DEL" -> Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurface
            )
            "BIO" -> Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Biometric",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            "" -> {}
            else -> Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
