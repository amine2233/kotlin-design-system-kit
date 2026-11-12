package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.atoms.DsLogo
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsSettingsRow
import io.github.amine2233.designsystem.molecules.DsSettingsVariant
import io.github.amine2233.designsystem.organisms.DsTopBar
import io.github.amine2233.designsystem.templates.DsCenteredLayout
import io.github.amine2233.designsystem.templates.DsScreenScaffold

/**
 * "About this app": identity and version, then the legal and support entry points, copyright last.
 *
 * Every row reports its tap; the screen opens nothing by itself.
 */
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    version: String = "1.0.0",
    onBack: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onLicensesClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
) {
    val spacing = DsTheme.spacing
    DsScreenScaffold(modifier = modifier, topBar = { DsTopBar("À propos", onBack = onBack) }) {
        DsCenteredLayout {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = spacing.screen, vertical = spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DsLogo()
                Spacer(Modifier.height(spacing.md))
                DsText("Caisse Pro", style = DsTheme.typography.headline)
                Spacer(Modifier.height(spacing.xs))
                DsText("Version $version", style = DsTheme.typography.label, color = DsTheme.colors.textSecondary)
                Spacer(Modifier.height(spacing.md))
                DsText(
                    "La caisse enregistreuse de votre commerce : ventes, paiements et tickets au même endroit.",
                    style = DsTheme.typography.body,
                    color = DsTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(spacing.lg))
                DsCard(Modifier.fillMaxWidth()) {
                    Column {
                        DsSettingsRow("Conditions d'utilisation", DsSettingsVariant.Navigation(onTermsClick), icon = DsIcons.Receipt)
                        DsDivider()
                        DsSettingsRow("Politique de confidentialité", DsSettingsVariant.Navigation(onPrivacyClick), icon = DsIcons.Lock)
                        DsDivider()
                        DsSettingsRow("Licences open source", DsSettingsVariant.Navigation(onLicensesClick), icon = DsIcons.Info)
                        DsDivider()
                        DsSettingsRow("Contacter le support", DsSettingsVariant.Navigation(onSupportClick), icon = DsIcons.Phone)
                    }
                }
                Spacer(Modifier.height(spacing.lg))
                DsText("© 2026 Caisse Pro", style = DsTheme.typography.caption, color = DsTheme.colors.textTertiary)
            }
        }
    }
}

@DsScreenPreview
@Composable
private fun AboutScreenPreview() = DsTheme { AboutScreen() }
