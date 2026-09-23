package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsTag
import io.github.amine2233.designsystem.atoms.DsTagTone
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsMenuCard
import io.github.amine2233.designsystem.molecules.DsMenuCardVariant
import io.github.amine2233.designsystem.molecules.DsQuantityStepper

/** The menu card in both variants, with and without a footer, plus the raw slot form. */
@Composable
fun MenuCardsGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CatalogSection("Tile — the product grid") {
            Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                DsMenuCard(
                    "Cappuccino",
                    "200 DA",
                    modifier = Modifier.weight(1f),
                    description = "Double expresso, lait vapeur",
                    tag = "Nouveau",
                    onClick = {},
                    media = { Icon(DsIcons.Coffee, contentDescription = null, modifier = Modifier.size(32.dp)) },
                )
                DsMenuCard(
                    "Crème brûlée",
                    "350 DA",
                    modifier = Modifier.weight(1f),
                    description = "Vanille de Madagascar, sucre caramélisé",
                    selected = true,
                    onClick = {},
                    media = { Icon(DsIcons.Coffee, contentDescription = null, modifier = Modifier.size(32.dp)) },
                )
            }
        }
        CatalogSection("Tile with a footer") {
            var quantity by rememberSaveable { mutableIntStateOf(2) }
            Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                DsMenuCard(
                    "Thé vert",
                    "120 DA",
                    modifier = Modifier.weight(1f),
                    description = "Sencha, théière 50 cl",
                    onClick = {},
                    footer = { DsButton("Ajouter", onClick = {}, size = DsButtonSize.Small, modifier = Modifier.fillMaxWidth()) },
                )
                DsMenuCard(
                    "Espresso",
                    "150 DA",
                    modifier = Modifier.weight(1f),
                    description = "Ristretto, 25 ml",
                    selected = quantity > 0,
                    onClick = {},
                    footer = { DsQuantityStepper(quantity, { quantity = it }) },
                )
            }
        }
        CatalogSection("Row — order review, search results") {
            DsMenuCard(
                "Cappuccino",
                "200 DA",
                variant = DsMenuCardVariant.Row,
                description = "Double expresso, lait vapeur",
                onClick = {},
                media = { Icon(DsIcons.Coffee, contentDescription = null, modifier = Modifier.size(28.dp)) },
                footer = { DsText("×2", style = DsTheme.typography.labelStrong, color = DsTheme.colors.primary) },
            )
            DsMenuCard(
                "Croissant",
                "90 DA",
                variant = DsMenuCardVariant.Row,
                description = "Pur beurre, cuit sur place",
                tag = "Stock bas",
                tagTone = DsTagTone.Warning,
                onClick = {},
            )
        }
        CatalogSection("Slots — when the venue needs something else") {
            DsMenuCard(
                variant = DsMenuCardVariant.Row,
                onClick = {},
                media = { Icon(DsIcons.Coffee, contentDescription = null, modifier = Modifier.size(28.dp)) },
                header = {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        DsText("Menu du jour", style = DsTheme.typography.bodyStrong)
                        DsText("1 200 DA", style = DsTheme.typography.labelStrong, color = DsTheme.colors.primary)
                    }
                },
                body = {
                    Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs)) {
                        DsTag("Entrée", tone = DsTagTone.Neutral)
                        DsTag("Plat", tone = DsTagTone.Neutral)
                        DsTag("Dessert", tone = DsTagTone.Neutral)
                    }
                },
                footer = { DsText("12 min", style = DsTheme.typography.caption, color = DsTheme.colors.textTertiary) },
            )
        }
    }
}

@Preview(name = "menu_cards", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun MenuCardsGalleryPreview() = DsTheme { MenuCardsGallery() }
