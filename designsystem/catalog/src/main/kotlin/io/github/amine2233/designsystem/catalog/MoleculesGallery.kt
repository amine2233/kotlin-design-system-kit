package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.atoms.DsRadioIndicator
import io.github.amine2233.designsystem.atoms.DsTag
import io.github.amine2233.designsystem.atoms.DsTagTone
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsAllocation
import io.github.amine2233.designsystem.molecules.DsAllocationBar
import io.github.amine2233.designsystem.molecules.DsAmountDisplay
import io.github.amine2233.designsystem.molecules.DsBanner
import io.github.amine2233.designsystem.molecules.DsBannerTone
import io.github.amine2233.designsystem.molecules.DsCheckRow
import io.github.amine2233.designsystem.molecules.DsDashedActionRow
import io.github.amine2233.designsystem.molecules.DsDropdown
import io.github.amine2233.designsystem.molecules.DsEmptyState
import io.github.amine2233.designsystem.molecules.DsExpandableSection
import io.github.amine2233.designsystem.molecules.DsFilterGroup
import io.github.amine2233.designsystem.molecules.DsFilterLayout
import io.github.amine2233.designsystem.molecules.DsFilterSelection
import io.github.amine2233.designsystem.molecules.DsKeyTone
import io.github.amine2233.designsystem.molecules.DsKeypadKey
import io.github.amine2233.designsystem.molecules.DsListItem
import io.github.amine2233.designsystem.molecules.DsProductCard
import io.github.amine2233.designsystem.molecules.DsQuantityStepper
import io.github.amine2233.designsystem.molecules.DsSearchBar
import io.github.amine2233.designsystem.molecules.DsSectionLabel
import io.github.amine2233.designsystem.molecules.DsSegmentedControl
import io.github.amine2233.designsystem.molecules.DsStepIndicator
import io.github.amine2233.designsystem.molecules.DsTabItem
import io.github.amine2233.designsystem.molecules.DsTabRow
import io.github.amine2233.designsystem.molecules.DsTagEntry
import io.github.amine2233.designsystem.molecules.DsTagGroup
import io.github.amine2233.designsystem.molecules.DsTagLayout
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.molecules.DsTotalsRow

@Composable
fun MoleculesGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CatalogSection("Tabs") {
            var orders by rememberSaveable { mutableIntStateOf(0) }
            DsSectionLabel("Compteurs")
            DsTabRow(
                listOf(
                    DsTabItem("En cours", badge = "3"),
                    DsTabItem("Prêtes", badge = "12"),
                    DsTabItem("Servies"),
                ),
                selectedIndex = orders,
                onSelect = { orders = it },
            )
            DsSectionLabel("Icônes, défilement au-delà de quatre onglets")
            var section by rememberSaveable { mutableIntStateOf(1) }
            DsTabRow(
                listOf(
                    DsTabItem("Vente", icon = DsIcons.Register),
                    DsTabItem("Commandes", icon = DsIcons.Receipt, badge = "2"),
                    DsTabItem("Catalogue", icon = DsIcons.Catalogue),
                    DsTabItem("Clients", icon = DsIcons.Account),
                    DsTabItem("Réglages", icon = DsIcons.Settings),
                ),
                selectedIndex = section,
                onSelect = { section = it },
            )
        }
        CatalogSection("Tag groups") {
            val tags =
                listOf(
                    DsTagEntry("Sans gluten", DsTagTone.Success),
                    DsTagEntry("Épicé", DsTagTone.Warning),
                    DsTagEntry("Nouveau", DsTagTone.Primary),
                    DsTagEntry("Fruits à coque", DsTagTone.Error),
                    DsTagEntry("Végétarien"),
                )
            DsSectionLabel("Wrap — tout est lisible")
            DsTagGroup(tags)
            DsSectionLabel("Scroll — une seule ligne")
            DsTagGroup(tags, layout = DsTagLayout.Scroll)
            DsSectionLabel("Débordement compté")
            DsTagGroup(tags, max = 2)
        }
        CatalogSection("Filters") {
            var category by rememberSaveable { mutableIntStateOf(0) }
            DsSectionLabel("Inline, un seul choix — chip « Tous » pour effacer")
            DsFilterGroup(
                listOf("Chauds", "Froids", "Snacks", "Desserts"),
                selected = setOf(category),
                onSelectedChange = { category = it.firstOrNull() ?: 0 },
                selection = DsFilterSelection.Single,
                allLabel = "Tous",
            )
            DsSectionLabel("Grid, plusieurs choix — rien ne défile")
            var methods by rememberSaveable { mutableStateOf(setOf(0, 2)) }
            DsFilterGroup(
                listOf("Carte", "Espèces", "Mixte", "En attente", "Remboursé"),
                selected = methods,
                onSelectedChange = { methods = it },
                layout = DsFilterLayout.Grid,
            )
        }
        CatalogSection("Step indicator") {
            DsStepIndicator(listOf("Panier", "Pourboire", "Paiement"), currentStep = 0)
            DsStepIndicator(listOf("Panier", "Pourboire", "Paiement"), currentStep = 1)
            DsStepIndicator(listOf("Panier", "Pourboire", "Paiement"), currentStep = 2)
        }
        CatalogSection("Stepper & segmented control") {
            var qty by rememberSaveable { mutableIntStateOf(2) }
            DsQuantityStepper(qty, { qty = it })
            var scope by rememberSaveable { mutableIntStateOf(0) }
            DsSectionLabel("Appliquer à")
            DsSegmentedControl(listOf("Panier complet", "Article spécifique"), scope, { scope = it }, Modifier.fillMaxWidth())
            var type by rememberSaveable { mutableIntStateOf(0) }
            DsSegmentedControl(listOf("% Pourcentage", "DA Montant fixe"), type, {
                type = it
            }, Modifier.fillMaxWidth(), shape = DsTheme.shapes.sm, height = 34.dp)
        }
        CatalogSection("Amount display") {
            DsAmountDisplay("1 500,00", "DA")
            DsAmountDisplay("10,00", "%", label = "Valeur saisie")
            DsAmountDisplay("108,00", "DA", label = "10% de 1 080 DA", prefix = "+")
        }
        CatalogSection("Totals rows") {
            DsTotalsRow("Sous-total HT", "1 025 DA")
            DsTotalsRow("TVA 5,5% recalculée", "5 DA", emphasis = DsTotalsEmphasis.Muted)
            DsTotalsRow("Remise totale", "−49 DA", emphasis = DsTotalsEmphasis.Discount)
            DsTotalsRow("Pourboire (10%)", "+ 108 DA", emphasis = DsTotalsEmphasis.Positive)
            DsTotalsRow("Total TTC", "1 080 DA", emphasis = DsTotalsEmphasis.Total)
        }
        CatalogSection("Search & list items") {
            var query by rememberSaveable { mutableStateOf("") }
            DsSearchBar(query, { query = it })
            DsSearchBar("cappu", {})
            DsSearchBar(
                "crème",
                {},
                height = DsTheme.spacing.minTouchTarget,
                trailing = { DsIconButton(DsIcons.Settings, contentDescription = "Filtres", onClick = {}) },
            )
            DsSearchBar("", {}, enabled = false, placeholder = "Recherche indisponible")
            DsListItem("Cappuccino", supporting = "×2 · 200 DA/u. · TVA 10%", leading = {
                DsRadioIndicator(false)
            }, trailing = { Text("400 DA", style = DsTheme.typography.labelStrong, color = DsTheme.colors.textSecondary) })
            DsListItem("Espresso", supporting = "×1 · 150 DA/u. · TVA 10%", selected = true, leading = {
                DsRadioIndicator(true)
            }, trailing = { Text("150 DA", style = DsTheme.typography.labelStrong, color = DsTheme.colors.primary) })
        }
        CatalogSection("Dropdown, tabs, actions") {
            var reason by rememberSaveable { mutableIntStateOf(0) }
            DsDropdown(listOf("Fidélité", "Geste commercial", "Erreur de saisie"), reason, { reason = it }, label = "Motif")
            var tab by rememberSaveable { mutableIntStateOf(0) }
            DsTabRow(listOf("Boissons", "Nourriture", "Extras"), tab, { tab = it })
            DsDashedActionRow("Montant libre / pavé numérique", onClick = {})
        }
        CatalogSection("Feedback") {
            DsBanner(
                "Le terminal est déconnecté.",
                tone = DsBannerTone.Error,
                title = "Paiement carte indisponible",
                actionLabel = "Réessayer",
                onAction = {},
            )
            DsBanner("Remise appliquée sur 1 article.", tone = DsBannerTone.Success, onDismiss = {})
            DsBanner("Le pourboire est calculé sur le total TTC.", tone = DsBannerTone.Info)
            DsBanner("Stock faible : Croissant (3)", tone = DsBannerTone.Warning)
            DsEmptyState(
                "Panier vide",
                description = "Touchez un produit pour l'ajouter.",
                icon = DsIcons.Cart,
                actionLabel = "Voir le catalogue",
                onAction = {},
            )
            DsAllocationBar(listOf(DsAllocation("Carte 800 DA", 0.67f), DsAllocation("Espèces 388 DA", 0.33f)))
        }
        CatalogSection("Expandable section") {
            var open by rememberSaveable { mutableStateOf(true) }
            DsExpandableSection("Produits", expanded = open, onToggle = {
                open = !open
            }, summary = "12 produits", trailing = { DsTag("12", tone = DsTagTone.Primary) }) {
                Text("Contenu de la section", style = DsTheme.typography.body)
            }
            DsExpandableSection("Catégories", expanded = false, onToggle = {}, summary = "Chauds · Froids · Snacks") {}
        }
        CatalogSection("Product cards & keys") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsProductCard("Espresso", "150 DA", onClick = {}, modifier = Modifier.weight(1f))
                DsProductCard("Cappuccino", "200 DA", onClick = {}, inCartCount = 2, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsKeypadKey("7", onClick = {}, Modifier.weight(1f).height(48.dp))
                DsKeypadKey(",", onClick = {}, Modifier.weight(1f).height(48.dp), tone = DsKeyTone.Secondary)
                DsKeypadKey("⌫", onClick = {}, Modifier.weight(1f).height(48.dp), tone = DsKeyTone.Destructive)
            }
            DsCheckRow("Encaissement rapide et fiable")
            DsCheckRow("TVA multi-taux, remises et pourboires", filled = false)
        }
    }
}

@Preview(name = "molecules", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun MoleculesGalleryPreview() = DsTheme { MoleculesGallery() }
