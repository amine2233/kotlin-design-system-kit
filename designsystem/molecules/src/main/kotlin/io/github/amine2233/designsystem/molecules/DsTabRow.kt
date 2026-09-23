package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
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
 * How the tabs share the width.
 *
 * [Grid] wraps onto as many lines as it needs and never scrolls, each tab as wide as its own label.
 * [Inline] keeps one line and scrolls, so a long set stays on a single row instead of stacking.
 * Neither stretches a tab to a column: "Prêtes" should not be as wide as "Commandes".
 *
 * This is DsTabRow's own enum; a sibling that lays items out the same way declares its own, so
 * gaining a layout here never forces it on chips or tags.
 */
enum class DsTabLayout { Inline, Grid }

/** Past this many tabs equal widths stop being readable on a phone, so the row goes [DsTabLayout.Inline]. */
private const val GRID_UP_TO = 4

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
 * [layout] defaults to "more than four tabs scroll": equal widths stop being readable past that on
 * a phone, and a scrolling row keeps each label whole instead of truncating every one of them.
 * Pass it explicitly when the screen knows better — a tablet pane with six short labels has the
 * room to keep them all visible.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DsTabRow(
    items: List<DsTabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    layout: DsTabLayout = if (items.size > GRID_UP_TO) DsTabLayout.Inline else DsTabLayout.Grid,
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
    if (layout == DsTabLayout.Inline) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            modifier = modifier,
            containerColor = c.surface,
            contentColor = c.primary,
            edgePadding = DsTheme.spacing.smd,
            indicator = { positions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(positions[selectedIndex]),
                    height = 2.dp,
                    color = c.primary,
                )
            },
            divider = { HorizontalDivider(color = c.border) },
            tabs = tabs,
        )
    } else {
        Column(modifier) {
            FlowRow(Modifier.fillMaxWidth()) {
                items.forEachIndexed { index, item ->
                    DsFlowTab(item, index == selectedIndex) { onSelect(index) }
                }
            }
            HorizontalDivider(color = c.border)
        }
    }
}

/**
 * A tab that is as wide as its content, with its own underline.
 *
 * Material's TabRow divides the width equally and its indicator slides between fixed positions,
 * neither of which survives wrapping onto a second line — so the grid layout draws the tab and its
 * underline itself.
 */
@Composable
private fun DsFlowTab(
    item: DsTabItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val c = DsTheme.colors
    Column(
        Modifier
            // Intrinsic width, or the underline below (which fills what it is given) would stretch
            // the tab to the whole row and push every other tab onto its own line.
            .width(IntrinsicSize.Max)
            .selectable(selected = selected, role = Role.Tab, onClick = onClick)
            .padding(horizontal = DsTheme.spacing.smd),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            Modifier.padding(vertical = DsTheme.spacing.smd),
            horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (item.icon != null) {
                Icon(
                    item.icon,
                    contentDescription = null,
                    tint = if (selected) c.primary else c.textSecondary,
                    modifier = Modifier.size(16.dp),
                )
            }
            Text(
                item.label,
                style = if (selected) DsTheme.typography.labelStrong else DsTheme.typography.label,
                color = if (selected) c.primary else c.textSecondary,
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
        HorizontalDivider(
            thickness = 2.dp,
            color = if (selected) c.primary else Color.Transparent,
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
