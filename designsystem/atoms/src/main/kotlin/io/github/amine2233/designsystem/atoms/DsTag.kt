package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

enum class DsTagTone { Neutral, Primary, Success, Warning, Error, Info }

/** Read-only status pill ("Terminal connecté", "En attente", "Payé"). Non-interactive — use [DsChip] for selection. */
@Composable
fun DsTag(
    text: String,
    modifier: Modifier = Modifier,
    tone: DsTagTone = DsTagTone.Neutral,
) {
    val c = DsTheme.colors
    val (bg, fg) =
        when (tone) {
            DsTagTone.Neutral -> c.surfaceMuted to c.textSecondary
            DsTagTone.Primary -> c.primaryContainer to c.primary
            DsTagTone.Success -> c.successContainer to c.success
            DsTagTone.Warning -> c.warningContainer to c.warning
            DsTagTone.Error -> c.errorContainer to c.error
            DsTagTone.Info -> c.infoContainer to c.info
        }
    Text(
        text,
        modifier = modifier.background(bg, DsShapes.pill).padding(horizontal = 8.dp, vertical = 3.dp),
        style = DsTheme.typography.overline,
        color = fg,
        maxLines = 1,
    )
}

@DsComponentPreview
@Composable
private fun DsTagPreview() =
    DsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            DsTag("Neutre")
            DsTag("Terminal", tone = DsTagTone.Primary)
            DsTag("Payé", tone = DsTagTone.Success)
            DsTag("En attente", tone = DsTagTone.Warning)
            DsTag("Échec", tone = DsTagTone.Error)
            DsTag("Info", tone = DsTagTone.Info)
        }
    }
