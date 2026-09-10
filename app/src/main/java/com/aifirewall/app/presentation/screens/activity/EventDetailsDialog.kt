package com.aifirewall.app.presentation.screens.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aifirewall.app.R
import com.aifirewall.app.data.local.db.entity.FirewallEventEntity
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventDetailsDialog(event: FirewallEventEntity, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(Dimens.SpaceLarge)
            ) {
                Text(
                    text = "Event Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))

                val color = if (event.action == "BLOCK") BlockedColor else AllowedColor
                
                DetailRow(label = "Application", value = event.packageName)
                DetailRow(label = "Action", value = event.action, valueColor = color)
                DetailRow(label = "Transport", value = event.transport)
                DetailRow(label = "Protocol", value = event.protocol)
                
                if (event.destinationAddress.isNotEmpty()) {
                    DetailRow(label = "Destination", value = "${event.destinationAddress}:${event.destinationPort}")
                }
                
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                DetailRow(label = "Time", value = dateFormat.format(Date(event.timestamp)))
                DetailRow(label = "Reason", value = event.reason)
                
                if (event.attempts > 1) {
                    DetailRow(label = "Attempts", value = event.attempts.toString())
                }

                Spacer(modifier = Modifier.height(Dimens.SpaceLarge))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Close", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = Dimens.SpaceSmall)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = valueColor,
            fontWeight = FontWeight.Medium
        )
        Divider(modifier = Modifier.padding(top = Dimens.SpaceSmall), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
    }
}
