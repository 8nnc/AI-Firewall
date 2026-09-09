package com.aifirewall.app.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import com.aifirewall.app.presentation.theme.ErrorColor
import com.aifirewall.app.presentation.theme.SuccessColor

enum class FirewallStatus {
    ACTIVE,
    INACTIVE,
    STARTING,
    ERROR
}

@Composable
fun FirewallStatusCard(
    status: FirewallStatus,
    statusText: String,
    modifier: Modifier = Modifier,
    onToggle: (() -> Unit)? = null
) {
    val statusColor by animateColorAsState(
        targetValue = when (status) {
            FirewallStatus.ACTIVE -> SuccessColor
            FirewallStatus.INACTIVE -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            FirewallStatus.STARTING -> CyanGlow
            FirewallStatus.ERROR -> ErrorColor
        }, label = "statusColor"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(Dimens.SpaceStandard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )
            Spacer(modifier = Modifier.width(Dimens.SpaceStandard))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Firewall Status",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            if (onToggle != null) {
                androidx.compose.material3.Switch(
                    checked = (status == FirewallStatus.ACTIVE || status == FirewallStatus.STARTING),
                    onCheckedChange = { onToggle() },
                    enabled = (status != FirewallStatus.STARTING)
                )
            }
        }
    }
}
