package com.aifirewall.app.presentation.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aifirewall.app.R
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.Dimens
import com.aifirewall.app.presentation.theme.SuccessColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Premium Commercial-Grade Cybersecurity Startup Experience (~1.8 seconds)
 *
 * Visual Sequence:
 * Phase 1: Background presence initialization
 * Phase 2: Refined fade & scale of brand emblem
 * Phase 3: Laser security scan-line sweep through the shield
 * Phase 4: Concentric security perimeter rings lock in
 * Phase 5: Technical status sequencing & micro-progress bar
 */
@Composable
fun AnimatedSplashScreen(onAnimationFinished: () -> Unit) {
    val logoScale = remember { Animatable(0.85f) }
    val logoAlpha = remember { Animatable(0f) }
    val scanProgress = remember { Animatable(0f) }
    val ringsAlpha = remember { Animatable(0f) }
    val progressValue = remember { Animatable(0f) }

    var statusStringRes by remember { mutableIntStateOf(R.string.splash_init_engine) }

    LaunchedEffect(Unit) {
        // Phase 2: Logo reveal (scale + fade)
        launch {
            logoAlpha.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        }
        launch {
            logoScale.animateTo(1f, tween(500, easing = FastOutSlowInEasing))
        }

        // Phase 5: Progress bar advancement throughout total duration
        launch {
            progressValue.animateTo(1f, tween(1700, easing = LinearEasing))
        }

        // Phase 3: Laser scan sweep across the brand shield
        delay(350)
        statusStringRes = R.string.splash_check_network
        launch {
            scanProgress.animateTo(1f, tween(750, easing = FastOutSlowInEasing))
        }

        // Phase 4: Security telemetry rings
        delay(400)
        statusStringRes = R.string.splash_load_policies
        launch {
            ringsAlpha.animateTo(1f, tween(450, easing = FastOutSlowInEasing))
        }

        // Phase 5 Final: System Ready confirmation
        delay(450)
        statusStringRes = R.string.splash_security_ready

        delay(400)
        // Completion: Transition directly to Dashboard without lag
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Subtle Background Radial Gradient
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        CyanGlow.copy(alpha = 0.04f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.minDimension * 0.7f
                )
            )
        }

        // Central Emblem with Concentric Security Telemetry Rings
        Box(
            modifier = Modifier
                .size(240.dp)
                .scale(logoScale.value)
                .alpha(logoAlpha.value),
            contentAlignment = Alignment.Center
        ) {
            // Phase 4: Concentric Security Telemetry Rings
            SecurityPerimeterRings(alpha = ringsAlpha.value)

            // Phase 2 & 3: Shield Emblem with Laser Scan Sweep
            ShieldBrandEmblem(scanProgress = scanProgress.value)
        }

        // Bottom Technical Status & Micro-Progress Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .padding(bottom = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Enterprise Tier Identifier
            Text(
                text = stringResource(id = R.string.splash_enterprise_shield),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Micro Technical Progress Bar (2dp height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progressValue.value)
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    CyanGlow.copy(alpha = 0.6f),
                                    CyanGlow,
                                    SuccessColor
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dynamic Technical Phase Status Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (progressValue.value >= 0.85f) SuccessColor else CyanGlow)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = statusStringRes),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Concentric technical security rings with subtle degree tick indicators.
 */
@Composable
private fun SecurityPerimeterRings(alpha: Float) {
    Canvas(modifier = Modifier.size(240.dp)) {
        if (alpha <= 0f) return@Canvas

        val centerOffset = Offset(size.width / 2, size.height / 2)
        val outerRadius = size.width * 0.46f
        val innerRadius = size.width * 0.38f

        // Outer telemetry ring
        drawCircle(
            color = CyanGlow.copy(alpha = alpha * 0.18f),
            radius = outerRadius,
            center = centerOffset,
            style = Stroke(width = 1.dp.toPx())
        )

        // Inner telemetry ring
        drawCircle(
            color = CyanGlow.copy(alpha = alpha * 0.10f),
            radius = innerRadius,
            center = centerOffset,
            style = Stroke(width = 0.8.dp.toPx())
        )

        // 4 crosshair tick marks at 0°, 90°, 180°, 270°
        val tickLength = 6.dp.toPx()
        val tickAngles = listOf(0.0, Math.PI / 2, Math.PI, 3 * Math.PI / 2)
        for (angle in tickAngles) {
            val startX = centerOffset.x + (outerRadius - tickLength / 2) * Math.cos(angle).toFloat()
            val startY = centerOffset.y + (outerRadius - tickLength / 2) * Math.sin(angle).toFloat()
            val endX = centerOffset.x + (outerRadius + tickLength / 2) * Math.cos(angle).toFloat()
            val endY = centerOffset.y + (outerRadius + tickLength / 2) * Math.sin(angle).toFloat()

            drawLine(
                color = CyanGlow.copy(alpha = alpha * 0.35f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 1.2.dp.toPx()
            )
        }
    }
}

/**
 * Geometric shield brand emblem featuring laser-sweep scanning pass.
 */
@Composable
private fun ShieldBrandEmblem(scanProgress: Float) {
    val sizeDp = 130.dp

    Box(
        modifier = Modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val topY = height * 0.08f
            val bottomY = height * 0.92f
            val leftX = width * 0.16f
            val rightX = width * 0.84f
            val curveY = height * 0.48f

            val shieldPath = Path().apply {
                moveTo(centerX, topY)
                lineTo(rightX, topY + (height * 0.10f))
                cubicTo(
                    rightX, curveY,
                    rightX * 0.88f, bottomY * 0.82f,
                    centerX, bottomY
                )
                cubicTo(
                    leftX * 1.12f, bottomY * 0.82f,
                    leftX, curveY,
                    leftX, topY + (height * 0.10f)
                )
                close()
            }

            // Shield interior background fill
            drawPath(
                path = shieldPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CyanGlow.copy(alpha = 0.10f),
                        CyanGlow.copy(alpha = 0.02f)
                    )
                )
            )

            // Shield outer border stroke
            drawPath(
                path = shieldPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CyanGlow.copy(alpha = 0.7f),
                        CyanGlow.copy(alpha = 0.2f)
                    )
                ),
                style = Stroke(width = 2.dp.toPx())
            )

            // Phase 3: Laser scan sweep line passing through the shield
            if (scanProgress in 0.01f..0.99f) {
                val scanY = topY + (bottomY - topY) * scanProgress
                val scanWidth = (rightX - leftX) * 0.9f
                val scanStartX = centerX - scanWidth / 2
                val scanEndX = centerX + scanWidth / 2

                // Laser scan line
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            CyanGlow.copy(alpha = 0.9f),
                            Color.White,
                            CyanGlow.copy(alpha = 0.9f),
                            Color.Transparent
                        ),
                        startX = scanStartX,
                        endX = scanEndX
                    ),
                    start = Offset(scanStartX, scanY),
                    end = Offset(scanEndX, scanY),
                    strokeWidth = 2.dp.toPx()
                )

                // Laser soft glow halo
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyanGlow.copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        center = Offset(centerX, scanY),
                        radius = 24.dp.toPx()
                    ),
                    radius = 24.dp.toPx(),
                    center = Offset(centerX, scanY)
                )
            }
        }

        // Center Lock Emblem
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyanGlow.copy(alpha = 0.35f)),
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = CyanGlow,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
