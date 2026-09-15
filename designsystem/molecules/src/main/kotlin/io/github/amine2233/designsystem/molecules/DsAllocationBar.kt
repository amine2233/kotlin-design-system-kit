package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsPalette
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

@Immutable
data class DsAllocation(val label: String, val fraction: Float, val color: Color? = null)

/** Split-payment proportional bar with legend ("Carte 800 DA · Espèces 388 DA"). Remainder shown as track. */
@Composable
fun DsAllocationBar(parts: List<DsAllocation>, modifier: Modifier = Modifier) {
    val c = DsTheme.colors
    val defaults = listOf(c.primary, DsPalette.Teal200, c.warning, c.info)
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth().height(10.dp).clip(DsShapes.pill).background(c.border)) {
            parts.forEachIndexed { i, p ->
                if (p.fraction > 0f) Box(Modifier.fillMaxHeight().weight(p.fraction).background(p.color ?: defaults[i % defaults.size]))
            }
            val rest = 1f - parts.sumOf { it.fraction.toDouble() }.toFloat()
            if (rest > 0f) Box(Modifier.weight(rest))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            parts.forEachIndexed { i, p ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Box(Modifier.size(8.dp).background(p.color ?: defaults[i % defaults.size], DsShapes.pill))
                    Text(p.label, style = DsTheme.typography.caption, color = c.textSecondary)
                }
            }
        }
    }
}
