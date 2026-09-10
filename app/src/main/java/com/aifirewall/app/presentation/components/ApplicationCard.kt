package com.aifirewall.app.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aifirewall.app.R
import com.aifirewall.app.presentation.theme.AllowedColor
import com.aifirewall.app.presentation.theme.BlockedColor
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import com.aifirewall.app.presentation.theme.DisabledColor
import com.aifirewall.app.presentation.theme.WarningColor

enum class AppNetworkState {
    ALLOWED, BLOCKED, UNKNOWN
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ApplicationCard(
    appName: String,
    wifiState: AppNetworkState,
    mobileState: AppNetworkState,
    modifier: Modifier = Modifier,
    packageName: String? = null,
    isSystemApp: Boolean = false,
    dataUsageFormatted: String? = null,
    isSelected: Boolean = false,
    isSelectionMode: Boolean = false,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    appIcon: ImageBitmap? = null,
    onWifiToggle: (() -> Unit)? = null,
    onMobileToggle: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    val isAllAllowed = wifiState == AppNetworkState.ALLOWED && mobileState == AppNetworkState.ALLOWED
    val isAllBlocked = wifiState == AppNetworkState.BLOCKED && mobileState == AppNetworkState.BLOCKED

    val animatedBorderColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primary
            isAllBlocked -> BlockedColor.copy(alpha = 0.35f)
            else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
        },
        label = "border_color"
    )

    val animatedBgColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            else -> MaterialTheme.colorScheme.surface
        },
        label = "bg_color"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(14.dp),
        color = animatedBgColor,
        border = BorderStroke(1.dp, animatedBorderColor),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selection Checkbox (if in selection mode)
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            // Application Icon
            if (appIcon != null) {
                Image(
                    bitmap = appIcon,
                    contentDescription = appName,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconColor.copy(alpha = 0.15f)),
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

            Spacer(modifier = Modifier.width(12.dp))

            // App details (Name, Package/Tag, Status badge)
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
                        text = appName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // System / User Tag
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isSystemApp) {
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        }
                    ) {
                        Text(
                            text = if (isSystemApp) {
                                stringResource(R.string.system_app_tag)
                            } else {
                                stringResource(R.string.user_app_tag)
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = if (isSystemApp) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Package name or Data Usage
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (packageName != null) {
                        Text(
                            text = packageName,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }

                    if (dataUsageFormatted != null) {
                        Text(
                            text = "• $dataUsageFormatted",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = CyanGlow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Protection Status Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (badgeText, badgeColor, badgeIcon) = when {
                        isAllBlocked -> Triple(
                            stringResource(R.string.status_blocked),
                            BlockedColor,
                            R.drawable.ic_shield_block
                        )
                        isAllAllowed -> Triple(
                            stringResource(R.string.status_protected),
                            AllowedColor,
                            R.drawable.ic_shield_check
                        )
                        else -> Triple(
                            stringResource(R.string.status_partial),
                            WarningColor,
                            R.drawable.ic_shield_block
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = badgeColor.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = badgeIcon),
                                contentDescription = null,
                                tint = badgeColor,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = badgeColor
                            )
                        }
                    }
                }
            }

            // Quick Network Rule Toggles (Wi-Fi & Mobile)
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Wi-Fi Toggle Button
                NetworkToggleButton(
                    iconRes = R.drawable.ic_wifi,
                    contentDescription = stringResource(R.string.wifi),
                    isAllowed = wifiState == AppNetworkState.ALLOWED,
                    onToggle = onWifiToggle
                )

                // Mobile Data Toggle Button
                NetworkToggleButton(
                    iconRes = R.drawable.ic_cellular,
                    contentDescription = stringResource(R.string.mobile_data),
                    isAllowed = mobileState == AppNetworkState.ALLOWED,
                    onToggle = onMobileToggle
                )
            }
        }
    }
}

@Composable
private fun NetworkToggleButton(
    iconRes: Int,
    contentDescription: String,
    isAllowed: Boolean,
    onToggle: (() -> Unit)?
) {
    val activeColor = if (isAllowed) AllowedColor else BlockedColor
    val activeBg = if (isAllowed) AllowedColor.copy(alpha = 0.12f) else BlockedColor.copy(alpha = 0.15f)
    val activeBorder = if (isAllowed) AllowedColor.copy(alpha = 0.3f) else BlockedColor.copy(alpha = 0.4f)

    Surface(
        onClick = { onToggle?.invoke() },
        enabled = onToggle != null,
        shape = RoundedCornerShape(10.dp),
        color = activeBg,
        border = BorderStroke(1.dp, activeBorder),
        modifier = Modifier.size(38.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                tint = activeColor,
                modifier = Modifier.size(18.dp)
            )

            // Small status dot/badge in bottom corner
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(3.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(activeColor)
            )
        }
    }
}
