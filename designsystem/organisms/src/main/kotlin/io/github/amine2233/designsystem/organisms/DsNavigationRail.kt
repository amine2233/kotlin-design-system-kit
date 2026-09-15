package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Tablet sidebar. Placed at the *start* edge by [io.github.amine2233.designsystem.templates.DsNavigationScaffold],
 * so it sits left in LTR (English) and right in RTL (Arabic) with no extra code.
 * [topItems] are the primary destinations, [bottomItems] the utilities (settings, account, logout).
 */
@Composable
fun DsNavigationRail(
    topItems: List<DsNavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    bottomItems: List<DsNavItem> = emptyList(),
    header: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(80.dp)
            .background(c.surface)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (header != null) {
            header()
            Spacer(Modifier.padding(bottom = 8.dp))
        }
        topItems.forEachIndexed { i, item -> RailItem(item, selected = i == selectedIndex) { onSelect(i) } }
        Spacer(Modifier.weight(1f))
        bottomItems.forEachIndexed { i, item ->
            val index = topItems.size + i
            RailItem(item, selected = index == selectedIndex) { onSelect(index) }
        }
    }
}

@Composable
private fun RailItem(item: DsNavItem, selected: Boolean, onClick: () -> Unit) {
    val c = DsTheme.colors
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = { BadgedBox(badge = { if (item.badge != null) Badge(containerColor = c.primary, contentColor = c.onPrimary) { Text(item.badge, style = DsTheme.typography.overline) } }) { Icon(item.icon, contentDescription = null) } },
        label = { Text(item.label, style = DsTheme.typography.overline, maxLines = 1) },
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = c.primary,
            selectedTextColor = c.primary,
            indicatorColor = c.primaryContainer,
            unselectedIconColor = c.textSecondary,
            unselectedTextColor = c.textSecondary,
        ),
    )
}

@Preview(name = "rail", heightDp = 480)
@Composable
private fun DsNavigationRailPreview() = DsTheme {
    DsNavigationRail(listOf(DsNavItem("Vente", DsIcons.Register), DsNavItem("Commandes", DsIcons.Receipt, badge = "2"), DsNavItem("Catalogue", DsIcons.Catalogue)), 0, {}, bottomItems = listOf(DsNavItem("Réglages", DsIcons.Settings)))
}
