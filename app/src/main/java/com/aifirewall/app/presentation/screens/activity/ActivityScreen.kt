package com.aifirewall.app.presentation.screens.activity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aifirewall.app.R
import com.aifirewall.app.core.utils.AppIconLoader
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.presentation.components.EmptyStateView
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen() {
    val viewModel: ActivityViewModel = viewModel()
    val allEvents by viewModel.events.collectAsState()
    val filteredEvents by viewModel.filteredEvents.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val actionFilter by viewModel.actionFilter.collectAsState()

    var selectedEvent by remember { mutableStateOf<FirewallEventEntity?>(null) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
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

    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.clear_activity),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = "Are you sure you want to permanently clear all security event logs?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearConfirmDialog = false
                        showAuthForClear = true
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = BlockedColor)
                ) {
                    Text(text = stringResource(R.string.clear_activity), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text(text = stringResource(R.string.close))
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
                            text = stringResource(id = R.string.nav_activity),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        val blockedCount = allEvents.count { it.action == "BLOCK" }
                        Text(
                            text = "${allEvents.size} events • $blockedCount blocked",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    if (allEvents.isNotEmpty()) {
                        IconButton(onClick = { showClearConfirmDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.clear_activity),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (allEvents.isNotEmpty()) {
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
                                text = stringResource(R.string.search_activity_hint),
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
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }

                // Action Filter Chips (All, Blocked, Allowed)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpaceStandard, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EventActionFilter.values().forEach { filter ->
                        val isSelected = actionFilter == filter
                        val (label, count) = when (filter) {
                            EventActionFilter.ALL -> stringResource(R.string.filter_events_all) to allEvents.size
                            EventActionFilter.BLOCKED -> stringResource(R.string.filter_events_blocked) to allEvents.count { it.action == "BLOCK" }
                            EventActionFilter.ALLOWED -> stringResource(R.string.filter_events_allowed) to allEvents.count { it.action == "ALLOW" }
                        }

                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updateActionFilter(filter) },
                            label = {
                                Text(
                                    text = "$label ($count)",
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

            if (allEvents.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimens.SpaceExtraLarge),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_shield_check),
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = CyanGlow
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
                    Text(
                        text = stringResource(id = R.string.no_activity),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpaceSmall))
                    Text(
                        text = stringResource(id = R.string.no_recent_activity_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = Dimens.SpaceLarge)
                    )
                }
            } else if (filteredEvents.isEmpty()) {
                EmptyStateView(
                    message = "No matching security events found",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = Dimens.SpaceStandard,
                        vertical = Dimens.SpaceSmall
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredEvents, key = { it.id }) { event ->
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
    val context = LocalContext.current
    val iconState = produceState<ImageBitmap?>(initialValue = null, event.packageName) {
        value = AppIconLoader.getAppIcon(context, event.packageName)
    }

    val isBlocked = event.action == "BLOCK"
    val actionColor = if (isBlocked) BlockedColor else AllowedColor
    val badgeIcon = if (isBlocked) R.drawable.ic_shield_block else R.drawable.ic_shield_check
    val transportIcon = if (event.transport.equals("WIFI", ignoreCase = true)) R.drawable.ic_wifi else R.drawable.ic_cellular

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.22f)),
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Icon
            val icon = iconState.value
            if (icon != null) {
                Image(
                    bitmap = icon,
                    contentDescription = event.packageName,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = event.packageName.takeLast(2).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Event description & metadata
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = event.packageName.substringAfterLast("."),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Action Badge (BLOCKED / ALLOWED)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = actionColor.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, actionColor.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = badgeIcon),
                                contentDescription = null,
                                tint = actionColor,
                                modifier = Modifier.size(9.dp)
                            )
                            Text(
                                text = event.action,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = actionColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Endpoint & Transport details
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (event.destinationAddress.isNotEmpty()) {
                        Text(
                            text = "${event.destinationAddress}:${event.destinationPort}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = CyanGlow,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    } else {
                        Text(
                            text = if (event.eventType == "CONNECTION_BLOCKED") stringResource(id = R.string.connection_blocked) else event.reason,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }

                    // Transport icon & Protocol
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = transportIcon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = event.protocol,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Timestamp and attempts counter
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = timeFormat.format(Date(event.timestamp)),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (event.attempts > 1) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = actionColor.copy(alpha = 0.12f),
                        modifier = Modifier.padding(top = 3.dp)
                    ) {
                        Text(
                            text = "${event.attempts}x",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            color = actionColor,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
        }
    }
}
