package io.github.amine2233.designsystem.organisms

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import io.github.amine2233.designsystem.atoms.DsAvatar
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsMotion
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled

data class DsStaffMember(
    val id: String,
    val name: String,
    val initials: String,
)

sealed interface DsStaffPinState {
    data object Entering : DsStaffPinState

    data class Error(
        val message: String,
    ) : DsStaffPinState

    data class Locked(
        val message: String,
    ) : DsStaffPinState
}

object DsStaffPinDefaults {
    val PinLength: Int = 4
}

/** Applies a key press to a PIN: digits append up to [length], backspace drops the last one, comma is a no-op. */
fun dsApplyPinKey(
    pin: String,
    key: DsKey,
    length: Int = DsStaffPinDefaults.PinLength,
): String =
    when (key) {
        is DsKey.Digit -> if (pin.length < length) pin + key.value else pin
        DsKey.Backspace -> pin.dropLast(1)
        DsKey.Comma -> pin
    }

/** Horizontal offsets of the error shake, as fractions of the amplitude; it ends at rest. */
private val ShakeSteps = listOf(1f, -1f, 0.5f, -0.5f, 0f)

/**
 * Staff sign-in for a shared terminal: pick a staff member, then type a PIN on the keypad (no comma key).
 *
 * Stateless and PIN-blind: the caller keeps the PIN (apply keys with [dsApplyPinKey]), passes only
 * [enteredCount], verifies it, counts attempts and runs any lockout timer, then reports the outcome in [state].
 * The keypad is disabled until [selectedStaffId] names a member of [staff], and while [state] is
 * [DsStaffPinState.Locked], which also disables staff selection.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DsStaffPinLogin(
    staff: List<DsStaffMember>,
    selectedStaffId: String?,
    onSelectStaff: (DsStaffMember) -> Unit,
    enteredCount: Int,
    onKey: (DsKey) -> Unit,
    modifier: Modifier = Modifier,
    state: DsStaffPinState = DsStaffPinState.Entering,
    pinLength: Int = DsStaffPinDefaults.PinLength,
    animate: Boolean = DsTheme.animationsEnabled,
    emptyStaffLabel: String = "No staff members",
    pinProgressLabel: (entered: Int, total: Int) -> String = { e, t -> "$e of $t digits entered" },
    keypadRowModifier: Modifier = Modifier,
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val spacing = DsTheme.spacing
    val locked = state is DsStaffPinState.Locked
    val keypadEnabled = !locked && staff.any { it.id == selectedStaffId }
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (staff.isEmpty()) {
            Text(emptyStaffLabel, style = t.body, color = c.textSecondary)
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth().selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(spacing.sm, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                staff.forEach { member ->
                    StaffItem(member, selected = member.id == selectedStaffId, enabled = !locked, onClick = { onSelectStaff(member) })
                }
            }
        }
        PinDots(enteredCount, pinLength, state, animate, pinProgressLabel)
        when (state) {
            DsStaffPinState.Entering -> Unit
            is DsStaffPinState.Error -> PinMessage(state.message, c.error)
            is DsStaffPinState.Locked -> PinMessage(state.message, c.textSecondary)
        }
        DsNumericKeypad(onKey = onKey, rowModifier = keypadRowModifier, showComma = false, enabled = keypadEnabled)
    }
}

@Composable
private fun StaffItem(
    member: DsStaffMember,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val c = DsTheme.colors
    val spacing = DsTheme.spacing
    Column(
        modifier =
            Modifier
                .sizeIn(minWidth = spacing.minTouchTarget, minHeight = spacing.minTouchTarget)
                .clip(DsShapes.md)
                .background(if (selected) c.primaryContainer else c.surface)
                .border(spacing.xxs, if (selected) c.primary else c.border, DsShapes.md)
                .selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick)
                .padding(spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        DsAvatar(member.initials, Modifier.clearAndSetSemantics {})
        Text(
            member.name,
            style = DsTheme.typography.label,
            color = if (enabled) c.textPrimary else c.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PinDots(
    enteredCount: Int,
    pinLength: Int,
    state: DsStaffPinState,
    animate: Boolean,
    pinProgressLabel: (entered: Int, total: Int) -> String,
) {
    val c = DsTheme.colors
    val spacing = DsTheme.spacing
    val count = enteredCount.coerceIn(0, pinLength)
    val dotColor = if (state is DsStaffPinState.Error) c.error else c.primary
    val shake = remember { Animatable(0f) }
    val amplitude = with(LocalDensity.current) { DsSpacing.sm.toPx() }
    LaunchedEffect(state, animate) {
        if (animate && state is DsStaffPinState.Error) {
            ShakeSteps.forEach { step -> shake.animateTo(step, tween(DsMotion.NORMAL / ShakeSteps.size, easing = DsMotion.standardEasing)) }
        } else {
            shake.snapTo(0f)
        }
    }
    Row(
        modifier =
            Modifier
                .graphicsLayer { translationX = shake.value * amplitude }
                .clearAndSetSemantics { contentDescription = pinProgressLabel(count, pinLength) },
        horizontalArrangement = Arrangement.spacedBy(spacing.smd),
    ) {
        repeat(pinLength) { index ->
            val dot = Modifier.size(spacing.smd).clip(DsShapes.pill)
            Box(if (index < count) dot.background(dotColor) else dot.border(spacing.xxs, dotColor, DsShapes.pill))
        }
    }
}

@Composable
private fun PinMessage(
    message: String,
    color: Color,
) {
    Text(
        message,
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
        style = DsTheme.typography.body,
        color = color,
        textAlign = TextAlign.Center,
    )
}

private val previewStaff =
    listOf(
        DsStaffMember("1", "Amina", "AB"),
        DsStaffMember("2", "Karim", "KD"),
        DsStaffMember("3", "Sonia", "SL"),
    )

@Composable
private fun StaffPinPreview(
    selectedStaffId: String?,
    enteredCount: Int = 0,
    state: DsStaffPinState = DsStaffPinState.Entering,
    staff: List<DsStaffMember> = previewStaff,
) = DsStaffPinLogin(
    staff = staff,
    selectedStaffId = selectedStaffId,
    onSelectStaff = {},
    enteredCount = enteredCount,
    onKey = {},
    state = state,
)

@DsComponentPreview
@Composable
private fun DsStaffPinLoginNoSelectionPreview() = DsPreview { StaffPinPreview(selectedStaffId = null) }

@DsComponentPreview
@Composable
private fun DsStaffPinLoginEnteringPreview() = DsPreview { StaffPinPreview(selectedStaffId = "2", enteredCount = 2) }

@DsComponentPreview
@Composable
private fun DsStaffPinLoginErrorPreview() =
    DsPreview {
        StaffPinPreview(selectedStaffId = "2", enteredCount = 4, state = DsStaffPinState.Error("Wrong PIN. Try again."))
    }

@DsComponentPreview
@Composable
private fun DsStaffPinLoginLockedPreview() =
    DsPreview {
        StaffPinPreview(selectedStaffId = "2", state = DsStaffPinState.Locked("Too many attempts. Try again in 0:45."))
    }

@DsComponentPreview
@Composable
private fun DsStaffPinLoginEmptyPreview() = DsPreview { StaffPinPreview(selectedStaffId = null, staff = emptyList()) }

@DsComponentPreview
@Composable
private fun DsStaffPinLoginWrappingPreview() =
    DsPreview {
        StaffPinPreview(
            selectedStaffId = "5",
            staff =
                previewStaff +
                    listOf(
                        DsStaffMember("4", "Yacine", "YM"),
                        DsStaffMember("5", "Lina", "LH"),
                        DsStaffMember("6", "Mehdi", "MR"),
                        DsStaffMember("7", "Nadia", "NO"),
                    ),
        )
    }
