package io.github.amine2233.designsystem.organisms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsElevation
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsMotion
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private const val EXPANDED_TRIGGER_ROTATION = 45f
private const val FULL_TURN = 360f
private val TriggerSize = DsSpacing.xxl + DsSpacing.sm
private val ActionSize = DsSpacing.minTouchTarget

/** One action fanned out by [DsCircleMenu]. Disabled actions are drawn muted and ignore taps. */
data class DsCircleMenuAction(
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
)

/**
 * Floating trigger that fans 2 to 8 [actions] out on an arc, drawn inline (no popup).
 *
 * Angles are in degrees: 0° points to the end edge and positive angles turn upwards. Actions sit from
 * [startAngle] to [startAngle] + [sweepAngle]; a sweep of 360° or more spreads them evenly around a full
 * circle. The default 90°→180° arc puts the trigger at the bottom-end corner and fans actions up and
 * towards the start. RTL mirrors the arc.
 *
 * The menu always measures the footprint of the open arc, so toggling never moves surrounding content.
 * The caller owns [expanded] and any dismissal (scrim, outside tap, back press). Tapping an action
 * calls its `onClick`, then `onExpandedChange(false)`.
 */
@Composable
fun DsCircleMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    actions: List<DsCircleMenuAction>,
    triggerContentDescription: String,
    modifier: Modifier = Modifier,
    triggerIcon: ImageVector = DsIcons.Add,
    startAngle: Float = 90f,
    sweepAngle: Float = 90f,
    radius: Dp = DsSpacing.xxxl + DsSpacing.xl,
    animate: Boolean = DsTheme.animationsEnabled,
) {
    require(actions.size in 2..8) { "DsCircleMenu supports 2 to 8 actions, got ${actions.size}" }
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val target = if (expanded) 1f else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(DsMotion.NORMAL, easing = DsMotion.emphasizedEasing),
        label = "circleMenuProgress",
    )
    val progress = if (animate) animatedProgress else target

    Layout(
        content = {
            CircleMenuTrigger(expanded, onExpandedChange, triggerIcon, triggerContentDescription, progress)
            if (progress > 0f) {
                actions.forEach { action ->
                    CircleMenuActionButton(action, interactive = expanded, progress = progress) {
                        action.onClick()
                        onExpandedChange(false)
                    }
                }
            }
        },
        modifier = modifier.clipToBounds(),
    ) { measurables, _ ->
        val triggerPx = TriggerSize.roundToPx()
        val actionPx = ActionSize.roundToPx()
        val triggerHalf = triggerPx / 2f
        val actionHalf = actionPx / 2f
        val slots = List(actions.size) { dsCircleMenuSlot(it, actions.size, startAngle, sweepAngle, radius.toPx(), rtl) }
        var minX = -triggerHalf
        var maxX = triggerHalf
        var minY = -triggerHalf
        var maxY = triggerHalf
        slots.forEach {
            minX = minOf(minX, it.x - actionHalf)
            maxX = maxOf(maxX, it.x + actionHalf)
            minY = minOf(minY, it.y - actionHalf)
            maxY = maxOf(maxY, it.y + actionHalf)
        }
        val triggerPlaceable = measurables.first().measure(Constraints.fixed(triggerPx, triggerPx))
        val actionPlaceables = measurables.drop(1).map { it.measure(Constraints.fixed(actionPx, actionPx)) }
        layout(ceil(maxX - minX).toInt(), ceil(maxY - minY).toInt()) {
            actionPlaceables.forEachIndexed { i, placeable ->
                val slot = slots[i] * progress
                placeable.place((slot.x - minX - actionHalf).roundToInt(), (slot.y - minY - actionHalf).roundToInt())
            }
            triggerPlaceable.place((-minX - triggerHalf).roundToInt(), (-minY - triggerHalf).roundToInt())
        }
    }
}

@Composable
private fun CircleMenuTrigger(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    icon: ImageVector,
    contentDescription: String,
    progress: Float,
) {
    val c = DsTheme.colors
    Box(
        Modifier
            .shadow(DsElevation.raised, DsShapes.pill)
            .background(c.primary, DsShapes.pill)
            .clickable(role = Role.Button) { onExpandedChange(!expanded) }
            .semantics {
                this.contentDescription = contentDescription
                stateDescription = if (expanded) "Ouvert" else "Fermé"
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = c.onPrimary, modifier = Modifier.rotate(EXPANDED_TRIGGER_ROTATION * progress))
    }
}

@Composable
private fun CircleMenuActionButton(
    action: DsCircleMenuAction,
    interactive: Boolean,
    progress: Float,
    onClick: () -> Unit,
) {
    val c = DsTheme.colors
    Box(
        Modifier
            .graphicsLayer { alpha = progress }
            .shadow(DsElevation.card, DsShapes.pill)
            .background(c.surface, DsShapes.pill)
            .border(BorderStroke(Dp.Hairline, c.border), DsShapes.pill)
            .then(
                if (interactive) {
                    Modifier.semantics { contentDescription = action.contentDescription }
                } else {
                    Modifier.clearAndSetSemantics {}
                },
            ).clickable(enabled = interactive && action.enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(action.icon, contentDescription = null, tint = if (action.enabled) c.textPrimary else c.textDisabled)
    }
}

/**
 * Centre of action [index] relative to the trigger centre, in the unit of [radius]. Degrees, 0° = end
 * edge, positive = up (y is negated for Compose's y-down space); [rtl] mirrors x.
 */
internal fun dsCircleMenuSlot(
    index: Int,
    count: Int,
    startAngle: Float,
    sweepAngle: Float,
    radius: Float,
    rtl: Boolean,
): Offset {
    val step = if (abs(sweepAngle) >= FULL_TURN) sweepAngle / count else sweepAngle / (count - 1)
    val radians = Math.toRadians((startAngle + step * index).toDouble())
    val x = radius * cos(radians).toFloat()
    val y = -radius * sin(radians).toFloat()
    return Offset(if (rtl) -x else x, y)
}

private val previewActions =
    listOf(
        DsCircleMenuAction(DsIcons.Edit, "Modifier", {}),
        DsCircleMenuAction(DsIcons.Print, "Imprimer", {}),
        DsCircleMenuAction(DsIcons.Delete, "Supprimer", {}),
    )

@DsComponentPreview
@Composable
private fun DsCircleMenuCollapsedPreview() =
    DsPreview {
        DsCircleMenu(false, {}, previewActions, "Actions", animate = false)
    }

@DsComponentPreview
@Composable
private fun DsCircleMenuExpandedPreview() =
    DsPreview {
        DsCircleMenu(true, {}, previewActions, "Actions", animate = false)
    }

@DsComponentPreview
@Composable
private fun DsCircleMenuFullCirclePreview() =
    DsPreview {
        DsCircleMenu(
            expanded = true,
            onExpandedChange = {},
            actions =
                previewActions +
                    listOf(
                        DsCircleMenuAction(DsIcons.Settings, "Réglages", {}),
                        DsCircleMenuAction(DsIcons.Receipt, "Ticket", {}),
                        DsCircleMenuAction(DsIcons.Discount, "Remise", {}),
                    ),
            triggerContentDescription = "Actions",
            startAngle = 90f,
            sweepAngle = 360f,
            animate = false,
        )
    }

@DsComponentPreview
@Composable
private fun DsCircleMenuDisabledActionPreview() =
    DsPreview {
        DsCircleMenu(
            expanded = true,
            onExpandedChange = {},
            actions = previewActions.dropLast(1) + DsCircleMenuAction(DsIcons.Delete, "Supprimer", {}, enabled = false),
            triggerContentDescription = "Actions",
            animate = false,
        )
    }
