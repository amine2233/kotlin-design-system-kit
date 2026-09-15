package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsChip

/** Horizontally scrolling single-select chips (categories, TVA rates, quick tips). */
@Composable
fun DsChipRow(
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (scrollable) Modifier.horizontalScroll(rememberScrollState()) else Modifier)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        options.forEachIndexed { i, label ->
            DsChip(
                label,
                selected = i == selectedIndex,
                onClick = { onSelect(i) },
                modifier = if (scrollable) Modifier else Modifier.weight(1f),
                contentPadding = if (scrollable) PaddingValues(horizontal = 14.dp, vertical = 6.dp) else PaddingValues(horizontal = 4.dp, vertical = 8.dp),
            )
        }
    }
}
