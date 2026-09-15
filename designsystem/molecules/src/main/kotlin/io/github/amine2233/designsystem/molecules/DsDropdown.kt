package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Select field ("Motif : Fidélité ▾", TVA rate per line). */
@Composable
fun DsDropdown(
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Sélectionner",
    enabled: Boolean = true,
) {
    val c = DsTheme.colors
    var open by remember { mutableStateOf(false) }
    Column(modifier) {
        if (label != null) {
            Text(label, style = DsTheme.typography.caption, color = c.textSecondary)
            Spacer(Modifier.height(4.dp))
        }
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp)
                    .clip(DsShapes.sm)
                    .background(c.surfaceSubtle)
                    .border(1.dp, if (open) c.primary else c.border, DsShapes.sm)
                    .clickable(enabled = enabled) { open = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val value = selectedIndex?.let { options.getOrNull(it) }
                Text(
                    value ?: placeholder,
                    style = DsTheme.typography.bodyStrong,
                    color = if (value == null) c.textTertiary else c.textPrimary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = c.textSecondary)
            }
            DropdownMenu(expanded = open, onDismissRequest = { open = false }, containerColor = c.surface, shape = DsShapes.md) {
                options.forEachIndexed { i, option ->
                    DropdownMenuItem(
                        text = { Text(option, style = DsTheme.typography.body, color = if (i == selectedIndex) c.primary else c.textPrimary) },
                        onClick = { open = false; onSelect(i) },
                    )
                }
            }
        }
    }
}
