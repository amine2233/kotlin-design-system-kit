package io.github.amine2233.designsystem.atoms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsMotion
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

enum class DsProgressRingSize(
    val diameter: Dp,
    val strokeWidth: Dp,
) {
    Small(40.dp, 4.dp),
    Medium(64.dp, 6.dp),
    Large(96.dp, 10.dp),
}

/** The tone of a [DsProgressRing]'s arc. */
enum class DsProgressRingStyle { Primary, Success, Warning, Danger, Info }

internal fun dsProgressRingFraction(progress: Float): Float = if (progress.isFinite()) progress.coerceIn(0f, 1f) else 0f

internal fun dsProgressRingPercentText(
    fraction: Float,
    locale: Locale,
): String {
    val format = NumberFormat.getPercentInstance(locale).apply { maximumFractionDigits = 0 }
    return format.format((fraction * 100).roundToInt() / 100.0)
}

@Composable
@ReadOnlyComposable
private fun DsProgressRingSize.valueStyle(): TextStyle =
    when (this) {
        DsProgressRingSize.Small -> DsTheme.typography.caption
        DsProgressRingSize.Medium -> DsTheme.typography.labelStrong
        DsProgressRingSize.Large -> DsTheme.typography.title
    }

@Composable
@ReadOnlyComposable
private fun DsProgressRingStyle.color(): Color {
    val c = DsTheme.colors
    return when (this) {
        DsProgressRingStyle.Primary -> c.primary
        DsProgressRingStyle.Success -> c.success
        DsProgressRingStyle.Warning -> c.warning
        DsProgressRingStyle.Danger -> c.error
        DsProgressRingStyle.Info -> c.info
    }
}

/**
 * Determinate ring showing how far a task has got, with the percentage in its centre and an
 * optional [label] below — for a metric the user reads (upload, quota, goal reached).
 *
 * For an indeterminate spinner or a bare indicator without a value, use [DsCircularProgress].
 */
@Composable
fun DsProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: DsProgressRingSize = DsProgressRingSize.Medium,
    style: DsProgressRingStyle = DsProgressRingStyle.Primary,
    showValue: Boolean = true,
    label: String? = null,
    enabled: Boolean = true,
    animate: Boolean = DsTheme.animationsEnabled,
) {
    val fraction = dsProgressRingFraction(progress)
    val percentText = dsProgressRingPercentText(fraction, LocalConfiguration.current.locales[0])
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(DsMotion.SLOW, easing = DsMotion.standardEasing),
        label = "progressRingFraction",
    )
    val drawnFraction = if (animate) animatedFraction else fraction
    val accent = if (enabled) style.color() else DsTheme.colors.textDisabled
    val track = DsTheme.colors.border

    Column(
        modifier =
            modifier.clearAndSetSemantics {
                progressBarRangeInfo = ProgressBarRangeInfo(fraction, 0f..1f)
                stateDescription = percentText
                if (label != null) contentDescription = label
                if (!enabled) disabled()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.size(size.diameter), contentAlignment = Alignment.Center) {
            Canvas(Modifier.matchParentSize()) {
                val strokePx = size.strokeWidth.toPx()
                val inset = strokePx / 2
                val topLeft = Offset(inset, inset)
                val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
                val stroke = Stroke(width = strokePx, cap = StrokeCap.Round)
                drawArc(track, 0f, 360f, useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
                if (drawnFraction > 0f) {
                    drawArc(
                        accent,
                        -90f,
                        360f * drawnFraction,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = stroke,
                    )
                }
            }
            if (showValue) {
                Text(
                    text = percentText,
                    style = size.valueStyle(),
                    color = accent,
                    maxLines = 1,
                    softWrap = false,
                )
            }
        }
        if (label != null) {
            Spacer(Modifier.height(DsTheme.spacing.xs))
            Text(
                text = label,
                style = DsTheme.typography.label,
                color = if (enabled) DsTheme.colors.textSecondary else DsTheme.colors.textDisabled,
            )
        }
    }
}

@DsComponentPreview
@Composable
private fun DsProgressRingPreview() =
    DsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm), verticalAlignment = Alignment.CenterVertically) {
            DsProgressRing(0.7f, size = DsProgressRingSize.Small, animate = false)
            DsProgressRing(0.7f, size = DsProgressRingSize.Medium, animate = false)
            DsProgressRing(0.7f, size = DsProgressRingSize.Large, animate = false)
        }
        Spacer(Modifier.height(DsTheme.spacing.sm))
        Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
            DsProgressRing(0.7f, showValue = false, animate = false)
            DsProgressRing(0.7f, label = "Upload", animate = false)
            DsProgressRing(0.4f, enabled = false, animate = false)
        }
        Spacer(Modifier.height(DsTheme.spacing.sm))
        Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs)) {
            DsProgressRingStyle.entries.forEach {
                DsProgressRing(0.6f, size = DsProgressRingSize.Small, style = it, animate = false)
            }
        }
        Spacer(Modifier.height(DsTheme.spacing.sm))
        Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
            DsProgressRing(0f, animate = false)
            DsProgressRing(1f, style = DsProgressRingStyle.Success, animate = false)
        }
    }
