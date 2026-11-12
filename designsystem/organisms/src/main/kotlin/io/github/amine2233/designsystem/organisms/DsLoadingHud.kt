package io.github.amine2233.designsystem.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import io.github.amine2233.designsystem.atoms.DsSpinner
import io.github.amine2233.designsystem.atoms.DsSpinnerSize
import io.github.amine2233.designsystem.atoms.DsSpinnerStyle
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsElevation
import io.github.amine2233.designsystem.core.DsMotion
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled

enum class DsLoadingHudState { Loading, Success, Failure }

// Caps the card so a long message wraps instead of stretching across a tablet.
private val HudMaxWidth = DsSpacing.xxxl * 4

internal fun dsLoadingHudOutcome(state: DsLoadingHudState): DsPaymentOutcome? =
    when (state) {
        DsLoadingHudState.Loading -> null
        DsLoadingHudState.Success -> DsPaymentOutcome.Success
        DsLoadingHudState.Failure -> DsPaymentOutcome.Failure
    }

internal fun dsLoadingHudLabel(
    state: DsLoadingHudState,
    message: String?,
    contentDescription: String?,
): String =
    message?.takeIf { it.isNotBlank() }
        ?: contentDescription?.takeIf { it.isNotBlank() }
        ?: when (state) {
            DsLoadingHudState.Loading -> "Chargement"
            DsLoadingHudState.Success -> "Succès"
            DsLoadingHudState.Failure -> "Échec"
        }

/**
 * The HUD card on its own: spinner or result mark, then the optional message. Draw it inline where a
 * popup cannot render (previews, snapshots); in the app use [DsLoadingHud].
 *
 * One live-region node, so TalkBack reads the label once and announces the switch to success or
 * failure without moving focus.
 */
@Composable
fun DsLoadingHudCard(
    state: DsLoadingHudState,
    modifier: Modifier = Modifier,
    message: String? = null,
    contentDescription: String? = null,
    animate: Boolean = DsTheme.animationsEnabled,
    progress: Float? = if (DsTheme.animationsEnabled) null else 1f,
) {
    val label = dsLoadingHudLabel(state, message, contentDescription)
    val outcome = dsLoadingHudOutcome(state)
    Column(
        modifier
            .widthIn(max = HudMaxWidth)
            .shadow(DsElevation.raised, DsShapes.lg)
            .background(DsTheme.colors.surface, DsShapes.lg)
            .padding(DsSpacing.lg)
            .clearAndSetSemantics {
                this.contentDescription = label
                liveRegion = LiveRegionMode.Polite
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(DsSpacing.md),
    ) {
        if (outcome == null) {
            DsSpinner(size = DsSpinnerSize.ExtraLarge, style = DsSpinnerStyle.Primary, animate = animate)
        } else {
            DsResultMark(outcome, size = DsSpinnerSize.ExtraLarge.diameter, progress = progress)
        }
        if (message != null) {
            Text(message, style = DsTheme.typography.body, color = DsTheme.colors.textPrimary, textAlign = TextAlign.Center)
        }
    }
}

/**
 * Blocking overlay: a scrim over the whole screen with [DsLoadingHudCard] centred on it.
 *
 * It lives in its own modal window, so touches and accessibility focus never reach the screen behind,
 * and neither back nor a tap outside hides it — the caller owns [visible] and every timing.
 */
@Composable
fun DsLoadingHud(
    visible: Boolean,
    state: DsLoadingHudState,
    modifier: Modifier = Modifier,
    message: String? = null,
    contentDescription: String? = null,
    animate: Boolean = DsTheme.animationsEnabled,
    progress: Float? = if (DsTheme.animationsEnabled) null else 1f,
) {
    val transition = remember { MutableTransitionState(false) }
    transition.targetState = visible
    if (!transition.currentState && !transition.targetState) return

    Dialog(
        onDismissRequest = {},
        properties =
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
    ) {
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect { window?.setDimAmount(0f) }
        val spec = if (animate) tween<Float>(DsMotion.NORMAL, easing = DsMotion.standardEasing) else snap()
        AnimatedVisibility(transition, enter = fadeIn(spec), exit = fadeOut(spec)) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(DsTheme.colors.scrim)
                    .padding(DsSpacing.xl),
                contentAlignment = Alignment.Center,
            ) {
                DsLoadingHudCard(state, modifier, message, contentDescription, animate, progress)
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsLoadingHudCardPreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsSpacing.md)) {
            DsLoadingHudCard(DsLoadingHudState.Loading, message = "Paiement en cours…", animate = false, progress = 1f)
            DsLoadingHudCard(DsLoadingHudState.Loading, animate = false, progress = 1f)
            DsLoadingHudCard(DsLoadingHudState.Success, message = "Paiement accepté", animate = false, progress = 1f)
            DsLoadingHudCard(DsLoadingHudState.Failure, message = "Paiement refusé", animate = false, progress = 1f)
            DsLoadingHudCard(
                DsLoadingHudState.Loading,
                message = "Synchronisation du catalogue avec le serveur, cela peut prendre quelques instants",
                animate = false,
                progress = 1f,
            )
        }
    }
