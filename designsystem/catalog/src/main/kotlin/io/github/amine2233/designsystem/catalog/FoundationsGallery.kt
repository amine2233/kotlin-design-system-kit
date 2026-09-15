package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsIllustration
import io.github.amine2233.designsystem.atoms.DsImage
import io.github.amine2233.designsystem.atoms.DsImageSlot
import io.github.amine2233.designsystem.atoms.DsLogo
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsImages
import io.github.amine2233.designsystem.core.DsTheme

private val iconSet = listOf(
    "Back" to DsIcons.Back, "Close" to DsIcons.Close, "Search" to DsIcons.Search, "Settings" to DsIcons.Settings,
    "Add" to DsIcons.Add, "Remove" to DsIcons.Remove, "Delete" to DsIcons.Delete, "Check" to DsIcons.Check,
    "Cart" to DsIcons.Cart, "Card" to DsIcons.Card, "Cash" to DsIcons.Cash, "Contactless" to DsIcons.Contactless,
    "Split" to DsIcons.Split, "Discount" to DsIcons.Discount, "Tip" to DsIcons.Tip, "Receipt" to DsIcons.Receipt,
    "Catalogue" to DsIcons.Catalogue, "Hold" to DsIcons.Hold, "Print" to DsIcons.Print, "Offline" to DsIcons.Offline,
)

/** Icons + images hosted by the DS. */
@Composable
fun FoundationsGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CatalogSection("DsIcons") {
            iconSet.chunked(5).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    row.forEach { (name, icon) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            Icon(icon, contentDescription = name, tint = DsTheme.colors.textPrimary, modifier = Modifier.size(24.dp))
                            Text(name, style = DsTheme.typography.overline, color = DsTheme.colors.textTertiary)
                        }
                    }
                }
            }
        }
        CatalogSection("DsImages") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                DsLogo(size = 64.dp)
                DsLogo(size = 40.dp)
                DsImage(DsImages.ProductPlaceholder, contentDescription = null, modifier = Modifier.size(64.dp))
                DsImageSlot(painter = null, contentDescription = null, modifier = Modifier.size(64.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DsIllustration(DsImages.Illustrations.EmptyCart, size = 96.dp)
                DsIllustration(DsImages.Illustrations.Error, size = 96.dp)
                DsIllustration(DsImages.Illustrations.Offline, size = 96.dp)
            }
        }
    }
}
