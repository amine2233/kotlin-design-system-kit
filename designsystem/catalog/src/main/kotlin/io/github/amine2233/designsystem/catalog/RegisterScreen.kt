package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsCheckbox
import io.github.amine2233.designsystem.atoms.DsLogo
import io.github.amine2233.designsystem.atoms.DsPasswordField
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.atoms.DsTextField
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.templates.DsCenteredLayout

/**
 * Account creation form: name, e-mail, password and terms, then create or go back to sign-in.
 *
 * Stateless reference: values, error messages and the submitting flag come in, edits and actions go
 * out. Validation belongs to the caller; a non-null error marks its field and shows the message.
 */
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    name: String = "",
    email: String = "",
    password: String = "",
    termsAccepted: Boolean = false,
    nameError: String? = null,
    emailError: String? = null,
    passwordError: String? = null,
    submitting: Boolean = false,
    onNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onTermsChange: (Boolean) -> Unit = {},
    onSubmit: () -> Unit = {},
    onSignIn: () -> Unit = {},
) {
    val spacing = DsTheme.spacing
    DsCenteredLayout(modifier.background(DsTheme.colors.surface)) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.xl, vertical = spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DsLogo()
            Spacer(Modifier.height(spacing.md))
            DsText("Créer un compte", style = DsTheme.typography.displayMedium)
            Spacer(Modifier.height(spacing.sm))
            DsText(
                "Ouvrez l'accès à la caisse de votre commerce.",
                style = DsTheme.typography.bodyLarge,
                color = DsTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(spacing.xl))
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                DsTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = "Nom complet",
                    isError = nameError != null,
                    supportingText = nameError,
                    required = true,
                    enabled = !submitting,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                )
                DsTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "E-mail",
                    isError = emailError != null,
                    supportingText = emailError,
                    required = true,
                    enabled = !submitting,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                )
                DsPasswordField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Mot de passe",
                    isError = passwordError != null,
                    supportingText = passwordError,
                    required = true,
                    enabled = !submitting,
                    imeAction = ImeAction.Done,
                )
                DsCheckbox(
                    checked = termsAccepted,
                    onCheckedChange = onTermsChange,
                    label = "J'accepte les conditions d'utilisation",
                    enabled = !submitting,
                )
            }
            Spacer(Modifier.height(spacing.lg))
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spacing.smd)) {
                DsButton(
                    "Créer le compte",
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth(),
                    loading = submitting,
                )
                DsButton(
                    "J'ai déjà un compte",
                    onClick = onSignIn,
                    modifier = Modifier.fillMaxWidth(),
                    variant = DsButtonVariant.Ghost,
                    enabled = !submitting,
                )
            }
        }
    }
}

@DsScreenPreview
@Composable
private fun RegisterScreenPreview() = DsTheme { RegisterScreen() }

@DsScreenPreview
@Composable
private fun RegisterScreenErrorsPreview() =
    DsTheme {
        RegisterScreen(
            name = "A",
            email = "amine@exemple",
            password = "123",
            nameError = "Indiquez votre nom complet",
            emailError = "Adresse e-mail invalide",
            passwordError = "8 caractères minimum",
        )
    }

@DsScreenPreview
@Composable
private fun RegisterScreenSubmittingPreview() =
    DsTheme {
        RegisterScreen(
            name = "Amine Benali",
            email = "amine@exemple.fr",
            password = "motdepasse",
            termsAccepted = true,
            submitting = true,
        )
    }
