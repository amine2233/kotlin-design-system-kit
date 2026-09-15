package io.github.amine2233.designsystem.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.amine2233.designsystem.atoms.DsVerticalDivider
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.DsWindowSize
import io.github.amine2233.designsystem.organisms.DsBottomNavBar
import io.github.amine2233.designsystem.organisms.DsNavItem
import io.github.amine2233.designsystem.organisms.DsNavigationRail

/**
 * App-level navigation chrome:
 *  - Expanded (tablet): sidebar rail at the start edge (left LTR / right RTL), top + bottom groups.
 *  - Compact (phone): bottom tab bar with [topItems] only; [bottomItems] belong in a menu/settings screen there.
 * [selectedIndex] indexes `topItems + bottomItems`.
 */
@Composable
fun DsNavigationScaffold(
    topItems: List<DsNavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    bottomItems: List<DsNavItem> = emptyList(),
    windowSize: DsWindowSize = DsTheme.windowSize,
    railHeader: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    if (windowSize.isExpanded) {
        Row(modifier.fillMaxSize()) {
            DsNavigationRail(topItems, selectedIndex, onSelect, bottomItems = bottomItems, header = railHeader)
            DsVerticalDivider()
            Box(Modifier.weight(1f).fillMaxSize()) { content() }
        }
    } else {
        Column(modifier.fillMaxSize()) {
            Box(Modifier.weight(1f)) { content() }
            DsBottomNavBar(topItems, selectedIndex.coerceIn(0, topItems.lastIndex), onSelect)
        }
    }
}
