package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Masked text entry with an optional reveal toggle.
 *
 * The reveal state is local — the caller only ever sees the value. Set [revealable] to false for
 * fields that must never be shown on screen (a card PIN on a shared terminal).
 */
@Composable
fun DsPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    required: Boolean = false,
    enabled: Boolean = true,
    revealable: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
) {
    var revealed by rememberSaveable { mutableStateOf(false) }
    val showing = revealable && revealed
    DsTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        leadingIcon = DsIcons.Lock,
        trailingContent =
            if (!revealable) {
                null
            } else {
                {
                    DsIconButton(
                        icon = if (showing) DsIcons.Hide else DsIcons.Reveal,
                        contentDescription = if (showing) "Masquer le mot de passe" else "Afficher le mot de passe",
                        onClick = { revealed = !revealed },
                        tint = DsTheme.colors.textSecondary,
                        enabled = enabled,
                    )
                }
            },
        isError = isError,
        supportingText = supportingText,
        required = required,
        enabled = enabled,
        visualTransformation = if (showing) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
    )
}

@DsComponentPreview
@Composable
private fun DsPasswordFieldPreview() =
    DsPreview {
        DsPasswordField("motdepasse", {}, label = "Mot de passe", required = true)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsPasswordField("123", {}, label = "Code caisse", isError = true, supportingText = "6 chiffres minimum", revealable = false)
    }
