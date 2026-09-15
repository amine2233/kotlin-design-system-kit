package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsAvatar
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.atoms.DsCircularProgress
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.atoms.DsLogo
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.DsWindowSize
import io.github.amine2233.designsystem.molecules.DsAmountDisplay
import io.github.amine2233.designsystem.molecules.DsCheckRow
import io.github.amine2233.designsystem.molecules.DsProductCard
import io.github.amine2233.designsystem.molecules.DsSearchBar
import io.github.amine2233.designsystem.molecules.DsSectionLabel
import io.github.amine2233.designsystem.molecules.DsStepIndicator
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.organisms.DsBottomCartBar
import io.github.amine2233.designsystem.organisms.DsCartLineItem
import io.github.amine2233.designsystem.organisms.DsChipRow
import io.github.amine2233.designsystem.organisms.DsNumericKeypad
import io.github.amine2233.designsystem.organisms.DsTopBar
import io.github.amine2233.designsystem.organisms.DsTotalsBlock
import io.github.amine2233.designsystem.organisms.DsTotalsLine
import io.github.amine2233.designsystem.organisms.applyKey
import io.github.amine2233.designsystem.templates.DsCenteredLayout
import io.github.amine2233.designsystem.templates.DsScreenScaffold
import io.github.amine2233.designsystem.templates.DsTwoPaneLayout

private val cartSteps = listOf("Panier", "Pourboire", "Paiement")

private data class Product(val name: String, val price: String, val inCart: Int = 0) {
    val lineTotal: String get() = "${price.substringBefore(" ").toInt() * inCart} DA"
}

private val products = listOf(
    Product("Espresso", "150 DA"), Product("Cappuccino", "200 DA", 2), Product("Latte", "220 DA"),
    Product("Americano", "170 DA"), Product("Croissant", "120 DA", 1), Product("Sandwich", "350 DA"),
    Product("Jus Orange", "180 DA"), Product("Eau Min.", "80 DA"), Product("Thé Citron", "120 DA"),
    Product("Pain au Choc.", "130 DA"), Product("Jus Pomme", "160 DA"), Product("Café au Lait", "180 DA"),
)

/** Screen 01 — Main Sales: adaptive (phone: grid + cart bar · tablet: grid + pinned cart pane). */
@Composable
fun MainSalesScreen(modifier: Modifier = Modifier, windowSize: DsWindowSize = DsTheme.windowSize) {
    var category by rememberSaveable { mutableIntStateOf(0) }
    var query by rememberSaveable { mutableStateOf("") }
    val expanded = windowSize.isExpanded
    DsScreenScaffold(
        modifier = modifier,
        topBar = {
            DsTopBar(
                title = if (expanded) "Café Central — Caisse 1" else "Café Central",
                titleContent = { DsSearchBar(query, { query = it }, Modifier.weight(1f)) },
                actions = { DsAvatar("AK") },
            )
        },
    ) {
        DsTwoPaneLayout(
            windowSize = windowSize,
            primary = {
                Column(Modifier.fillMaxSize()) {
                    DsChipRow(listOf("Tous", "Boissons Chaudes", "Boissons Froides", "Nourriture", "Pâtisserie", "Extras"), category, { category = it }, Modifier.background(DsTheme.colors.surface))
                    DsDivider()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(if (expanded) 4 else 2),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(products) { p -> DsProductCard(p.name, p.price, onClick = {}, inCartCount = p.inCart) }
                    }
                }
            },
            secondary = { CartPane() },
            compactSecondary = { DsBottomCartBar(itemCount = 3, total = "737 DA", onExpand = {}, onPay = {}) },
        )
    }
}

@Composable
private fun CartPane() {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().background(DsTheme.colors.surface).padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Panier (3)", style = DsTheme.typography.title, modifier = Modifier.weight(1f))
            DsButton("Attente", onClick = {}, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small)
            DsButton("Vider", onClick = {}, variant = DsButtonVariant.Danger, size = DsButtonSize.Small)
        }
        DsDivider()
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(products.filter { it.inCart > 0 }) { p ->
                DsCartLineItem(p.name, p.inCart, {}, lineTotal = p.lineTotal, unitPrice = p.price, tvaLabel = "TVA 10%")
            }
        }
        DsDivider()
        Column(Modifier.background(DsTheme.colors.surface).padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DsTotalsBlock(lines = listOf(DsTotalsLine("Sous-total", "670 DA"), DsTotalsLine("TVA (10%)", "67 DA")), total = DsTotalsLine("Total", "737 DA"))
            DsButton("Encaisser 737 DA", onClick = {}, modifier = Modifier.fillMaxWidth())
            DsButton("Mettre en attente", onClick = {}, variant = DsButtonVariant.Outlined, size = DsButtonSize.Medium, modifier = Modifier.fillMaxWidth())
        }
    }
}

/** Screen 02 — Cart (phone). */
@Composable
fun CartScreen(modifier: Modifier = Modifier) {
    DsScreenScaffold(
        modifier = modifier,
        topBar = {
            Column {
                DsTopBar("Panier", onBack = {}, badge = "5", actions = {
                    DsButton("Attente", onClick = {}, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small)
                    DsButton("Vider", onClick = {}, variant = DsButtonVariant.Danger, size = DsButtonSize.Small)
                })
                DsStepIndicator(cartSteps, 0, Modifier.background(DsTheme.colors.surface).padding(horizontal = 8.dp, vertical = 9.dp))
                DsDivider()
            }
        },
        bottomPanel = {
            DsTotalsBlock(
                lines = listOf(
                    DsTotalsLine("Sous-total HT", "1 025 DA"),
                    DsTotalsLine("TVA 10%", "98 DA"),
                    DsTotalsLine("TVA 5,5%", "6 DA"),
                    DsTotalsLine("Remise totale", "−49 DA", DsTotalsEmphasis.Discount),
                ),
                total = DsTotalsLine("Total TTC", "1 080 DA"),
            )
            Spacer(Modifier.height(10.dp))
            DsButton("% Remise globale", onClick = {}, variant = DsButtonVariant.Outlined, size = DsButtonSize.Medium, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            DsButton("Encaisser — 1 080 DA", onClick = {}, modifier = Modifier.fillMaxWidth())
        },
    ) {
        LazyColumn(contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item { DsCartLineItem("Cappuccino", 2, {}, lineTotal = "400 DA", unitPrice = "200 DA", tvaLabel = "TVA 10%", onRemove = {}) }
            item { DsCartLineItem("Espresso", 1, {}, lineTotal = "150 DA", unitPrice = "150 DA", tvaLabel = "TVA 10%", onRemove = {}) }
            item { DsCartLineItem("Croissant", 1, {}, lineTotal = "120 DA", unitPrice = "120 DA", tvaLabel = "TVA 5,5%", onRemove = {}) }
            item { DsCartLineItem("Formule Déjeuner", 1, {}, lineTotal = "441 DA", unitPrice = "490 DA", tvaLabel = "TVA 10%", note = "Sans sucre", originalTotal = "490 DA", discountLabel = "−10% ✓", onRemove = {}) }
            item { DsCartLineItem("Thé Citron", 1, {}, lineTotal = "120 DA", unitPrice = "120 DA", tvaLabel = "TVA 10%", onRemove = {}) }
        }
    }
}

/** Screen 02B — Tips (phone): quick chips + keypad + live totals. */
@Composable
fun TipsScreen(modifier: Modifier = Modifier) {
    var preset by rememberSaveable { mutableIntStateOf(1) }
    var amount by rememberSaveable { mutableStateOf("108,00") }
    DsScreenScaffold(
        modifier = modifier,
        topBar = {
            Column {
                DsTopBar("Pourboire", onBack = {}, actions = { DsButton("Ignorer", onClick = {}, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small) })
                DsStepIndicator(cartSteps, 1, Modifier.background(DsTheme.colors.surface).padding(horizontal = 8.dp, vertical = 9.dp))
                DsDivider()
            }
        },
        bottomPanel = {
            DsTotalsBlock(
                lines = listOf(DsTotalsLine("Total panier", "1 080 DA", DsTotalsEmphasis.Muted), DsTotalsLine("Pourboire (10%)", "+ 108 DA", DsTotalsEmphasis.Positive)),
                total = DsTotalsLine("Nouveau total", "1 188 DA"),
            )
            Spacer(Modifier.height(12.dp))
            DsButton("Continuer vers le paiement — 1 188 DA", onClick = {}, modifier = Modifier.fillMaxWidth())
        },
    ) {
        Column(Modifier.background(DsTheme.colors.surface).padding(horizontal = 14.dp, vertical = 10.dp)) {
            DsSectionLabel("Montant rapide")
            Spacer(Modifier.height(8.dp))
            DsChipRow(listOf("5%", "10%", "15%", "20%", "Aucun"), preset, { preset = it }, scrollable = false, modifier = Modifier.padding(0.dp))
        }
        DsDivider()
        Column(Modifier.padding(horizontal = 14.dp, vertical = 8.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DsAmountDisplay(amount, "DA", label = "10% de 1 080 DA", prefix = "+")
            DsNumericKeypad(onKey = { amount = amount.applyKey(it); preset = -1 }, modifier = Modifier.weight(1f), rowModifier = Modifier.weight(1f))
        }
    }
}

/** Onboarding — État A: same composable centers on tablet via [DsCenteredLayout]. */
@Composable
fun OnboardingScreen(modifier: Modifier = Modifier) {
    DsCenteredLayout(modifier.background(DsTheme.colors.surface)) {
        Column(Modifier.padding(horizontal = 32.dp, vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            DsLogo(size = 80.dp)
            Spacer(Modifier.height(18.dp))
            Text("Caisse Pro", style = DsTheme.typography.displayMedium)
            Spacer(Modifier.height(8.dp))
            Text("Encaissez vite, gérez mieux.", style = DsTheme.typography.bodyLarge, color = DsTheme.colors.textSecondary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(28.dp))
            DsCard(Modifier.fillMaxWidth(), containerColor = DsTheme.colors.surfaceSubtle, borderColor = DsTheme.colors.surfaceSubtle, shape = DsShapes.lg, contentPadding = 16.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DsCheckRow("Encaissement rapide et fiable")
                    DsCheckRow("TVA multi-taux, remises et pourboires")
                    DsCheckRow("CB, espèces et paiement mixte")
                }
            }
            Spacer(Modifier.height(32.dp))
            DsButton("Se connecter", onClick = {}, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            DsButton("Créer un compte", onClick = {}, variant = DsButtonVariant.Outlined, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Text("Conditions d'utilisation · Confidentialité", style = DsTheme.typography.caption, color = DsTheme.colors.textTertiary)
        }
    }
}

/** Onboarding — État C: post-auth loading. */
@Composable
fun AuthLoadingScreen(modifier: Modifier = Modifier) {
    DsCenteredLayout(modifier.background(DsTheme.colors.surface)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            DsLogo(size = 56.dp)
            Spacer(Modifier.height(12.dp))
            Text("Caisse Pro", style = DsTheme.typography.title)
            Spacer(Modifier.height(40.dp))
            DsCircularProgress(0.75f)
            Spacer(Modifier.height(18.dp))
            Text("Authentification en cours…", style = DsTheme.typography.bodyLarge)
            Text("Vérification de vos accès", style = DsTheme.typography.body, color = DsTheme.colors.textTertiary)
        }
    }
}
