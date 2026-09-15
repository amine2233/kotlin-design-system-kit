package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsAvatar
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsAmountDisplay
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.organisms.DsBottomCartBar
import io.github.amine2233.designsystem.organisms.DsBottomNavBar
import io.github.amine2233.designsystem.organisms.DsCartLineItem
import io.github.amine2233.designsystem.organisms.DsChipRow
import io.github.amine2233.designsystem.organisms.DsNavItem
import io.github.amine2233.designsystem.organisms.DsNfcPulse
import io.github.amine2233.designsystem.organisms.DsNumericKeypad
import io.github.amine2233.designsystem.organisms.DsPaymentMethodRow
import io.github.amine2233.designsystem.organisms.DsPaymentOutcome
import io.github.amine2233.designsystem.organisms.DsProcessingState
import io.github.amine2233.designsystem.organisms.DsResultMark
import io.github.amine2233.designsystem.organisms.DsSnackbar
import io.github.amine2233.designsystem.organisms.DsSnackbarTone
import io.github.amine2233.designsystem.organisms.DsTopBar
import io.github.amine2233.designsystem.organisms.DsTotalsBlock
import io.github.amine2233.designsystem.organisms.DsTotalsLine
import io.github.amine2233.designsystem.organisms.applyKey

@Composable
fun OrganismsGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CatalogSection("Top bars") {
            DsTopBar("Panier", onBack = {}, badge = "5", actions = {
                DsButton("Attente", onClick = {}, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small)
                DsButton("Vider", onClick = {}, variant = DsButtonVariant.Danger, size = DsButtonSize.Small)
            })
            DsTopBar("Café Central", actions = { DsAvatar("AK") })
        }
        CatalogSection("Chip rows") {
            var cat by rememberSaveable { mutableIntStateOf(0) }
            DsChipRow(listOf("Tous", "Chauds", "Froids", "Snacks", "Pâtisserie", "Extras"), cat, { cat = it })
            var tva by rememberSaveable { mutableIntStateOf(1) }
            DsChipRow(listOf("20%", "10%", "5,5%", "0%"), tva, { tva = it }, scrollable = false)
        }
        CatalogSection("Cart line items") {
            var q1 by rememberSaveable { mutableIntStateOf(2) }
            DsCartLineItem("Cappuccino", q1, { q1 = it }, lineTotal = "400 DA", unitPrice = "200 DA", tvaLabel = "TVA 10%", onRemove = {})
            DsCartLineItem(
                "Formule Déjeuner",
                1,
                {},
                lineTotal = "441 DA",
                unitPrice = "490 DA",
                tvaLabel = "TVA 10%",
                note = "Sans sucre",
                originalTotal = "490 DA",
                discountLabel = "−10% ✓",
                onRemove = {},
            )
        }
        CatalogSection("Totals block") {
            DsTotalsBlock(
                lines =
                    listOf(
                        DsTotalsLine("Sous-total HT", "1 025 DA"),
                        DsTotalsLine("TVA 10%", "98 DA"),
                        DsTotalsLine("TVA 5,5%", "6 DA"),
                        DsTotalsLine("Remise totale", "−49 DA", DsTotalsEmphasis.Discount),
                    ),
                total = DsTotalsLine("Total TTC", "1 080 DA"),
            )
        }
        CatalogSection("Numeric keypad") {
            var amount by rememberSaveable { mutableStateOf("0,00") }
            DsAmountDisplay(amount, "DA")
            DsNumericKeypad(
                onKey = { amount = amount.applyKey(it) },
                rowModifier = Modifier.height(52.dp),
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        CatalogSection("Payment methods") {
            var method by rememberSaveable { mutableIntStateOf(0) }
            DsPaymentMethodRow(DsIcons.Card, "Carte bancaire", "Terminal connecté", selected = method == 0, onClick = { method = 0 })
            DsPaymentMethodRow(DsIcons.Cash, "Espèces", "Calcul de la monnaie", selected = method == 1, onClick = { method = 1 })
        }
        CatalogSection("Snackbars (errors & confirmations)") {
            DsSnackbar("Échec du paiement — terminal injoignable", tone = DsSnackbarTone.Error, actionLabel = "Réessayer")
            DsSnackbar("Commande mise en attente", tone = DsSnackbarTone.Success)
            DsSnackbar("Article supprimé", actionLabel = "Annuler", onDismiss = {})
        }
        CatalogSection("Processing state") {
            DsProcessingState(
                DsIcons.Card,
                "Présentez la carte",
                description = "Le client tape ou insère sa carte sur le terminal.",
                progress = 0.6f,
                amount = "800 DA",
                amountLabel = "Montant sur le terminal",
            )
        }
        CatalogSection("Payment animations") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                DsResultMark(DsPaymentOutcome.Success, size = 80.dp, progress = 1f)
                DsResultMark(DsPaymentOutcome.Failure, size = 80.dp, progress = 1f)
                DsResultMark(DsPaymentOutcome.Success, size = 80.dp, progress = 0.6f)
                DsNfcPulse(size = 120.dp, animate = false)
            }
        }
        CatalogSection("Bottom navigation") {
            var nav by rememberSaveable { mutableIntStateOf(0) }
            DsBottomNavBar(
                listOf(
                    DsNavItem("Vente", DsIcons.Register),
                    DsNavItem("Commandes", DsIcons.Receipt, badge = "2"),
                    DsNavItem("Catalogue", DsIcons.Catalogue),
                    DsNavItem("Réglages", DsIcons.Settings),
                ),
                nav,
                { nav = it },
            )
        }
        CatalogSection("Bottom cart bar") {
            DsBottomCartBar(itemCount = 3, total = "737 DA", onExpand = {}, onPay = {})
        }
    }
}

@Preview(name = "organisms", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun OrganismsGalleryPreview() = DsTheme { OrganismsGallery() }
