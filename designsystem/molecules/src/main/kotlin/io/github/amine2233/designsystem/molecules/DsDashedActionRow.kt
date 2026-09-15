package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Dashed teal "add" row ("+ Montant libre / pavé numérique", "+ Ajouter une option"). */
@Composable
fun DsDashedActionRow(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val c = DsTheme.colors
    val stroke = with(LocalDensity.current) { 1.5.dp.toPx() }
    val dash = with(LocalDensity.current) { 6.dp.toPx() }
    val radius = with(LocalDensity.current) { 16.dp.toPx() }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .clip(DsShapes.lg)
            .drawBehind {
                drawRoundRect(
                    color = c.primary,
                    cornerRadius = CornerRadius(radius),
                    style = Stroke(width = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash))),
                )
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(Modifier.size(24.dp).background(c.primary, DsShapes.pill), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Add, contentDescription = null, tint = c.onPrimary, modifier = Modifier.size(16.dp))
        }
        Text(text, style = DsTheme.typography.bodyStrong, color = c.primary)
    }
}

@DsComponentPreview
@Composable
private fun DsDashedActionRowPreview() = DsPreview {
    DsDashedActionRow("Montant libre / pavé numérique", onClick = {})
}
