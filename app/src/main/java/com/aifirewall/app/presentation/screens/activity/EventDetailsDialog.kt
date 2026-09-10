package com.aifirewall.app.presentation.screens.activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.aifirewall.app.R
import com.aifirewall.app.core.utils.AppIconLoader
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventDetailsDialog(event: FirewallEventEntity, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val iconState = produceState<ImageBitmap?>(initialValue = null, event.packageName) {
        value = AppIconLoader.getAppIcon(context, event.packageName)
    }

    val isBlocked = event.action == "BLOCK"
    val actionColor = if (isBlocked) BlockedColor else AllowedColor
    val badgeIcon = if (isBlocked) R.drawable.ic_shield_block else R.drawable.ic_shield_check

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
            shadowElevation = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Row with Title & Status Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.event_details_title),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = actionColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, actionColor.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = badgeIcon),
                                contentDescription = null,
                                tint = actionColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (isBlocked) stringResource(R.string.status_blocked) else stringResource(R.string.status_protected),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                color = actionColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Application Identification Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.packageName.substringAfterLast("."),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = event.packageName,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Telemetry Details List
                if (event.destinationAddress.isNotEmpty()) {
                    TelemetryItem(
                        label = stringResource(R.string.event_detail_endpoint),
                        value = "${event.destinationAddress}:${event.destinationPort}",
                        isMonospace = true,
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }

                val transportIcon = if (event.transport.equals("WIFI", ignoreCase = true)) R.drawable.ic_wifi else R.drawable.ic_cellular
                TelemetryItem(
                    label = stringResource(R.string.event_detail_transport),
                    value = "${event.protocol} • ${event.transport} • ${event.direction}"
                )

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                TelemetryItem(
                    label = stringResource(R.string.event_detail_timestamp),
                    value = dateFormat.format(Date(event.timestamp))
                )

                TelemetryItem(
                    label = stringResource(R.string.event_detail_reason),
                    value = if (event.eventType == "CONNECTION_BLOCKED") stringResource(id = R.string.connection_blocked) else event.reason
                )

                TelemetryItem(
                    label = stringResource(R.string.event_detail_uid),
                    value = "UID: ${event.uid}"
                )

                if (event.attempts > 1) {
                    TelemetryItem(
                        label = stringResource(R.string.attempts_count, event.attempts),
                        value = "${event.attempts} connection attempts recorded",
                        valueColor = actionColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.close),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TelemetryItem(
    label: String,
    value: String,
    isMonospace: Boolean = false,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
                fontSize = if (isMonospace) 13.sp else 14.sp
            ),
            color = valueColor
        )
        Divider(
            modifier = Modifier.padding(top = 6.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
        )
    }
}
