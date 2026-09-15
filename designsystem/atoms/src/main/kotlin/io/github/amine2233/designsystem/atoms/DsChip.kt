package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

enum class DsChipTone { Neutral, Primary, Warning, Error }

/** Pill chip: selected = filled primary, unselected = subtle surface with border. */
@Composable
fun DsChip(
    label: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    tone: DsChipTone = DsChipTone.Neutral,
    shape: Shape = DsShapes.chip,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
) {
    val c = DsTheme.colors
    val (bg, fg, border) = when {
        selected -> Triple(c.primary, c.onPrimary, Color.Transparent)
        tone == DsChipTone.Primary -> Triple(c.primaryContainer, c.primary, c.primaryContainerBorder)
        tone == DsChipTone.Warning -> Triple(c.warningContainer, c.warning, c.warningBorder)
        tone == DsChipTone.Error -> Triple(c.errorContainer, c.error, c.errorBorder)
        else -> Triple(c.surfaceSubtle, c.textSecondary, c.border)
    }
    Box(
        modifier = modifier
            .heightIn(min = 32.dp)
            .clip(shape)
            .background(bg)
            .border(1.dp, border, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = if (selected) DsTheme.typography.labelStrong else DsTheme.typography.label,
            color = fg,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}
