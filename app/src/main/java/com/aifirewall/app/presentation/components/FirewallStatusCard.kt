package com.aifirewall.app.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aifirewall.app.R
import com.aifirewall.app.domain.model.FirewallState
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import com.aifirewall.app.presentation.theme.ErrorColor
import com.aifirewall.app.presentation.theme.SuccessColor
import com.aifirewall.app.presentation.theme.WarningColor

enum class FirewallStatus {
    ACTIVE,
    INACTIVE,
    STARTING,
    STOPPING,
    ERROR
}

/**
 * Enterprise Primary Protection Status Centerpiece
 * Displays real-time FirewallState with animated status ring, telemetry descriptors,
 * and state-managed tactical control button.
 */
@Composable
fun EnterpriseProtectionCenterpiece(
    state: FirewallState,
    modifier: Modifier = Modifier,
    onToggle: () -> Unit
) {
    val statusColor by animateColorAsState(
        targetValue = when (state) {
            FirewallState.ACTIVE -> SuccessColor
            FirewallState.STARTING -> CyanGlow
            FirewallState.STOPPING -> WarningColor
            FirewallState.ERROR -> ErrorColor
            FirewallState.INACTIVE -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
        },
        label = "statusColor"
    )

    val isTransitioning = state == FirewallState.STARTING || state == FirewallState.STOPPING
    val isProtected = state == FirewallState.ACTIVE

    // Subtle breathing pulse animation when active or transitioning
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by if (isProtected || isTransitioning) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
    } else {
        rememberInfiniteTransition(label = "static").animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1000)),
            label = "static"
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        border = BorderStroke(
            width = 1.dp,
            color = statusColor.copy(alpha = if (isProtected || isTransitioning) 0.35f else 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Icon with Outer Pulsing Glow Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(88.dp)
            ) {
                // Outer ring
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = if (isProtected || isTransitioning) 0.12f else 0.05f))
                        .border(
                            width = 1.5.dp,
                            color = statusColor.copy(alpha = if (isProtected || isTransitioning) 0.3f else 0.15f),
                            shape = CircleShape
                        )
                )

                // Inner core
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    statusColor.copy(alpha = 0.25f),
                                    statusColor.copy(alpha = 0.08f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isTransitioning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = statusColor,
                            strokeWidth = 3.dp
                        )
                    } else {
                        val iconVector = when (state) {
                            FirewallState.ACTIVE -> Icons.Default.CheckCircle
                            FirewallState.ERROR -> Icons.Default.Warning
                            else -> Icons.Default.Lock
                        }
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceStandard))

            // State Badge Pill
            Surface(
                shape = RoundedCornerShape(50),
                color = statusColor.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (state) {
                            FirewallState.ACTIVE -> stringResource(R.string.status_active).uppercase()
                            FirewallState.STARTING -> stringResource(R.string.status_starting).uppercase()
                            FirewallState.STOPPING -> stringResource(R.string.status_stopping).uppercase()
                            FirewallState.ERROR -> stringResource(R.string.status_error).uppercase()
                            FirewallState.INACTIVE -> stringResource(R.string.status_inactive).uppercase()
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

            // Status Headline
            val statusTitle = when (state) {
                FirewallState.ACTIVE -> stringResource(R.string.firewall_protected_title)
                FirewallState.STARTING -> stringResource(R.string.firewall_starting_title)
                FirewallState.STOPPING -> stringResource(R.string.firewall_stopping_title)
                FirewallState.ERROR -> stringResource(R.string.firewall_error_title)
                FirewallState.INACTIVE -> stringResource(R.string.firewall_inactive_title)
            }
            Text(
                text = statusTitle,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle Description
            val statusDesc = when (state) {
                FirewallState.ACTIVE -> stringResource(R.string.firewall_active_desc)
                FirewallState.STARTING -> stringResource(R.string.firewall_starting_desc)
                FirewallState.STOPPING -> stringResource(R.string.firewall_stopping_desc)
                FirewallState.ERROR -> stringResource(R.string.firewall_error_desc)
                FirewallState.INACTIVE -> stringResource(R.string.firewall_inactive_desc)
            }
            Text(
                text = statusDesc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Dimens.SpaceMedium)
            )

            Spacer(modifier = Modifier.height(Dimens.SpaceLarge))

            // Tactical Control Action Button
            if (isProtected) {
                OutlinedButton(
                    onClick = onToggle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ErrorColor
                    ),
                    border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.disable_protection),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            } else {
                Button(
                    onClick = onToggle,
                    enabled = !isTransitioning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state == FirewallState.ERROR) ErrorColor else MaterialTheme.colorScheme.primary,
                        contentColor = if (state == FirewallState.ERROR) Color.White else MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    if (isTransitioning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.status_processing),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.enable_protection),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Backwards-compatible FirewallStatusCard for any legacy references.
 */
@Composable
fun FirewallStatusCard(
    status: FirewallStatus,
    statusText: String,
    modifier: Modifier = Modifier,
    onToggle: (() -> Unit)? = null
) {
    val mappedState = when (status) {
        FirewallStatus.ACTIVE -> FirewallState.ACTIVE
        FirewallStatus.STARTING -> FirewallState.STARTING
        FirewallStatus.STOPPING -> FirewallState.STOPPING
        FirewallStatus.ERROR -> FirewallState.ERROR
        FirewallStatus.INACTIVE -> FirewallState.INACTIVE
    }

    EnterpriseProtectionCenterpiece(
        state = mappedState,
        modifier = modifier,
        onToggle = { onToggle?.invoke() }
    )
}
