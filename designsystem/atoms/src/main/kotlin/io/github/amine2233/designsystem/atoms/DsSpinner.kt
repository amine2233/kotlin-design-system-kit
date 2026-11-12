package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled

private const val STROKE_RATIO = 8

enum class DsSpinnerSize(
    val diameter: Dp,
) {
    Tiny(14.dp),
    Small(18.dp),
    Medium(22.dp),
    Large(26.dp),
    ExtraLarge(30.dp),
    ;

    val strokeWidth: Dp get() = diameter / STROKE_RATIO
}

enum class DsSpinnerStyle { Primary, Success, Warning, Danger, Info }

@Composable
fun DsSpinnerStyle.color(): Color {
    val c = DsTheme.colors
    return when (this) {
        DsSpinnerStyle.Primary -> c.primary
        DsSpinnerStyle.Success -> c.success
        DsSpinnerStyle.Warning -> c.warning
        DsSpinnerStyle.Danger -> c.error
        DsSpinnerStyle.Info -> c.info
    }
}

/** Indeterminate activity ring; a fixed three-quarter arc when [animate] is false. */
@Composable
fun DsSpinner(
    modifier: Modifier = Modifier,
    size: DsSpinnerSize = DsSpinnerSize.Medium,
    style: DsSpinnerStyle = DsSpinnerStyle.Primary,
    contentDescription: String = "Chargement",
    animate: Boolean = DsTheme.animationsEnabled,
) {
    Box(
        modifier.clearAndSetSemantics {
            this.contentDescription = contentDescription
            progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        },
    ) {
        DsCircularIndicator(
            progress = null,
            animate = animate,
            color = style.color(),
            trackColor = Color.Transparent,
            fixedTrackColor = Color.Transparent,
            size = size.diameter,
            strokeWidth = size.strokeWidth,
            strokeCap = StrokeCap.Round,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsSpinnerPreview() =
    DsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(DsSpacing.smd), verticalAlignment = Alignment.CenterVertically) {
            DsSpinnerSize.entries.forEach { DsSpinner(size = it) }
        }
        Spacer(Modifier.height(DsSpacing.smd))
        Row(horizontalArrangement = Arrangement.spacedBy(DsSpacing.smd)) {
            DsSpinnerStyle.entries.forEach { DsSpinner(style = it) }
        }
        Spacer(Modifier.height(DsSpacing.smd))
        DsSpinner(animate = false)
    }
