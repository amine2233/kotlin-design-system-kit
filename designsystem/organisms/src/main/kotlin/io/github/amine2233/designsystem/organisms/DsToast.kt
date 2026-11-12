package io.github.amine2233.designsystem.organisms

import android.view.accessibility.AccessibilityManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsMotion
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class DsToastStyle { Info, Success, Warning, Error }

data class DsToastMessage(
    val message: String,
    val style: DsToastStyle = DsToastStyle.Info,
    val title: String? = null,
    val duration: Duration = DsToastDefaults.Duration,
    val id: String = UUID.randomUUID().toString(),
)

object DsToastDefaults {
    val Duration = 4.seconds
    val AccessibleMinimum = 10.seconds
}

/** A toast always auto-dismisses; a screen-reader user gets at least [DsToastDefaults.AccessibleMinimum] to hear it. */
fun dsToastDismissDelay(
    duration: Duration,
    isScreenReaderOn: Boolean,
): Duration {
    val base = if (duration.isPositive()) duration else DsToastDefaults.Duration
    return if (isScreenReaderOn) maxOf(base, DsToastDefaults.AccessibleMinimum) else base
}

/** FIFO queue: one [current] toast, the rest waiting in [pending]. Re-showing an id updates it in place. */
data class DsToastQueueState(
    val current: DsToastMessage? = null,
    val pending: List<DsToastMessage> = emptyList(),
) {
    fun enqueue(message: DsToastMessage): DsToastQueueState =
        when {
            current == null || current.id == message.id -> copy(current = message)
            pending.any { it.id == message.id } -> copy(pending = pending.map { if (it.id == message.id) message else it })
            else -> copy(pending = pending + message)
        }

    fun dismiss(id: String): DsToastQueueState =
        when {
            current?.id == id -> {
                val next = pending.firstOrNull()
                DsToastQueueState(current = next, pending = pending.filterNot { it.id == next?.id })
            }

            pending.any { it.id == id } -> {
                copy(pending = pending.filterNot { it.id == id })
            }

            else -> {
                this
            }
        }
}

@Stable
class DsToastHostState {
    private var state by mutableStateOf(DsToastQueueState())

    val queue: DsToastQueueState get() = state

    fun show(message: DsToastMessage) {
        state = state.enqueue(message)
    }

    fun dismiss(id: String) {
        state = state.dismiss(id)
    }
}

@Composable
fun rememberDsToastHostState(): DsToastHostState = remember { DsToastHostState() }

/** Shows [state]'s current toast; tap or swipe it away, otherwise it leaves after [dsToastDismissDelay]. */
@Composable
fun DsToastHost(
    state: DsToastHostState,
    modifier: Modifier = Modifier,
    animate: Boolean = DsTheme.animationsEnabled,
) {
    val current = state.queue.current
    val context = LocalContext.current
    val accessibility = remember(context) { context.getSystemService(AccessibilityManager::class.java) }
    val isScreenReaderOn = accessibility?.isTouchExplorationEnabled == true

    if (current != null) {
        LaunchedEffect(current.id) {
            delay(dsToastDismissDelay(current.duration, isScreenReaderOn))
            state.dismiss(current.id)
        }
    }

    AnimatedContent(
        targetState = current,
        modifier = modifier,
        transitionSpec = {
            if (animate) {
                val enter =
                    fadeIn(tween(DsMotion.NORMAL, easing = DsMotion.standardEasing)) +
                        slideInVertically(tween(DsMotion.NORMAL, easing = DsMotion.standardEasing))
                val exit =
                    fadeOut(tween(DsMotion.FAST, easing = DsMotion.standardEasing)) +
                        slideOutVertically(tween(DsMotion.FAST, easing = DsMotion.standardEasing))
                enter togetherWith exit
            } else {
                EnterTransition.None togetherWith ExitTransition.None
            }
        },
        contentKey = { it?.id },
        label = "DsToastHost",
    ) { toast ->
        if (toast != null) {
            key(toast.id) {
                val swipe = rememberSwipeToDismissBoxState()
                SwipeToDismissBox(
                    state = swipe,
                    backgroundContent = {},
                    modifier = Modifier.padding(DsSpacing.md),
                    onDismiss = { state.dismiss(toast.id) },
                ) {
                    DsToast(
                        message = toast.message,
                        style = toast.style,
                        title = toast.title,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .semantics { liveRegion = LiveRegionMode.Polite }
                                .clip(DsShapes.md)
                                .clickable(onClickLabel = "Fermer") { state.dismiss(toast.id) },
                    )
                }
            }
        }
    }
}

/** Stateless toast surface: informational only, no action — use [DsSnackbar] when the user can act on it. */
@Composable
fun DsToast(
    message: String,
    modifier: Modifier = Modifier,
    style: DsToastStyle = DsToastStyle.Info,
    title: String? = null,
) {
    val c = DsTheme.colors
    val (container, tint, icon) =
        when (style) {
            DsToastStyle.Info -> Triple(c.infoContainer, c.info, DsIcons.Info)
            DsToastStyle.Success -> Triple(c.successContainer, c.success, DsIcons.Success)
            DsToastStyle.Warning -> Triple(c.warningContainer, c.warning, DsIcons.Warning)
            DsToastStyle.Error -> Triple(c.errorContainer, c.error, DsIcons.Error)
        }
    Surface(modifier = modifier, shape = DsShapes.md, color = container, contentColor = c.textPrimary) {
        Row(
            modifier = Modifier.heightIn(min = DsSpacing.minTouchTarget).padding(horizontal = DsSpacing.md, vertical = DsSpacing.smd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DsSpacing.smd),
        ) {
            Icon(icon, contentDescription = null, tint = tint)
            Column(verticalArrangement = Arrangement.spacedBy(DsSpacing.xs)) {
                if (title != null) Text(title, style = DsTheme.typography.bodyStrong, color = c.textPrimary)
                Text(message, style = DsTheme.typography.body, color = c.textPrimary)
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsToastStylesPreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsSpacing.sm)) {
            DsToast("Synchronisation terminée", style = DsToastStyle.Info)
            DsToast("Ticket imprimé", style = DsToastStyle.Success)
            DsToast("Papier bientôt épuisé", style = DsToastStyle.Warning)
            DsToast("Terminal injoignable", style = DsToastStyle.Error)
        }
    }

@DsComponentPreview
@Composable
private fun DsToastTitlePreview() =
    DsPreview {
        DsToast("La commande a été transmise à la cuisine.", style = DsToastStyle.Success, title = "Commande envoyée")
    }

@DsComponentPreview
@Composable
private fun DsToastLongMessagePreview() =
    DsPreview {
        DsToast(
            "La connexion au terminal de paiement a été perdue. Les ventes en espèces restent possibles en attendant " +
                "le retour du réseau.",
            style = DsToastStyle.Warning,
        )
    }
