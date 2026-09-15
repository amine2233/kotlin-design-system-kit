package io.github.amine2233.designsystem.organisms

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

@Immutable
data class DsNavItem(val label: String, val icon: ImageVector, val badge: String? = null)

@Composable
fun DsBottomNavBar(items: List<DsNavItem>, selectedIndex: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    val c = DsTheme.colors
    NavigationBar(modifier, containerColor = c.surface) {
        items.forEachIndexed { i, item ->
            NavigationBarItem(
                selected = i == selectedIndex,
                onClick = { onSelect(i) },
                icon = {
                    BadgedBox(badge = { if (item.badge != null) Badge(containerColor = c.primary, contentColor = c.onPrimary) { Text(item.badge, style = DsTheme.typography.overline) } }) {
                        Icon(item.icon, contentDescription = null)
                    }
                },
                label = { Text(item.label, style = DsTheme.typography.caption) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = c.primary,
                    selectedTextColor = c.primary,
                    indicatorColor = c.primaryContainer,
                    unselectedIconColor = c.textSecondary,
                    unselectedTextColor = c.textSecondary,
                ),
            )
        }
    }
}

@DsComponentPreview
@Composable
private fun DsBottomNavBarPreview() = DsPreview {
    DsBottomNavBar(listOf(DsNavItem("Vente", DsIcons.Register), DsNavItem("Commandes", DsIcons.Receipt, badge = "2"), DsNavItem("Catalogue", DsIcons.Catalogue), DsNavItem("Réglages", DsIcons.Settings)), 0, {})
}
