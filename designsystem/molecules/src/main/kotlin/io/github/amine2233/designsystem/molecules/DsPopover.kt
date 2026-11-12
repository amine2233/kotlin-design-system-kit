package io.github.amine2233.designsystem.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsElevation
import io.github.amine2233.designsystem.core.DsMotion
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled

/** Which side of the anchor a [DsPopover] prefers; it flips to the other one when there is no room. */
enum class DsPopoverPlacement { Above, Below }

/**
 * Free-form content in a floating surface next to whatever the caller puts in [anchor]. State is
 * the caller's; an outside tap or Back calls [onDismissRequest], and focus moves into the popover.
 */
@Composable
fun DsPopover(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    placement: DsPopoverPlacement = DsPopoverPlacement.Below,
    animate: Boolean = DsTheme.animationsEnabled,
    anchor: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val gap = with(LocalDensity.current) { DsSpacing.xs.roundToPx() }
    val positionProvider =
        remember(placement, gap) {
            object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize,
                ): IntOffset = dsPopoverOffset(anchorBounds, windowSize, popupContentSize, placement, layoutDirection, gap)
            }
        }
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = expanded

    Box(modifier) {
        anchor()
        val showing = if (animate) visibleState.currentState || visibleState.targetState else expanded
        if (showing) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = onDismissRequest,
                properties = PopupProperties(focusable = true),
            ) {
                if (animate) {
                    val spec = tween<Float>(DsMotion.FAST, easing = DsMotion.standardEasing)
                    AnimatedVisibility(
                        visibleState = visibleState,
                        enter = fadeIn(spec) + scaleIn(spec),
                        exit = fadeOut(spec) + scaleOut(spec),
                    ) {
                        DsPopoverSurface(content = content)
                    }
                } else {
                    DsPopoverSurface(content = content)
                }
            }
        }
    }
}

/** The bordered surface a [DsPopover] floats; public so galleries and snapshots can show it inline. */
@Composable
fun DsPopoverSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    DsCard(
        modifier = modifier,
        shape = DsShapes.md,
        elevation = DsElevation.raised,
        contentPadding = DsSpacing.md,
        content = content,
    )
}

/**
 * Where a popover of [popover] size goes next to [anchor]: the preferred side when it fits, else the
 * other side when that fits, else the roomier side clamped into the window. Horizontally it lines up
 * with the anchor's start edge and is clamped into the window.
 */
internal fun dsPopoverOffset(
    anchor: IntRect,
    window: IntSize,
    popover: IntSize,
    placement: DsPopoverPlacement,
    layoutDirection: LayoutDirection,
    gap: Int,
): IntOffset {
    val below = anchor.bottom + gap
    val above = anchor.top - gap - popover.height
    val fitsBelow = below + popover.height <= window.height
    val fitsAbove = above >= 0
    val y =
        when {
            placement == DsPopoverPlacement.Below && fitsBelow -> below
            placement == DsPopoverPlacement.Above && fitsAbove -> above
            fitsBelow -> below
            fitsAbove -> above
            window.height - below >= anchor.top - gap -> below
            else -> above
        }.coerceIn(0, maxOf(0, window.height - popover.height))
    val start = if (layoutDirection == LayoutDirection.Ltr) anchor.left else anchor.right - popover.width
    val x = start.coerceIn(0, maxOf(0, window.width - popover.width))
    return IntOffset(x, y)
}

@DsComponentPreview
@Composable
private fun DsPopoverPreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsSpacing.md)) {
            DsPopover(expanded = false, onDismissRequest = {}, anchor = {
                DsButton("Détails", onClick = {}, variant = DsButtonVariant.Outlined, size = DsButtonSize.Small)
            }) {
                DsText("Remise appliquée au ticket")
            }
            DsPopoverSurface {
                DsText("Remise appliquée au ticket")
            }
            DsPopoverSurface {
                Column(verticalArrangement = Arrangement.spacedBy(DsSpacing.sm)) {
                    DsText("Paiement fractionné", style = DsTheme.typography.title)
                    DsText(
                        "Le client règle une partie en carte et le reste en espèces. Le ticket indique chaque moyen de paiement.",
                        color = DsTheme.colors.textSecondary,
                    )
                    DsButton("Compris", onClick = {}, size = DsButtonSize.Small)
                }
            }
        }
    }
