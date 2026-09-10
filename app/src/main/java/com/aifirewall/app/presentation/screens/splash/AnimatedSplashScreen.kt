package com.aifirewall.app.presentation.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aifirewall.app.R
import com.aifirewall.app.presentation.components.ShieldLogo
import com.aifirewall.app.presentation.theme.CyanGlow
import com.aifirewall.app.presentation.theme.PurpleAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedSplashScreen(onAnimationFinished: () -> Unit) {
    var stage by remember { mutableStateOf(0) }
    
    val pulseAnim = remember { Animatable(0f) }
    val networkNodesAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Stage 1: Darkness (initial state)
        delay(300)
        
        // Stage 2: Energy Field & Stage 3: Network Nodes
        stage = 1
        launch {
            networkNodesAlpha.animateTo(1f, tween(800, easing = LinearEasing))
        }
        delay(1000)

        // Stage 4: Shield Formation
        stage = 2
        delay(800)

        // Stage 5: Protection Pulse
        stage = 3
        launch {
            pulseAnim.animateTo(1f, tween(1000))
        }
        delay(600)

        // Stage 6: Network Contact (Streams) - Handled in Canvas via state
        stage = 4
        delay(500)

        // Stage 7: Brand Reveal
        stage = 5
        textAlpha.animateTo(1f, tween(600))
        delay(1500)

        // Stage 8: Transition
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Background Energy / Nodes
        if (stage >= 1) {
            NetworkNodesLayer(alpha = networkNodesAlpha.value)
        }

        // Central Shield
        if (stage >= 2) {
            ShieldLogo(
                modifier = Modifier.align(Alignment.Center),
                size = 140.dp,
                progress = 1f,
                glowIntensity = if (stage >= 3) 1.5f else 1f
            )
        }

        // Pulse Effect
        if (stage >= 3 && pulseAnim.value < 1f) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension * pulseAnim.value
                drawCircle(
                    color = CyanGlow.copy(alpha = 1f - pulseAnim.value),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 8.dp.toPx())
                )
            }
        }

        // Brand Text
        AnimatedVisibility(
            visible = stage >= 5,
            enter = fadeIn(tween(800)),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.splash_subtitle),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = CyanGlow,
                        letterSpacing = 2.sp
                    )
                )
            }
        }
    }
}

@Composable
fun NetworkNodesLayer(alpha: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "nodes")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension * 0.35f
        val nodeCount = 5

        for (i in 0 until nodeCount) {
            val angle = Math.toRadians((rotation + (i * (360f / nodeCount))).toDouble())
            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()

            drawCircle(
                color = CyanGlow.copy(alpha = alpha * 0.6f),
                radius = 6.dp.toPx(),
                center = Offset(x, y)
            )
            
            // Connect to center
            drawLine(
                color = PurpleAccent.copy(alpha = alpha * 0.3f),
                start = center,
                end = Offset(x, y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}
