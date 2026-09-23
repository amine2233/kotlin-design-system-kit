package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.DsWindowSize

/** How much room a modal takes: half the screen for a quick choice, nearly all of it for a task. */
enum class DsModalSize { Half, Full }

/**
 * Fraction of the available height a modal fills.
 *
 * Half is deliberately not exactly 0.5 on a phone: the sheet has to clear the status bar and still
 * show its header, and 0.5 leaves a strip of list peeking that reads as a rendering bug. On a
 * tablet the modal is a centred dialog, so the fractions are of the window, not of a sheet.
 */
fun dsModalHeightFraction(
    size: DsModalSize,
    windowSize: DsWindowSize,
): Float =
    when {
        size == DsModalSize.Full -> if (windowSize.isExpanded) 0.9f else 0.95f
        windowSize.isExpanded -> 0.5f
        else -> 0.55f
    }

/**
 * A modal that is a bottom sheet on a phone and a centred dialog on a tablet.
 *
 * That switch is the point of the component: a full-width sheet crawling up a 960dp tablet reads as
 * a phone app stretched, and a small dialog on a phone wastes the width. [DsModalSize.Half] is for
 * a choice the user makes and leaves; [DsModalSize.Full] for a task they work inside.
 *
 * The header is always present — title plus a close button — because a sheet without one leaves
 * dragging as the only way out, which a first-time user does not know.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsModal(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    size: DsModalSize = DsModalSize.Half,
    windowSize: DsWindowSize = DsTheme.windowSize,
    closeContentDescription: String = "Fermer",
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val fraction = dsModalHeightFraction(size, windowSize)
    val body: @Composable ColumnScope.() -> Unit = {
        DsModalHeader(title, onDismiss, closeContentDescription)
        DsDivider()
        Column(
            Modifier
                .weight(1f, fill = false)
                .padding(DsTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
            content = content,
        )
        if (footer != null) {
            DsDivider()
            Column(Modifier.padding(DsTheme.spacing.md)) { footer() }
        }
    }
    if (windowSize.isExpanded) {
        Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Column(
                modifier
                    .widthIn(max = 560.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(fraction)
                    .clip(DsShapes.lg)
                    .background(DsTheme.colors.surface),
                content = body,
            )
        }
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier.fillMaxHeight(fraction),
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = DsShapes.sheet,
            containerColor = DsTheme.colors.surface,
            scrimColor = DsTheme.colors.scrim,
            dragHandle = { DsSheetHandle() },
            content = body,
        )
    }
}

@Composable
private fun DsModalHeader(
    title: String,
    onDismiss: () -> Unit,
    closeContentDescription: String,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = DsTheme.spacing.md, end = DsTheme.spacing.sm, top = DsTheme.spacing.sm, bottom = DsTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DsText(title, style = DsTheme.typography.title, modifier = Modifier.weight(1f), maxLines = 1)
        DsIconButton(DsIcons.Close, contentDescription = closeContentDescription, onClick = onDismiss)
    }
}

@DsScreenPreview
@Composable
private fun DsModalPreview() =
    DsTheme {
        DsModal(title = "Actions sur la ligne", onDismiss = {}, size = DsModalSize.Half) {
            DsText("Modifier la quantité, appliquer une remise, retirer l'article.")
        }
    }
