package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.atoms.DsLogo
import io.github.amine2233.designsystem.atoms.DsOAuthButton
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.templates.DsCenteredLayout

/**
 * Sign-in with identity providers only — no e-mail, no password.
 *
 * The reference for an OAuth-only entry point: providers stacked with one visual weight so none
 * reads as the "real" way in, and the legal line last. Logos are the app's own assets; this screen
 * stands in with DS icons.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loadingProvider: String? = null,
    onSignIn: (String) -> Unit = {},
) {
    DsCenteredLayout(modifier.background(DsTheme.colors.surface)) {
        Column(
            Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DsLogo(size = 72.dp)
            Spacer(Modifier.height(16.dp))
            DsText("Caisse Pro", style = DsTheme.typography.displayMedium)
            Spacer(Modifier.height(8.dp))
            DsText(
                "Connectez-vous avec le compte de votre commerce.",
                style = DsTheme.typography.bodyLarge,
                color = DsTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(32.dp))
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DsOAuthButton(
                    "Continuer avec Google",
                    onClick = { onSignIn("google") },
                    loading = loadingProvider == "google",
                    logo = { Icon(DsIcons.Account, contentDescription = null) },
                )
                DsOAuthButton(
                    "Continuer avec Apple",
                    onClick = { onSignIn("apple") },
                    loading = loadingProvider == "apple",
                    logo = { Icon(DsIcons.Store, contentDescription = null) },
                )
                DsOAuthButton(
                    "Continuer avec Microsoft",
                    onClick = { onSignIn("microsoft") },
                    loading = loadingProvider == "microsoft",
                    logo = { Icon(DsIcons.Register, contentDescription = null) },
                )
            }
            Spacer(Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                DsDivider(Modifier.weight(1f))
                DsText(
                    "Accès réservé au personnel",
                    style = DsTheme.typography.caption,
                    color = DsTheme.colors.textTertiary,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
                DsDivider(Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            DsText(
                "Conditions d'utilisation · Confidentialité",
                style = DsTheme.typography.caption,
                color = DsTheme.colors.textTertiary,
            )
        }
    }
}

@DsScreenPreview
@Composable
private fun LoginScreenPreview() = DsTheme { LoginScreen() }
