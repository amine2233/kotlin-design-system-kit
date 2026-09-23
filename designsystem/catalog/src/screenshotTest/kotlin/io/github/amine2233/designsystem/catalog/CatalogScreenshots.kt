package io.github.amine2233.designsystem.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.android.tools.screenshot.PreviewTest
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.DsWindowSize
import io.github.amine2233.designsystem.organisms.DsPaymentOutcome

/*
 * Reference images live in src/debug/screenshotTest/reference/.
 *   ./gradlew :designsystem:catalog:updateDebugScreenshotTest    # record
 *   ./gradlew :designsystem:catalog:validateDebugScreenshotTest  # verify (fails on regression)
 */

private const val PHONE = "spec:width=375dp,height=780dp,dpi=420"
private const val TABLET = "spec:width=960dp,height=600dp,dpi=320"

// Galleries scroll past the phone viewport; forms are snapshotted on a tall canvas so every field is covered.
private const val FORM_PAGE = "spec:width=375dp,height=2400dp,dpi=320"

// The atoms gallery is the longest page in the catalog; it needs a taller canvas than the rest.
private const val ATOMS_PAGE = "spec:width=375dp,height=4000dp,dpi=320"

@Composable
private fun Snapshot(
    dark: Boolean = false,
    content: @Composable () -> Unit,
) {
    DsTheme(darkTheme = dark, animationsEnabled = false, content = content)
}

// ── Galleries (phone, light + dark) ─────────────────────────────

@PreviewTest
@Preview(name = "atoms", device = ATOMS_PAGE, showBackground = true)
@Composable
fun AtomsGalleryPhone() = Snapshot { AtomsGallery() }

@PreviewTest
@Preview(name = "atoms_dark", device = ATOMS_PAGE, showBackground = true)
@Composable
fun AtomsGalleryPhoneDark() = Snapshot(dark = true) { AtomsGallery() }

@PreviewTest
@Preview(name = "molecules", device = PHONE, showBackground = true)
@Composable
fun MoleculesGalleryPhone() = Snapshot { MoleculesGallery() }

@PreviewTest
@Preview(name = "molecules_dark", device = PHONE, showBackground = true)
@Composable
fun MoleculesGalleryPhoneDark() = Snapshot(dark = true) { MoleculesGallery() }

@PreviewTest
@Preview(name = "organisms", device = PHONE, showBackground = true)
@Composable
fun OrganismsGalleryPhone() = Snapshot { OrganismsGallery() }

@PreviewTest
@Preview(name = "organisms_dark", device = PHONE, showBackground = true)
@Composable
fun OrganismsGalleryPhoneDark() = Snapshot(dark = true) { OrganismsGallery() }

@PreviewTest
@Preview(name = "forms", device = FORM_PAGE, showBackground = true)
@Composable
fun FormsGalleryPhone() = Snapshot { FormsGallery() }

@PreviewTest
@Preview(name = "forms_dark", device = FORM_PAGE, showBackground = true)
@Composable
fun FormsGalleryPhoneDark() = Snapshot(dark = true) { FormsGallery() }

@PreviewTest
@Preview(name = "choices", device = FORM_PAGE, showBackground = true)
@Composable
fun ChoicesGalleryPhone() = Snapshot { ChoicesGallery() }

@PreviewTest
@Preview(name = "choices_dark", device = FORM_PAGE, showBackground = true)
@Composable
fun ChoicesGalleryPhoneDark() = Snapshot(dark = true) { ChoicesGallery() }

@PreviewTest
@Preview(name = "pickers", device = FORM_PAGE, showBackground = true)
@Composable
fun PickersGalleryPhone() = Snapshot { PickersGallery() }

@PreviewTest
@Preview(name = "pickers_dark", device = FORM_PAGE, showBackground = true)
@Composable
fun PickersGalleryPhoneDark() = Snapshot(dark = true) { PickersGallery() }

@PreviewTest
@Preview(name = "menu_cards", device = FORM_PAGE, showBackground = true)
@Composable
fun MenuCardsGalleryPhone() = Snapshot { MenuCardsGallery() }

@PreviewTest
@Preview(name = "menu_cards_dark", device = FORM_PAGE, showBackground = true)
@Composable
fun MenuCardsGalleryPhoneDark() = Snapshot(dark = true) { MenuCardsGallery() }

@PreviewTest
@Preview(name = "calendars", device = FORM_PAGE, showBackground = true)
@Composable
fun CalendarGalleryPhone() = Snapshot { CalendarGallery() }

@PreviewTest
@Preview(name = "calendars_dark", device = FORM_PAGE, showBackground = true)
@Composable
fun CalendarGalleryPhoneDark() = Snapshot(dark = true) { CalendarGallery() }

@PreviewTest
@Preview(name = "overlays", device = FORM_PAGE, showBackground = true)
@Composable
fun OverlaysGalleryPhone() = Snapshot { OverlaysGallery() }

@PreviewTest
@Preview(name = "overlays_dark", device = FORM_PAGE, showBackground = true)
@Composable
fun OverlaysGalleryPhoneDark() = Snapshot(dark = true) { OverlaysGallery() }

@PreviewTest
@Preview(name = "foundations", device = PHONE, showBackground = true)
@Composable
fun FoundationsGalleryPhone() = Snapshot { FoundationsGallery() }

// ── Screens: phone vs tablet ────────────────────────────────────

@PreviewTest
@Preview(name = "main_sales_phone", device = PHONE)
@Composable
fun MainSalesPhone() = Snapshot { MainSalesScreen(windowSize = DsWindowSize.Compact) }

@PreviewTest
@Preview(name = "main_sales_tablet", device = TABLET)
@Composable
fun MainSalesTablet() = Snapshot { MainSalesScreen(windowSize = DsWindowSize.Expanded) }

@PreviewTest
@Preview(name = "cart_phone", device = PHONE)
@Composable
fun CartPhone() = Snapshot { CartScreen() }

@PreviewTest
@Preview(name = "tips_phone", device = PHONE)
@Composable
fun TipsPhone() = Snapshot { TipsScreen() }

@PreviewTest
@Preview(name = "discount_phone", device = PHONE)
@Composable
fun DiscountPhone() = Snapshot { DiscountScreen() }

@PreviewTest
@Preview(name = "payment_phone", device = PHONE)
@Composable
fun PaymentPhone() = Snapshot { PaymentScreen() }

@PreviewTest
@Preview(name = "shell_phone", device = PHONE)
@Composable
fun ShellPhone() = Snapshot { AppShellScreen(windowSize = DsWindowSize.Compact, layoutDirection = LayoutDirection.Ltr) }

@PreviewTest
@Preview(name = "shell_tablet", device = TABLET)
@Composable
fun ShellTablet() = Snapshot { AppShellScreen(windowSize = DsWindowSize.Expanded, layoutDirection = LayoutDirection.Ltr) }

@PreviewTest
@Preview(name = "shell_tablet_rtl", device = TABLET)
@Composable
fun ShellTabletRtl() = Snapshot { AppShellScreen(windowSize = DsWindowSize.Expanded, layoutDirection = LayoutDirection.Rtl) }

@PreviewTest
@Preview(name = "nfc_phone", device = PHONE)
@Composable
fun NfcPhone() = Snapshot { NfcPaymentScreen() }

@PreviewTest
@Preview(name = "payment_success_phone", device = PHONE)
@Composable
fun PaymentSuccessPhone() = Snapshot { PaymentResultScreen(DsPaymentOutcome.Success) }

@PreviewTest
@Preview(name = "payment_failure_phone", device = PHONE)
@Composable
fun PaymentFailurePhone() = Snapshot { PaymentResultScreen(DsPaymentOutcome.Failure) }

@PreviewTest
@Preview(name = "onboarding_phone", device = PHONE)
@Composable
fun OnboardingPhone() = Snapshot { OnboardingScreen() }

@PreviewTest
@Preview(name = "onboarding_tablet", device = TABLET)
@Composable
fun OnboardingTablet() = Snapshot { OnboardingScreen() }

@PreviewTest
@Preview(name = "login_phone", device = PHONE)
@Composable
fun LoginPhone() = Snapshot { LoginScreen() }

@PreviewTest
@Preview(name = "login_tablet", device = TABLET)
@Composable
fun LoginTablet() = Snapshot { LoginScreen() }

@PreviewTest
@Preview(name = "login_loading_phone", device = PHONE)
@Composable
fun LoginLoadingPhone() = Snapshot { LoginScreen(loadingProvider = "google") }

@PreviewTest
@Preview(name = "auth_loading_phone", device = PHONE)
@Composable
fun AuthLoadingPhone() = Snapshot { AuthLoadingScreen() }
