package com.aifirewall.app.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.DeepBlue
import com.aifirewall.app.presentation.theme.PurpleAccent

@Composable
fun ShieldLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    progress: Float = 1f,
    glowIntensity: Float = 1f
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000),
        label = "ShieldProgress"
    )

    Canvas(modifier = modifier.size(size)) {
        val width = size.toPx()
        val height = size.toPx()
        val centerX = width / 2
        val topY = height * 0.1f
        val bottomY = height * 0.9f
        val leftX = width * 0.15f
        val rightX = width * 0.85f
        val curveY = height * 0.45f

        val shieldPath = Path().apply {
            moveTo(centerX, topY)
            lineTo(rightX, topY + (height * 0.1f))
            cubicTo(
                rightX, curveY,
                rightX * 0.9f, bottomY * 0.8f,
                centerX, bottomY
            )
            cubicTo(
                leftX * 1.1f, bottomY * 0.8f,
                leftX, curveY,
                leftX, topY + (height * 0.1f)
            )
            close()
        }

        // Draw inner glow
        drawPath(
            path = shieldPath,
            brush = Brush.radialGradient(
                colors = listOf(
                    CyanGlow.copy(alpha = 0.2f * glowIntensity),
                    PurpleAccent.copy(alpha = 0.1f * glowIntensity),
                    Color.Transparent
                ),
                center = Offset(centerX, height * 0.5f),
                radius = width * 0.5f
            ),
            blendMode = BlendMode.Screen
        )

        // Draw outer border (with dash path effect optionally, or just stroke)
        if (animatedProgress > 0f) {
            val strokeWidth = 4.dp.toPx()
            drawPath(
                path = shieldPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        CyanGlow.copy(alpha = animatedProgress),
                        PurpleAccent.copy(alpha = animatedProgress)
                    )
                ),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
