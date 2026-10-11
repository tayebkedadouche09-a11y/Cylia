package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

data class FloatingParticle(
    val id: Int,
    val emoji: String,
    val startX: Float,
    val speed: Int,
    val maxScale: Float
)

@Composable
fun FloatingHeartsEffect(
    triggerCount: Int,
    modifier: Modifier = Modifier
) {
    if (triggerCount <= 0) return

    val particles = remember(triggerCount) {
        val emojis = listOf("❤️", "💖", "💓", "✨", "💕", "🌹", "💘")
        List(6) { idx ->
            FloatingParticle(
                id = triggerCount * 10 + idx,
                emoji = emojis.random(),
                startX = Random.nextFloat() * 240f - 120f,
                speed = Random.nextInt(1200, 2000),
                maxScale = Random.nextFloat() * 0.8f + 0.9f
            )
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        particles.forEach { particle ->
            SingleFloatingHeart(particle)
        }
    }
}

@Composable
private fun SingleFloatingHeart(particle: FloatingParticle) {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val scale = remember { Animatable(0.4f) }

    LaunchedEffect(particle.id) {
        offsetY.animateTo(
            targetValue = -400f,
            animationSpec = tween(durationMillis = particle.speed, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(particle.id) {
        scale.animateTo(
            targetValue = particle.maxScale,
            animationSpec = tween(durationMillis = particle.speed / 2)
        )
    }

    LaunchedEffect(particle.id) {
        delay((particle.speed * 0.5).toLong())
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = (particle.speed * 0.5).toInt())
        )
    }

    if (alpha.value > 0f) {
        Text(
            text = particle.emoji,
            fontSize = 32.sp,
            modifier = Modifier
                .offset { IntOffset(particle.startX.toInt(), offsetY.value.toInt()) }
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}
