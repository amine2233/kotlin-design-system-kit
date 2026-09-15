package io.github.amine2233.designsystem.sample

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import io.github.amine2233.designsystem.catalog.AppShellScreen
import io.github.amine2233.designsystem.catalog.AtomsGallery
import io.github.amine2233.designsystem.catalog.AuthLoadingScreen
import io.github.amine2233.designsystem.catalog.CartScreen
import io.github.amine2233.designsystem.catalog.DiscountScreen
import io.github.amine2233.designsystem.catalog.FoundationsGallery
import io.github.amine2233.designsystem.catalog.MainSalesScreen
import io.github.amine2233.designsystem.catalog.MoleculesGallery
import io.github.amine2233.designsystem.catalog.NfcPaymentScreen
import io.github.amine2233.designsystem.catalog.OnboardingScreen
import io.github.amine2233.designsystem.catalog.OrganismsGallery
import io.github.amine2233.designsystem.catalog.PaymentResultScreen
import io.github.amine2233.designsystem.catalog.PaymentScreen
import io.github.amine2233.designsystem.catalog.TipsScreen
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.organisms.DsPaymentOutcome
import io.github.amine2233.designsystem.molecules.DsListItem
import io.github.amine2233.designsystem.molecules.DsSectionLabel
import io.github.amine2233.designsystem.organisms.DsTopBar
import io.github.amine2233.designsystem.templates.DsScreenScaffold

private enum class Page(val title: String, val group: String) {
    Foundations("Icons & images", "Design system"),
    Atoms("Atoms", "Design system"),
    Molecules("Molecules", "Design system"),
    Organisms("Organisms", "Design system"),
    Shell("App shell — rail / tab bar", "Screens"),
    ShellArabic("App shell — عربي (RTL)", "Screens"),
    MainSales("01 · Main Sales (adaptive)", "Screens"),
    Cart("02 · Panier", "Screens"),
    Tips("02B · Pourboire", "Screens"),
    Discount("03 · Remise", "Screens"),
    Payment("04 · Encaissement", "Screens"),
    Nfc("04 · NFC", "Screens"),
    Success("04 · Paiement accepté", "Screens"),
    Failure("04 · Paiement refusé", "Screens"),
    Onboarding("Onboarding", "Screens"),
    AuthLoading("Authentification", "Screens"),
}

// ponytail: enum-backed state instead of Navigation Compose; switch when deep links are needed.
@Composable
fun SampleApp() {
    var page by rememberSaveable { mutableStateOf<Page?>(null) }
    val current = page
    if (current == null) {
        Home(onOpen = { page = it })
    } else {
        BackHandler { page = null }
        when (current) {
            Page.Foundations -> Gallery(current.title, onBack = { page = null }) { FoundationsGallery() }
            Page.Atoms -> Gallery(current.title, onBack = { page = null }) { AtomsGallery() }
            Page.Molecules -> Gallery(current.title, onBack = { page = null }) { MoleculesGallery() }
            Page.Organisms -> Gallery(current.title, onBack = { page = null }) { OrganismsGallery() }
            Page.Shell -> AppShellScreen()
            Page.ShellArabic -> AppShellScreen(layoutDirection = LayoutDirection.Rtl)
            Page.MainSales -> MainSalesScreen()
            Page.Cart -> CartScreen()
            Page.Tips -> TipsScreen()
            Page.Discount -> DiscountScreen()
            Page.Payment -> PaymentScreen()
            Page.Nfc -> NfcPaymentScreen()
            Page.Success -> PaymentResultScreen(DsPaymentOutcome.Success)
            Page.Failure -> PaymentResultScreen(DsPaymentOutcome.Failure)
            Page.Onboarding -> OnboardingScreen()
            Page.AuthLoading -> AuthLoadingScreen()
        }
    }
}

@Composable
private fun Home(onOpen: (Page) -> Unit) {
    DsScreenScaffold(topBar = { DsTopBar("DS Kit · ${DsTheme.windowSize}") }) {
        LazyColumn(contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Page.entries.groupBy { it.group }.forEach { (group, pages) ->
                item { DsSectionLabel(group, Modifier.fillMaxWidth()) }
                items(pages) { p ->
                    DsListItem(p.title, onClick = { onOpen(p) }, trailing = {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = DsTheme.colors.textTertiary)
                    })
                }
            }
        }
    }
}

@Composable
private fun Gallery(title: String, onBack: () -> Unit, content: @Composable () -> Unit) {
    DsScreenScaffold(topBar = { DsTopBar(title, onBack = onBack) }) {
        Column { content() }
    }
}
