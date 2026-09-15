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

@Composable
private fun Snapshot(
    dark: Boolean = false,
    content: @Composable () -> Unit,
) {
    DsTheme(darkTheme = dark, animationsEnabled = false, content = content)
}

// ── Galleries (phone, light + dark) ─────────────────────────────

@PreviewTest
@Preview(name = "atoms", device = PHONE, showBackground = true)
@Composable
fun AtomsGalleryPhone() = Snapshot { AtomsGallery() }

@PreviewTest
@Preview(name = "atoms_dark", device = PHONE, showBackground = true)
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
@Preview(name = "auth_loading_phone", device = PHONE)
@Composable
fun AuthLoadingPhone() = Snapshot { AuthLoadingScreen() }
