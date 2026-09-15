package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsTheme

/** [progress] null = indeterminate. */
@Composable
fun DsLinearProgress(progress: Float?, modifier: Modifier = Modifier, thickness: Dp = 4.dp) {
    val m = modifier.fillMaxWidth().height(thickness)
    if (progress == null) {
        LinearProgressIndicator(modifier = m, color = DsTheme.colors.primary, trackColor = DsTheme.colors.border, strokeCap = StrokeCap.Round)
    } else {
        LinearProgressIndicator(progress = { progress.coerceIn(0f, 1f) }, modifier = m, color = DsTheme.colors.primary, trackColor = DsTheme.colors.border, strokeCap = StrokeCap.Round, drawStopIndicator = {})
    }
}

@Composable
fun DsCircularProgress(progress: Float?, modifier: Modifier = Modifier, size: Dp = 52.dp, strokeWidth: Dp = 4.dp) {
    val m = modifier.size(size)
    if (progress == null) {
        CircularProgressIndicator(modifier = m, color = DsTheme.colors.primary, trackColor = DsTheme.colors.border, strokeWidth = strokeWidth)
    } else {
        CircularProgressIndicator(progress = { progress.coerceIn(0f, 1f) }, modifier = m, color = DsTheme.colors.primary, trackColor = DsTheme.colors.primaryContainerBorder, strokeWidth = strokeWidth)
    }
}

