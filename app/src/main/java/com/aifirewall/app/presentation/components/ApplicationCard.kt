package com.aifirewall.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ImageBitmap
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aifirewall.app.R
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.Dimens
import com.aifirewall.app.presentation.theme.DisabledColor

enum class AppNetworkState {
    ALLOWED, BLOCKED, UNKNOWN
}

@Composable
fun ApplicationCard(
    appName: String,
    wifiState: AppNetworkState,
    mobileState: AppNetworkState,
    modifier: Modifier = Modifier,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    appIcon: ImageBitmap? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(Dimens.SpaceStandard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (appIcon != null) {
                Image(
                    bitmap = appIcon,
                    contentDescription = appName,
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = appName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = iconColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(Dimens.SpaceStandard))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(modifier = Modifier.padding(top = Dimens.SpaceSmall)) {
                    NetworkIndicator(
                        icon = Icons.Default.Share,
                        label = stringResource(id = R.string.wifi),
                        state = wifiState
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpaceLarge))
                    NetworkIndicator(
                        icon = Icons.Default.Build,
                        label = stringResource(id = R.string.mobile_data),
                        state = mobileState
                    )
                }
            }
        }
    }
}

@Composable
private fun NetworkIndicator(
    icon: ImageVector,
    label: String,
    state: AppNetworkState
) {
    val (color, statusIcon) = when (state) {
        AppNetworkState.ALLOWED -> AllowedColor to Icons.Default.CheckCircle
        AppNetworkState.BLOCKED -> BlockedColor to Icons.Default.Clear
        AppNetworkState.UNKNOWN -> DisabledColor to Icons.Default.Clear
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(Dimens.SpaceSmall))
        Icon(
            imageVector = statusIcon,
            contentDescription = state.name,
            modifier = Modifier.size(14.dp),
            tint = color
        )
    }
}
