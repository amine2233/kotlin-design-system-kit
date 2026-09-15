package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsAmountDisplay
import io.github.amine2233.designsystem.molecules.DsDropdown
import io.github.amine2233.designsystem.molecules.DsSectionLabel
import io.github.amine2233.designsystem.molecules.DsSegmentedControl
import io.github.amine2233.designsystem.molecules.DsStepIndicator
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.molecules.DsTotalsRow
import io.github.amine2233.designsystem.organisms.DsChipRow
import io.github.amine2233.designsystem.organisms.DsNumericKeypad
import io.github.amine2233.designsystem.organisms.DsPaymentMethodRow
import io.github.amine2233.designsystem.organisms.DsTopBar
import io.github.amine2233.designsystem.organisms.DsTotalsBlock
import io.github.amine2233.designsystem.organisms.DsTotalsLine
import io.github.amine2233.designsystem.organisms.applyKey
import io.github.amine2233.designsystem.templates.DsScreenScaffold

private val cartSteps = listOf("Panier", "Pourboire", "Paiement")

/** Screen 03 — Discount, État A (whole cart, −10%). */
@Composable
fun DiscountScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    var scope by rememberSaveable { mutableIntStateOf(0) }
    var type by rememberSaveable { mutableIntStateOf(0) }
    var quick by rememberSaveable { mutableIntStateOf(1) }
    var reason by rememberSaveable { mutableIntStateOf(0) }
    var value by rememberSaveable { mutableStateOf("10,00") }
    val c = DsTheme.colors
    DsScreenScaffold(
        modifier = modifier,
        topBar = {
            Column {
                DsTopBar("Remise", onBack = onBack, actions = {
                    DsButton("Annuler", onClick = {}, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small)
                })
                DsStepIndicator(cartSteps, 0, Modifier.background(c.surface).padding(horizontal = 8.dp, vertical = 9.dp))
                DsDivider()
            }
        },
        bottomPanel = {
            DsTotalsRow("Total avant remise", "1 080 DA", emphasis = DsTotalsEmphasis.Muted)
            DsTotalsRow("Remise − 10%", "− 108 DA", emphasis = DsTotalsEmphasis.Discount)
            DsTotalsRow("TVA 10% recalculée", "88 DA", emphasis = DsTotalsEmphasis.Muted)
            DsTotalsRow("TVA 5,5% recalculée", "5 DA", emphasis = DsTotalsEmphasis.Muted)
            Spacer(Modifier.height(8.dp))
            DsDivider()
            Spacer(Modifier.height(8.dp))
            DsTotalsRow("Nouveau total TTC", "972 DA", emphasis = DsTotalsEmphasis.Total, valueColor = c.primary)
            Spacer(Modifier.height(10.dp))
            DsButton("Appliquer la remise — 972 DA", onClick = {}, modifier = Modifier.fillMaxWidth())
            DsButton("Supprimer la remise existante", onClick = {
            }, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small, modifier = Modifier.fillMaxWidth())
        },
    ) {
        Column(
            Modifier.background(c.surface).padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DsSectionLabel("Appliquer à")
            DsSegmentedControl(listOf("Panier complet", "Article spécifique"), scope, { scope = it }, Modifier.fillMaxWidth())
            DsSegmentedControl(listOf("% Pourcentage", "DA Montant fixe"), type, {
                type = it
            }, Modifier.fillMaxWidth(), shape = DsTheme.shapes.sm, height = 34.dp)
            DsChipRow(listOf("5%", "10%", "20%", "Perso."), quick, { quick = it }, scrollable = false, modifier = Modifier.padding(0.dp))
            DsDropdown(listOf("Fidélité", "Geste commercial", "Erreur de saisie"), reason, { reason = it }, label = "Motif")
        }
        DsDivider()
        Column(Modifier.padding(horizontal = 14.dp, vertical = 8.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DsAmountDisplay(value, if (type == 0) "%" else "DA", label = "Valeur saisie")
            DsNumericKeypad(onKey = {
                value = value.applyKey(it)
                quick = -1
            }, modifier = Modifier.weight(1f), rowModifier = Modifier.weight(1f))
        }
    }
}

/** Screen 04 — Payment, cash state (method list · quick cash · keypad · change). */
@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    var method by rememberSaveable { mutableIntStateOf(1) }
    var quick by rememberSaveable { mutableIntStateOf(2) }
    var received by rememberSaveable { mutableStateOf("1 500,00") }
    val c = DsTheme.colors
    DsScreenScaffold(
        modifier = modifier,
        topBar = {
            Column {
                DsTopBar("Encaissement", onBack = onBack)
                DsStepIndicator(cartSteps, 2, Modifier.background(c.surface).padding(horizontal = 8.dp, vertical = 9.dp))
                DsDivider()
            }
        },
        bottomPanel = {
            DsTotalsBlock(
                lines = listOf(DsTotalsLine("À encaisser", "1 188 DA", DsTotalsEmphasis.Muted), DsTotalsLine("Reçu du client", "1 500 DA")),
                total = DsTotalsLine("Monnaie à rendre", "312 DA"),
            )
            Spacer(Modifier.height(12.dp))
            DsButton("Valider l'encaissement", onClick = {}, modifier = Modifier.fillMaxWidth(), leadingIcon = DsIcons.Check)
        },
    ) {
        Column(Modifier.background(c.surface).padding(horizontal = 14.dp, vertical = 10.dp)) {
            DsSectionLabel("Montant à encaisser")
            Text("1 188 DA", style = DsTheme.typography.displayMedium, color = c.textPrimary)
            Text("1 080 DA panier · 108 DA pourboire", style = DsTheme.typography.caption, color = c.textSecondary)
        }
        DsDivider()
        Column(Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            DsSectionLabel("Moyen de paiement")
            DsPaymentMethodRow(DsIcons.Card, "Carte bancaire", "Terminal connecté", selected = method == 0, onClick = { method = 0 })
            DsPaymentMethodRow(DsIcons.Cash, "Espèces", "Calcul de la monnaie", selected = method == 1, onClick = { method = 1 })
            DsPaymentMethodRow(DsIcons.Split, "Paiement mixte", "Carte + espèces", selected = method == 2, onClick = { method = 2 })
        }
        Column(Modifier.padding(horizontal = 14.dp, vertical = 4.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DsSectionLabel("Montant remis")
            DsChipRow(
                listOf("Exact", "1 200", "1 500", "2 000"),
                quick,
                { quick = it },
                scrollable = false,
                modifier = Modifier.padding(0.dp),
            )
            DsAmountDisplay(received, "DA", label = "Reçu du client")
            DsNumericKeypad(onKey = {
                received = received.applyKey(it)
                quick = -1
            }, modifier = Modifier.weight(1f), rowModifier = Modifier.weight(1f))
        }
    }
}

private const val PHONE = "spec:width=375dp,height=780dp,dpi=420"

@Preview(device = PHONE)
@Composable
private fun DiscountScreenPreview() = DsTheme { DiscountScreen() }

@Preview(device = PHONE)
@Composable
private fun PaymentScreenPreview() = DsTheme { PaymentScreen() }
