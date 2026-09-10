package com.aifirewall.app.presentation.screens.diagnostics

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.VpnService
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aifirewall.app.domain.model.FirewallState
import com.aifirewall.app.engine.FirewallStateManager
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class DiagnosticStatus {
    UNKNOWN, PASS, WARNING, FAIL
}

data class DiagnosticResult(
    val name: String,
    val status: DiagnosticStatus,
    val reason: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isRunning by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<List<DiagnosticResult>>(emptyList()) }
    
    val firewallState by FirewallStateManager.firewallState.collectAsState()

    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        isRunning = true
        coroutineScope.launch {
            try {
                results = runDiagnostics(context, FirewallStateManager.firewallState.value)
            } finally {
                isRunning = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Firewall Diagnostics") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
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
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.SpaceStandard)
                .verticalScroll(scrollState)
        ) {
            Button(
                onClick = {
                    if (isRunning) return@Button
                    val vpnIntent = try {
                        VpnService.prepare(context)
                    } catch (e: Exception) {
                        null
                    }

                    if (vpnIntent != null) {
                        try {
                            isRunning = true
                            vpnPermissionLauncher.launch(vpnIntent)
                        } catch (e: Exception) {
                            coroutineScope.launch {
                                try {
                                    results = runDiagnostics(context, firewallState)
                                } finally {
                                    isRunning = false
                                }
                            }
                        }
                    } else {
                        isRunning = true
                        coroutineScope.launch {
                            try {
                                results = runDiagnostics(context, firewallState)
                            } finally {
                                isRunning = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isRunning
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.size(Dimens.SpaceSmall))
                Text(if (isRunning) "Running Tests..." else "Run Firewall Test")
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceLarge))

            if (results.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column {
                        results.forEachIndexed { index, result ->
                            DiagnosticRow(result)
                            if (index < results.size - 1) {
                                Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun runDiagnostics(context: Context, firewallState: FirewallState): List<DiagnosticResult> {
    val results = mutableListOf<DiagnosticResult>()
    
    // Delays removed for immediate accurate results

    // 1. VPN Permission
    val hasVpnPermission = try {
        VpnService.prepare(context) == null
    } catch (e: Exception) {
        false
    }
    results.add(
        DiagnosticResult(
            name = "VPN Permission",
            status = if (hasVpnPermission) DiagnosticStatus.PASS else DiagnosticStatus.FAIL,
            reason = if (hasVpnPermission) "Granted" else "Permission required from Android"
        )
    )
    

    // 2. Firewall Engine
    results.add(
        DiagnosticResult(
            name = "Firewall Engine",
            status = when (firewallState) {
                FirewallState.ACTIVE -> DiagnosticStatus.PASS
                FirewallState.INACTIVE -> DiagnosticStatus.WARNING
                else -> DiagnosticStatus.FAIL
            },
            reason = firewallState.name
        )
    )
    

    // 3. Network Transport
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetwork
    val caps = cm.getNetworkCapabilities(activeNetwork)
    
    val hasInternet = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    val isWifi = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    val isCellular = caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    
    results.add(
        DiagnosticResult(
            name = "Upstream Network",
            status = if (hasInternet) DiagnosticStatus.PASS else DiagnosticStatus.WARNING,
            reason = if (hasInternet) {
                if (isWifi) "Wi-Fi Active" else if (isCellular) "Mobile Data Active" else "Connected"
            } else {
                "No Internet Connection"
            }
        )
    )
    
    
    // 4. IPv6 Support
    // We do not fully support IPv6 proxying yet, so we warn the user honestly.
    results.add(
        DiagnosticResult(
            name = "IPv6 Forwarding",
            status = DiagnosticStatus.WARNING,
            reason = "IPv6 is restricted by current engine implementation"
        )
    )
    

    // 5. DNS Status
    results.add(
        DiagnosticResult(
            name = "DNS Resolution",
            status = if (firewallState == FirewallState.ACTIVE) DiagnosticStatus.PASS else DiagnosticStatus.WARNING,
            reason = if (firewallState == FirewallState.ACTIVE) "Forwarding to system DNS" else "Engine inactive"
        )
    )

    return results
}

@Composable
fun DiagnosticRow(result: DiagnosticResult) {
    val icon = when (result.status) {
        DiagnosticStatus.PASS -> Icons.Default.CheckCircle
        DiagnosticStatus.WARNING -> Icons.Default.Warning
        DiagnosticStatus.FAIL -> Icons.Default.Warning
        else -> Icons.Default.Warning
    }
    
    val color = when (result.status) {
        DiagnosticStatus.PASS -> AllowedColor
        DiagnosticStatus.WARNING -> Color(0xFFFFB300)
        DiagnosticStatus.FAIL -> BlockedColor
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.SpaceStandard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color)
        }
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = Dimens.SpaceMedium)
        ) {
            Text(
                text = result.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = result.reason,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = result.status.name,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun Box(modifier: Modifier, contentAlignment: Alignment, content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Box(modifier = modifier, contentAlignment = contentAlignment) {
        content()
    }
}
