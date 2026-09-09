package com.aifirewall.app.presentation.screens.activity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aifirewall.app.R
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen() {
    val viewModel: ActivityViewModel = viewModel()
    val events by viewModel.events.collectAsState()
    var selectedEvent by remember { mutableStateOf<FirewallEventEntity?>(null) }
    var showAuthForClear by remember { mutableStateOf(false) }

    if (showAuthForClear) {
        com.aifirewall.app.presentation.screens.security.RequireAuthentication(
            onAuthenticated = {
                showAuthForClear = false
                viewModel.clearEvents()
            },
            onCancel = {
                showAuthForClear = false
            }
        ) {
            // Empty content as RequireAuthentication manages the screen when active
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.nav_activity), fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showAuthForClear = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Clear Activity")
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
        if (events.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
                Text(
                    text = stringResource(id = R.string.no_activity),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(Dimens.SpaceStandard),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
            ) {
                items(events, key = { it.id }) { event ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(animationSpec = tween(300)) { it }
                    ) {
                        EventRow(event = event) {
                            selectedEvent = event
                        }
                    }
                }
            }
        }
    }

    val eventToShow = selectedEvent
    if (eventToShow != null) {
        EventDetailsDialog(event = eventToShow) {
            selectedEvent = null
        }
    }
}

@Composable
fun EventRow(event: FirewallEventEntity, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(Dimens.SpaceStandard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (event.action) {
                "BLOCK" -> Icons.Default.Clear
                "ALLOW" -> Icons.Default.CheckCircle
                else -> Icons.Default.Info
            }
            val color = when (event.action) {
                "BLOCK" -> BlockedColor
                "ALLOW" -> AllowedColor
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.size(Dimens.SpaceMedium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.packageName.substringAfterLast("."),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (event.eventType == "CONNECTION_BLOCKED") stringResource(id = R.string.connection_blocked) else event.reason,
                        style = MaterialTheme.typography.bodySmall,
                        color = color
                    )
                    Text(
                        text = " • ${event.transport} • ${event.protocol}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.size(Dimens.SpaceMedium))

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = timeFormat.format(Date(event.timestamp)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (event.attempts > 1) {
                    Text(
                        text = "${event.attempts}x",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = color,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
