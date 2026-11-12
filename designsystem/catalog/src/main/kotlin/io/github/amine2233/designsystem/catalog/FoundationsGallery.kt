package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsIllustration
import io.github.amine2233.designsystem.atoms.DsImage
import io.github.amine2233.designsystem.atoms.DsImageSlot
import io.github.amine2233.designsystem.atoms.DsLogo
import io.github.amine2233.designsystem.core.DsColorScheme
import io.github.amine2233.designsystem.core.DsColorTheme
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsImages
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

private val iconSet =
    listOf(
        "Back" to DsIcons.Back,
        "Close" to DsIcons.Close,
        "Search" to DsIcons.Search,
        "Settings" to DsIcons.Settings,
        "Add" to DsIcons.Add,
        "Remove" to DsIcons.Remove,
        "Delete" to DsIcons.Delete,
        "Check" to DsIcons.Check,
        "Cart" to DsIcons.Cart,
        "Card" to DsIcons.Card,
        "Cash" to DsIcons.Cash,
        "Contactless" to DsIcons.Contactless,
        "Split" to DsIcons.Split,
        "Discount" to DsIcons.Discount,
        "Tip" to DsIcons.Tip,
        "Receipt" to DsIcons.Receipt,
        "Catalogue" to DsIcons.Catalogue,
        "Hold" to DsIcons.Hold,
        "Print" to DsIcons.Print,
        "Offline" to DsIcons.Offline,
    )

private fun DsColorScheme.primaryFamily() =
    listOf(
        "primary" to primary,
        "primaryPressed" to primaryPressed,
        "onPrimary" to onPrimary,
        "primaryContainer" to primaryContainer,
        "primaryContainerBorder" to primaryContainerBorder,
        "primaryTint" to primaryTint,
    )

/** Icons, images and the selectable color themes hosted by the DS. */
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
        CatalogSection("DsColorTheme") {
            DsColorTheme.entries.forEach { theme ->
                Text(theme.name, style = DsTheme.typography.title, color = DsTheme.colors.textPrimary)
                ThemeSwatches(theme.colors(dark = false), "Light")
                ThemeSwatches(theme.colors(dark = true), "Dark")
            }
        }
    }
}

// Painted on the scheme's own background so the swatches read the same whatever theme wraps the gallery.
@Composable
private fun ThemeSwatches(
    scheme: DsColorScheme,
    mode: String,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(scheme.background, DsShapes.md)
            .border(1.dp, scheme.border, DsShapes.md)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(mode, style = DsTheme.typography.label, color = scheme.textSecondary)
        scheme.primaryFamily().chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (name, color) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .background(color, DsShapes.sm)
                                .border(1.dp, scheme.border, DsShapes.sm),
                        )
                        Text(name, style = DsTheme.typography.overline, color = scheme.textSecondary, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Preview(name = "foundations", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun FoundationsGalleryPreview() = DsTheme { FoundationsGallery() }
