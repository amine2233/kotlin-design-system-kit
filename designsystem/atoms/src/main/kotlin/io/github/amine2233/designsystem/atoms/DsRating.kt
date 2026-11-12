package io.github.amine2233.designsystem.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsMotion
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled
import kotlin.math.floor

/** How much of one star is filled. */
enum class DsStarFill { Full, Half, Empty }

/**
 * Clamps [value] to `0..max` and rounds it to the nearest multiple of [step] (positive), ties up:
 * 0.25 → 0.5 and 0.75 → 1.0 with a step of 0.5.
 * NaN and infinities count as 0 and a [max] below 1 counts as 1, so bad input never crashes a row of stars.
 */
fun dsRatingClamp(
    value: Float,
    max: Int,
    step: Float,
): Float {
    val upper = max.coerceAtLeast(1).toFloat()
    if (!value.isFinite()) return 0f
    val rounded = floor(value.coerceIn(0f, upper) / step + 0.5f) * step
    return rounded.coerceIn(0f, upper)
}

/**
 * Splits [value] into exactly [max] stars (at least one), filled from the first one, to the nearest half:
 * 2.5 of 5 → Full, Full, Half, Empty, Empty.
 */
fun dsRatingStars(
    value: Float,
    max: Int,
): List<DsStarFill> {
    val count = max.coerceAtLeast(1)
    val displayed = dsRatingClamp(value, count, 0.5f)
    return List(count) { index ->
        when {
            displayed >= index + 1 -> DsStarFill.Full
            displayed >= index + 0.5f -> DsStarFill.Half
            else -> DsStarFill.Empty
        }
    }
}

/**
 * The value a tap on the 0-based star [index] selects: `index + 1`, or `index + 0.5` when [allowHalf] and the
 * tap landed in the star's leading half ([leadingHalf] — the start side, so the right half in RTL).
 */
fun dsRatingValueForTap(
    index: Int,
    leadingHalf: Boolean,
    allowHalf: Boolean,
): Float = if (allowHalf && leadingHalf) index + 0.5f else index + 1f

/**
 * Star rating — a product review, a service score.
 *
 * Read-only when [onValueChange] is null; otherwise every star is a [DsTheme.spacing] `minTouchTarget` cell and,
 * with [allowHalf], its leading half selects the `.5`. Accessibility sees the row as one adjustable element, never
 * one button per star. The value is always drawn to the nearest half. [stateDescription] is the caller's wording
 * for the value ("3.5 out of 5"): like [DsSlider], the rating never formats numbers itself.
 */
@Composable
fun DsRating(
    value: Float,
    modifier: Modifier = Modifier,
    onValueChange: ((Float) -> Unit)? = null,
    max: Int = 5,
    allowHalf: Boolean = false,
    enabled: Boolean = true,
    starSize: Dp = DsTheme.spacing.lg,
    stateDescription: String? = null,
    animate: Boolean = DsTheme.animationsEnabled,
) {
    val count = max.coerceAtLeast(1)
    val step = if (allowHalf) 0.5f else 1f
    val displayed = dsRatingClamp(value, count, 0.5f)
    val onChange by rememberUpdatedState(onValueChange)
    Row(
        modifier =
            modifier.clearAndSetSemantics {
                if (onValueChange == null) {
                    progressBarRangeInfo = ProgressBarRangeInfo(displayed, 0f..count.toFloat())
                } else {
                    val steps = if (allowHalf) 2 * count - 1 else count - 1
                    progressBarRangeInfo = ProgressBarRangeInfo(displayed, 0f..count.toFloat(), steps)
                    if (!enabled) disabled()
                    setProgress { target ->
                        if (enabled) onValueChange(dsRatingClamp(target, count, step))
                        enabled
                    }
                }
                if (stateDescription != null) this.stateDescription = stateDescription
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        dsRatingStars(displayed, count).forEachIndexed { index, fill ->
            val c = DsTheme.colors
            val target =
                when {
                    !enabled -> c.textDisabled
                    fill == DsStarFill.Empty -> c.borderStrong
                    else -> c.primary
                }
            val tint by animateColorAsState(target, if (animate) tween(DsMotion.FAST) else snap(), label = "ratingStar")
            val icon =
                when (fill) {
                    DsStarFill.Full -> DsIcons.StarFilled
                    DsStarFill.Half -> DsIcons.StarHalf
                    DsStarFill.Empty -> DsIcons.StarOutline
                }
            if (onValueChange == null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(starSize), tint = tint)
            } else {
                Box(
                    modifier =
                        Modifier
                            .sizeIn(minWidth = DsTheme.spacing.minTouchTarget, minHeight = DsTheme.spacing.minTouchTarget)
                            .pointerInput(enabled, allowHalf, isRtl, index) {
                                if (!enabled) return@pointerInput
                                detectTapGestures { offset ->
                                    val startHalf = offset.x < size.width / 2f
                                    val leadingHalf = if (isRtl) !startHalf else startHalf
                                    onChange?.invoke(dsRatingValueForTap(index, leadingHalf, allowHalf))
                                }
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(starSize), tint = tint)
                }
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsRatingPreview() =
    DsPreview {
        DsRating(0f)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsRating(2.5f)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsRating(5f)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsRating(7.5f, max = 10, starSize = DsTheme.spacing.md)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        var rating by remember { mutableFloatStateOf(3.5f) }
        DsRating(rating, onValueChange = { rating = it }, allowHalf = true)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsRating(3f, onValueChange = {}, enabled = false)
    }
