package io.github.amine2233.designsystem.organisms

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

private const val RING_COUNT = 3
private const val PERIOD_MS = 1800

/**
 * Contactless waiting animation: three rings expand and fade from a teal disc holding the NFC icon.
 * [animate] false freezes rings at staggered radii (deterministic for screenshot tests).
 */
@Composable
fun DsNfcPulse(modifier: Modifier = Modifier, size: Dp = 180.dp, animate: Boolean = true) {
    val c = DsTheme.colors
    val transition = rememberInfiniteTransition(label = "nfc")
    val phases: List<State<Float>> = List(RING_COUNT) { i ->
        if (animate) {
            transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(PERIOD_MS, easing = LinearEasing),
                    RepeatMode.Restart,
                    initialStartOffset = StartOffset(i * PERIOD_MS / RING_COUNT),
                ),
                label = "ring$i",
            )
        } else {
            mutableFloatStateOf((i + 1f) / (RING_COUNT + 1f))
        }
    }
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(size)) {
            val maxR = this.size.minDimension / 2f
            val innerR = maxR * 0.34f
            phases.forEach { phase ->
                val t = phase.value
                drawCircle(
                    color = c.primary.copy(alpha = (1f - t) * 0.45f),
                    radius = innerR + (maxR - innerR) * t,
                    style = Stroke(width = maxR * 0.03f),
                )
            }
        }
        Box(Modifier.size(size * 0.62f).padding(size * 0.14f).background(c.primary, DsShapes.pill), contentAlignment = Alignment.Center) {
            Icon(DsIcons.Contactless, contentDescription = null, tint = c.onPrimary, modifier = Modifier.size(size * 0.2f))
        }
    }
}

/** NFC panel: pulse + "Approchez le téléphone" copy + amount. */
@Composable
fun DsNfcWaiting(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    amount: String? = null,
    animate: Boolean = true,
) {
    val c = DsTheme.colors
    Column(modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (amount != null) {
            Text(amount, style = DsTheme.typography.displayMedium, color = c.textPrimary)
            Spacer(Modifier.height(8.dp))
        }
        DsNfcPulse(animate = animate)
        Spacer(Modifier.height(12.dp))
        Text(title, style = DsTheme.typography.titleLarge, color = c.textPrimary, textAlign = TextAlign.Center)
        if (description != null) {
            Spacer(Modifier.height(6.dp))
            Text(description, style = DsTheme.typography.body, color = c.textSecondary, textAlign = TextAlign.Center)
        }
    }
}

@DsComponentPreview
@Composable
private fun DsNfcPulsePreview() = DsPreview {
    DsNfcPulse(animate = false)
}

@DsComponentPreview
@Composable
private fun DsNfcWaitingPreview() = DsPreview {
    DsNfcWaiting("Approchez le téléphone", description = "Le client valide avec Apple Pay ou Google Pay.", amount = "1 188 DA", animate = false)
}
