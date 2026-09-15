package io.github.amine2233.designsystem.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Catalogue phone layout: collapsible card section with title, optional summary and trailing slot. */
@Composable
fun DsExpandableSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val c = DsTheme.colors
    DsCard(modifier.fillMaxWidth()) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .clickable(onClick = onToggle)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column(Modifier.weight(1f)) {
                    Text(title, style = DsTheme.typography.title, color = c.textPrimary)
                    if (summary != null &&
                        !expanded
                    ) {
                        Text(summary, style = DsTheme.typography.caption, color = c.textSecondary, maxLines = 1)
                    }
                }
                trailing?.invoke()
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = c.textSecondary,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f),
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    DsDivider()
                    Column(Modifier.padding(14.dp)) { content() }
                }
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsExpandableSectionPreview() =
    DsPreview {
        DsExpandableSection("Produits", expanded = true, onToggle = {}, summary = "12 produits") { DsCheckRow("Contenu de la section") }
        Spacer(Modifier.height(6.dp))
        DsExpandableSection("Catégories", expanded = false, onToggle = {}, summary = "Chauds · Froids · Snacks") {}
    }
