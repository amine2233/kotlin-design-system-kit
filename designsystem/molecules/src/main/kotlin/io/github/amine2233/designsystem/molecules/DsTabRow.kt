package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsBadge
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** One tab: its label, an optional count, an optional icon. */
@Immutable
data class DsTabItem(
    val label: String,
    val badge: String? = null,
    val icon: ImageVector? = null,
)

/**
 * Past this many tabs the row scrolls instead of dividing the width further; below it, equal
 * widths keep the set readable as one thing.
 */
private const val SCROLLABLE_FROM = 4

/** Underlined tabs (Catalogue groups, orders filters). For a toggle use [DsSegmentedControl]. */
@Composable
fun DsTabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    DsTabRow(tabs.map(::DsTabItem), selectedIndex, onSelect, modifier)
}

/**
 * Tabs carrying a count or an icon, and scrolling once there are too many to divide the width.
 *
 * [scrollable] defaults to "more than four": equal widths stop being readable past that on a phone,
 * and a scrolling row keeps each label whole instead of truncating every one of them. Force it
 * either way when the screen knows better — a tablet pane with six short labels does not need to
 * scroll.
 */
@Composable
fun DsTabRow(
    items: List<DsTabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = items.size > SCROLLABLE_FROM,
) {
    val c = DsTheme.colors
    val tabs: @Composable () -> Unit = {
        items.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            Tab(
                selected = selected,
                onClick = { onSelect(index) },
                selectedContentColor = c.primary,
                unselectedContentColor = c.textSecondary,
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (item.icon != null) {
                            Icon(item.icon, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            item.label,
                            style = if (selected) DsTheme.typography.labelStrong else DsTheme.typography.label,
                            maxLines = 1,
                        )
                        if (item.badge != null) {
                            DsBadge(
                                item.badge,
                                containerColor = if (selected) c.primary else c.surfaceMuted,
                                contentColor = if (selected) c.onPrimary else c.textSecondary,
                            )
                        }
                    }
                },
            )
        }
    }
    val indicator: @Composable (List<TabPosition>) -> Unit = { positions ->
        TabRowDefaults.SecondaryIndicator(
            Modifier.tabIndicatorOffset(positions[selectedIndex]),
            height = 2.dp,
            color = c.primary,
        )
    }
    if (scrollable) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            modifier = modifier,
            containerColor = c.surface,
            contentColor = c.primary,
            edgePadding = DsTheme.spacing.smd,
            indicator = indicator,
            divider = { HorizontalDivider(color = c.border) },
            tabs = tabs,
        )
    } else {
        TabRow(
            selectedTabIndex = selectedIndex,
            modifier = modifier,
            containerColor = c.surface,
            contentColor = c.primary,
            indicator = indicator,
            divider = { HorizontalDivider(color = c.border) },
            tabs = tabs,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsTabRowPreview() =
    DsPreview {
        DsTabRow(listOf("Boissons", "Nourriture", "Extras"), 0, {})
        DsTabRow(
            listOf(
                DsTabItem("En cours", badge = "3"),
                DsTabItem("Prêtes", badge = "12"),
                DsTabItem("Servies"),
            ),
            selectedIndex = 0,
            onSelect = {},
        )
        DsTabRow(
            listOf(
                DsTabItem("Vente", icon = DsIcons.Register),
                DsTabItem("Commandes", icon = DsIcons.Receipt, badge = "2"),
                DsTabItem("Catalogue", icon = DsIcons.Catalogue),
                DsTabItem("Clients", icon = DsIcons.Account),
                DsTabItem("Réglages", icon = DsIcons.Settings),
            ),
            selectedIndex = 1,
            onSelect = {},
        )
    }
