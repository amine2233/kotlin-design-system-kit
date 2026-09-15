package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled

// Frame shown for indeterminate indicators in previews / screenshot tests.
private const val INSPECTION_PROGRESS = 0.75f

/** [progress] null = indeterminate. */
@Composable
fun DsLinearProgress(
    progress: Float?,
    modifier: Modifier = Modifier,
    thickness: Dp = 4.dp,
) {
    val m = modifier.fillMaxWidth().height(thickness)
    if (progress == null && DsTheme.animationsEnabled) {
        LinearProgressIndicator(
            modifier = m,
            color = DsTheme.colors.primary,
            trackColor = DsTheme.colors.border,
            strokeCap = StrokeCap.Round,
        )
    } else {
        LinearProgressIndicator(
            progress = { (progress ?: INSPECTION_PROGRESS).coerceIn(0f, 1f) },
            modifier = m,
            color = DsTheme.colors.primary,
            trackColor = DsTheme.colors.border,
            strokeCap = StrokeCap.Round,
            drawStopIndicator = {},
        )
    }
}

@Composable
fun DsCircularProgress(
    progress: Float?,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    strokeWidth: Dp = 4.dp,
) {
    val m = modifier.size(size)
    if (progress == null && DsTheme.animationsEnabled) {
        CircularProgressIndicator(
            modifier = m,
            color = DsTheme.colors.primary,
            trackColor = DsTheme.colors.border,
            strokeWidth = strokeWidth,
        )
    } else {
        CircularProgressIndicator(
            progress = { (progress ?: INSPECTION_PROGRESS).coerceIn(0f, 1f) },
            modifier = m,
            color = DsTheme.colors.primary,
            trackColor = DsTheme.colors.primaryContainerBorder,
            strokeWidth = strokeWidth,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsProgressPreview() =
    DsPreview {
        DsLinearProgress(0.72f)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DsCircularProgress(0.65f)
            DsCircularProgress(0.3f, size = 32.dp, strokeWidth = 3.dp)
        }
    }
