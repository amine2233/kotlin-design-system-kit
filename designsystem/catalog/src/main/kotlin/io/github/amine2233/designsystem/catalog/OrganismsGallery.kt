package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsAmountDisplay
import io.github.amine2233.designsystem.molecules.DsListItem
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.organisms.DsBottomCartBar
import io.github.amine2233.designsystem.organisms.DsBottomNavBar
import io.github.amine2233.designsystem.organisms.DsCartLineItem
import io.github.amine2233.designsystem.organisms.DsCashTender
import io.github.amine2233.designsystem.organisms.DsChipRow
import io.github.amine2233.designsystem.organisms.DsChipRowLayout
import io.github.amine2233.designsystem.organisms.DsCircleMenu
import io.github.amine2233.designsystem.organisms.DsCircleMenuAction
import io.github.amine2233.designsystem.organisms.DsNavItem
import io.github.amine2233.designsystem.organisms.DsNfcPulse
import io.github.amine2233.designsystem.organisms.DsNumericKeypad
import io.github.amine2233.designsystem.organisms.DsPaymentMethodRow
import io.github.amine2233.designsystem.organisms.DsPaymentOutcome
import io.github.amine2233.designsystem.organisms.DsProcessingState
import io.github.amine2233.designsystem.organisms.DsProductGrid
import io.github.amine2233.designsystem.organisms.DsProductGridItem
import io.github.amine2233.designsystem.organisms.DsPullToRefresh
import io.github.amine2233.designsystem.organisms.DsReceipt
import io.github.amine2233.designsystem.organisms.DsReceiptItem
import io.github.amine2233.designsystem.organisms.DsResultMark
import io.github.amine2233.designsystem.organisms.DsSnackbar
import io.github.amine2233.designsystem.organisms.DsSnackbarTone
import io.github.amine2233.designsystem.organisms.DsSplitBill
import io.github.amine2233.designsystem.organisms.DsSplitMode
import io.github.amine2233.designsystem.organisms.DsSplitPart
import io.github.amine2233.designsystem.organisms.DsToast
import io.github.amine2233.designsystem.organisms.DsToastStyle
import io.github.amine2233.designsystem.organisms.DsTopBar
import io.github.amine2233.designsystem.organisms.DsTotalsBlock
import io.github.amine2233.designsystem.organisms.DsTotalsLine
import io.github.amine2233.designsystem.organisms.applyKey
import kotlinx.coroutines.delay

private const val REFRESH_DEMO_MS = 1500L
private const val CASH_TENDER_DUE = 118_800L

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
            DsChipRow(listOf("20%", "10%", "5,5%", "0%"), tva, { tva = it }, layout = DsChipRowLayout.Grid)
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
        CatalogSection("Cash tender") {
            var tendered by rememberSaveable { mutableLongStateOf(0L) }
            var selectedBill by rememberSaveable { mutableStateOf<Long?>(null) }
            DsCashTender(
                amountDue = CASH_TENDER_DUE,
                tendered = tendered,
                onKey = {
                    tendered = tendered.applyKey(it)
                    selectedBill = null
                },
                onQuickTender = {
                    tendered = it
                    selectedBill = it
                },
                unit = "DA",
                selectedQuickTender = selectedBill,
                amountDueLabel = "À encaisser",
                tenderedLabel = "Reçu du client",
                exactLabel = "Exact",
                changeLabel = "Monnaie à rendre",
                missingLabel = "Reste à payer",
                keypadRowModifier = Modifier.height(52.dp),
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
        CatalogSection("Toasts") {
            DsToast("Synchronisation terminée", Modifier.fillMaxWidth(), style = DsToastStyle.Info)
            DsToast(
                "La commande a été transmise à la cuisine.",
                Modifier.fillMaxWidth(),
                style = DsToastStyle.Success,
                title = "Commande envoyée",
            )
            DsToast("Papier bientôt épuisé", Modifier.fillMaxWidth(), style = DsToastStyle.Warning)
            DsToast("Terminal injoignable", Modifier.fillMaxWidth(), style = DsToastStyle.Error)
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
        CatalogSection("Pull to refresh") {
            var refreshing by rememberSaveable { mutableStateOf(false) }
            LaunchedEffect(refreshing) {
                if (refreshing) {
                    delay(REFRESH_DEMO_MS)
                    refreshing = false
                }
            }
            DsPullToRefresh(isRefreshing = refreshing, onRefresh = { refreshing = true }, modifier = Modifier.height(200.dp)) {
                RefreshRows()
            }
            DsPullToRefresh(isRefreshing = true, onRefresh = {}, modifier = Modifier.height(200.dp), animate = false) {
                RefreshRows()
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
        CatalogSection("Circle menu") {
            val actions =
                listOf(
                    DsCircleMenuAction(DsIcons.Edit, "Modifier", {}),
                    DsCircleMenuAction(DsIcons.Print, "Imprimer", {}),
                    DsCircleMenuAction(DsIcons.Delete, "Supprimer", {}),
                )
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.Bottom) {
                var orderMenuOpen by rememberSaveable { mutableStateOf(false) }
                DsCircleMenu(orderMenuOpen, { orderMenuOpen = it }, actions, "Actions de la commande")
                var ticketMenuOpen by rememberSaveable { mutableStateOf(true) }
                DsCircleMenu(ticketMenuOpen, { ticketMenuOpen = it }, actions, "Actions du ticket")
            }
        }
        CatalogSection("Product grid") {
            var query by rememberSaveable { mutableStateOf("") }
            var category by rememberSaveable { mutableIntStateOf(0) }
            val visible =
                galleryProducts.filter { (product, productCategory) ->
                    (category == 0 || productCategory == category) && product.name.contains(query.trim(), ignoreCase = true)
                }
            DsProductGrid(
                products = visible.map { it.first },
                onProductClick = {},
                emptyTitle = "Aucun résultat",
                modifier = Modifier.height(460.dp),
                categories = productCategories,
                selectedCategory = category,
                onCategorySelect = { category = it },
                query = query,
                onQueryChange = { query = it },
                emptyDescription = "Essayez une autre recherche ou catégorie.",
                emptyIcon = DsIcons.Search,
            )
            DsProductGrid(
                products = emptyList(),
                onProductClick = {},
                emptyTitle = "Aucun résultat",
                modifier = Modifier.height(280.dp),
                categories = productCategories,
                selectedCategory = 3,
                query = "Mocha",
                emptyDescription = "Essayez une autre recherche ou catégorie.",
                emptyIcon = DsIcons.Search,
            )
        }
        CatalogSection("Split bill") {
            var equalParts by remember { mutableStateOf(List(3) { DsSplitPart() }) }
            SplitBillExample(
                DsSplitMode.Equal,
                equalParts,
                onPartCountChange = { count -> equalParts = List(count) { equalParts.getOrElse(it) { DsSplitPart() } } },
                onCollect = { index -> equalParts = equalParts.mapIndexed { i, part -> if (i == index) part.copy(paid = true) else part } },
            )
            SplitBillExample(
                DsSplitMode.Custom,
                listOf(
                    DsSplitPart(paid = true, amountMinor = 50_000L, label = "Karim"),
                    DsSplitPart(amountMinor = 30_000L, label = "Sonia"),
                ),
            )
            SplitBillExample(
                DsSplitMode.Custom,
                listOf(
                    DsSplitPart(amountMinor = 70_000L, label = "Karim"),
                    DsSplitPart(amountMinor = 45_000L, label = "Sonia"),
                ),
            )
        }
        CatalogSection("Receipt") {
            DsReceipt(
                merchantName = "Café Central",
                merchantDetails = listOf("12 rue Didouche Mourad, Alger", "NIF 000123456789"),
                items =
                    listOf(
                        DsReceiptItem("2×", "Cappuccino", "400 DA", detail = "Lait d'avoine"),
                        DsReceiptItem("1×", "Formule Déjeuner", "441 DA", originalTotal = "490 DA"),
                        DsReceiptItem("1×", "Part de gâteau pistache et fleur d'oranger de saison", "292 DA"),
                    ),
                totals =
                    listOf(
                        DsTotalsLine("Sous-total HT", "1 025 DA"),
                        DsTotalsLine("Remise totale", "−49 DA", DsTotalsEmphasis.Discount),
                        DsTotalsLine("TVA 10%", "92 DA", DsTotalsEmphasis.Muted),
                        DsTotalsLine("TVA 5,5%", "4 DA", DsTotalsEmphasis.Muted),
                        DsTotalsLine("Pourboire", "+ 108 DA", DsTotalsEmphasis.Positive),
                    ),
                total = DsTotalsLine("Total TTC", "1 180 DA"),
                paymentIcon = DsIcons.Card,
                paymentLabel = "Carte bancaire •••• 4242",
                timestamp = "12 nov. 2026, 14:32",
                reference = "Ticket n° A-10423",
                onPrint = {},
                onEmail = {},
                onSms = {},
                onNoReceipt = {},
                printLabel = "Imprimer",
                emailLabel = "E-mail",
                smsLabel = "SMS",
                noReceiptLabel = "Pas de ticket",
            )
            DsDivider()
            DsReceipt(
                merchantName = "Café Central",
                items = listOf(DsReceiptItem("1×", "Espresso", "150 DA")),
                totals = listOf(DsTotalsLine("Sous-total HT", "136 DA"), DsTotalsLine("TVA 10%", "14 DA", DsTotalsEmphasis.Muted)),
                total = DsTotalsLine("Total TTC", "150 DA"),
                paymentIcon = DsIcons.Cash,
                paymentLabel = "Espèces",
                timestamp = "12 nov. 2026, 14:35",
                reference = "Ticket n° A-10424",
            )
        }
    }
}

private val productCategories = listOf("Tous", "Chauds", "Froids", "Snacks")

private val galleryProducts =
    listOf(
        DsProductGridItem(1, "Espresso", "150 DA") to 1,
        DsProductGridItem(2, "Cappuccino", "200 DA", inCartCount = 2) to 1,
        DsProductGridItem(3, "Thé menthe", "120 DA", outOfStock = true) to 1,
        DsProductGridItem(4, "Jus d'orange", "180 DA") to 2,
        DsProductGridItem(5, "Limonade", "160 DA", inCartCount = 1) to 2,
        DsProductGridItem(6, "Croissant", "90 DA") to 3,
    )

private fun splitAmount(minor: Long): String = "${minor / 100},${(minor % 100).toString().padStart(2, '0')} DA"

@Composable
private fun SplitBillExample(
    mode: DsSplitMode,
    parts: List<DsSplitPart>,
    onPartCountChange: (Int) -> Unit = {},
    onCollect: (Int) -> Unit = {},
) {
    DsSplitBill(
        totalMinor = 100_000L,
        mode = mode,
        onModeChange = {},
        parts = parts,
        onPartCountChange = onPartCountChange,
        maxParts = 8,
        onPartClick = {},
        onCollect = onCollect,
        formatAmount = ::splitAmount,
        equalModeLabel = "Parts égales",
        customModeLabel = "Montants libres",
        partCountLabel = "Nombre de parts",
        totalLabel = "Total",
        paidLabel = "Payé",
        remainingLabel = "Reste à payer",
        unallocatedLabel = "Non réparti",
        overAllocatedLabel = "Dépassement",
        collectLabel = "Encaisser",
    )
}

@Composable
private fun RefreshRows() {
    LazyColumn(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(DsSpacing.sm)) {
        items(6) { DsListItem("Commande #${it + 101}", supporting = "Table ${it + 1}") }
    }
}

@Preview(name = "organisms", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun OrganismsGalleryPreview() = DsTheme { OrganismsGallery() }
