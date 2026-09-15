package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsAvatar
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsLogo
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.DsWindowSize
import io.github.amine2233.designsystem.organisms.DsNavItem
import io.github.amine2233.designsystem.organisms.DsNfcWaiting
import io.github.amine2233.designsystem.organisms.DsPaymentOutcome
import io.github.amine2233.designsystem.organisms.DsPaymentResult
import io.github.amine2233.designsystem.organisms.DsTopBar
import io.github.amine2233.designsystem.templates.DsCenteredLayout
import io.github.amine2233.designsystem.templates.DsNavigationScaffold
import io.github.amine2233.designsystem.templates.DsScreenScaffold

private val navTop =
    listOf(
        DsNavItem("Vente", DsIcons.Register),
        DsNavItem("Commandes", DsIcons.Receipt, badge = "2"),
        DsNavItem("Catalogue", DsIcons.Catalogue),
        DsNavItem("Rapports", DsIcons.Store),
    )
private val navBottom = listOf(DsNavItem("Réglages", DsIcons.Settings), DsNavItem("Compte", DsIcons.Account))

private val navTopAr =
    listOf(
        DsNavItem("البيع", DsIcons.Register),
        DsNavItem("الطلبات", DsIcons.Receipt, badge = "2"),
        DsNavItem("الكتالوج", DsIcons.Catalogue),
        DsNavItem("التقارير", DsIcons.Store),
    )
private val navBottomAr = listOf(DsNavItem("الإعدادات", DsIcons.Settings), DsNavItem("الحساب", DsIcons.Account))

/** App shell: rail (tablet, start edge) or tab bar (phone) around the Main Sales screen. */
@Composable
fun AppShellScreen(
    modifier: Modifier = Modifier,
    windowSize: DsWindowSize = DsTheme.windowSize,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
) {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    val arabic = layoutDirection == LayoutDirection.Rtl
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        DsNavigationScaffold(
            topItems = if (arabic) navTopAr else navTop,
            bottomItems = if (arabic) navBottomAr else navBottom,
            selectedIndex = selected,
            onSelect = { selected = it },
            windowSize = windowSize,
            railHeader = { DsLogo(size = 40.dp) },
            modifier = modifier,
        ) {
            if (selected == 0) {
                MainSalesScreen(windowSize = windowSize)
            } else {
                DsScreenScaffold(topBar = {
                    DsTopBar((if (arabic) navTopAr + navBottomAr else navTop + navBottom)[selected].label, actions = { DsAvatar("AK") })
                }) {
                    DsCenteredLayout { Text("…", style = DsTheme.typography.headline) }
                }
            }
        }
    }
}

/** Payment — NFC waiting state. */
@Composable
fun NfcPaymentScreen(
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    onBack: () -> Unit = {},
) {
    DsScreenScaffold(
        modifier = modifier,
        topBar = { DsTopBar("Encaissement", onBack = onBack) },
        bottomPanel = {
            DsButton(
                "Annuler le paiement",
                onClick = {},
                variant = DsButtonVariant.Outlined,
                modifier = Modifier.fillMaxWidth(),
            )
        },
    ) {
        DsCenteredLayout {
            DsNfcWaiting(
                title = "Approchez le téléphone",
                description = "Le client valide avec Apple Pay ou Google Pay sur son appareil.",
                amount = "1 188 DA",
                animate = animate,
            )
        }
    }
}

/** Payment — result states. [progress] fixed for previews; null animates. */
@Composable
fun PaymentResultScreen(
    outcome: DsPaymentOutcome,
    modifier: Modifier = Modifier,
    progress: Float? = null,
) {
    DsScreenScaffold(modifier = modifier, topBar = { DsTopBar("Encaissement") }) {
        DsCenteredLayout {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if (outcome == DsPaymentOutcome.Success) {
                    DsPaymentResult(
                        outcome,
                        title = "Paiement accepté",
                        amount = "1 188 DA",
                        description = "Ticket n° 0421 · Carte bancaire",
                        primaryAction = "Nouvelle vente",
                        secondaryAction = "Imprimer le ticket",
                        progress = progress,
                    )
                } else {
                    DsPaymentResult(
                        outcome,
                        title = "Paiement refusé",
                        amount = "1 188 DA",
                        description = "Carte refusée par la banque. Aucun montant n'a été débité.",
                        primaryAction = "Réessayer",
                        secondaryAction = "Autre moyen de paiement",
                        progress = progress,
                    )
                }
            }
        }
    }
}

private const val PHONE = "spec:width=375dp,height=780dp,dpi=420"

@DsScreenPreview @Composable
private fun AppShellScreenPreview() = DsTheme { AppShellScreen() }

@Preview(name = "tablet · عربي", device = "spec:width=960dp,height=600dp,dpi=320")
@Composable
private fun AppShellScreenRtlPreview() = DsTheme { AppShellScreen(layoutDirection = LayoutDirection.Rtl) }

@Preview(device = PHONE)
@Composable
private fun NfcPaymentScreenPreview() = DsTheme { NfcPaymentScreen(animate = false) }

@Preview(device = PHONE)
@Composable
private fun PaymentResultSuccessPreview() = DsTheme { PaymentResultScreen(DsPaymentOutcome.Success, progress = 1f) }

@Preview(device = PHONE)
@Composable
private fun PaymentResultFailurePreview() = DsTheme { PaymentResultScreen(DsPaymentOutcome.Failure, progress = 1f) }
