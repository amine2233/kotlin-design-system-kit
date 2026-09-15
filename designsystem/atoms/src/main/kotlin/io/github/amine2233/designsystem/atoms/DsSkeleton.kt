package io.github.amine2233.designsystem.atoms

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Pulsing placeholder block; size it with the modifier (e.g. `Modifier.fillMaxWidth().height(12.dp)`). */
@Composable
fun DsSkeleton(modifier: Modifier = Modifier, shape: Shape = DsShapes.sm, animate: Boolean = true) {
    val alpha by rememberInfiniteTransition(label = "skeleton").animateFloat(
        initialValue = 1f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Reverse),
        label = "skeletonAlpha",
    )
    Box(modifier.alpha(if (animate) alpha else 1f).background(DsTheme.colors.surfaceMuted, shape))
}

@DsComponentPreview
@Composable
private fun DsSkeletonPreview() = DsPreview {
    DsSkeleton(Modifier.fillMaxWidth().height(14.dp), animate = false)
    Spacer(Modifier.height(8.dp))
    DsSkeleton(Modifier.size(64.dp), shape = DsShapes.pill, animate = false)
}
