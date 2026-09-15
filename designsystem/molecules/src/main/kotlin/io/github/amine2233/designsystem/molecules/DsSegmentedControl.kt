package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Teal-outlined toggle ("Panier complet | Article spécifique", "% | DA"). */
@Composable
fun DsSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = DsShapes.md,
    height: Dp = 40.dp,
) {
    val c = DsTheme.colors
    Row(
        modifier =
            modifier
                .height(height)
                .clip(shape)
                .border(1.5.dp, c.primary, shape),
    ) {
        options.forEachIndexed { index, label ->
            if (index > 0) Box(Modifier.width(1.5.dp).fillMaxHeight().background(c.primary))
            Segment(label, selected = index == selectedIndex) { onSelect(index) }
        }
    }
}

@Composable
private fun RowScope.Segment(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val c = DsTheme.colors
    Box(
        modifier =
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(if (selected) c.primary else c.surface)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = if (selected) DsTheme.typography.labelStrong else DsTheme.typography.label,
            color = if (selected) c.onPrimary else c.primary,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsSegmentedControlPreview() =
    DsPreview {
        DsSegmentedControl(listOf("Panier complet", "Article spécifique"), 0, {}, Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        DsSegmentedControl(listOf("% Pourcentage", "DA Montant fixe"), 1, {}, Modifier.fillMaxWidth(), shape = DsShapes.sm, height = 34.dp)
    }
