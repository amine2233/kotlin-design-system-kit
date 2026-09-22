package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Multi-line entry for free text — a note on an order, an address, a refund reason.
 *
 * [maxLength] both shows the counter and refuses longer input, so the caller never has to
 * trim the value it gets back.
 */
@Composable
fun DsTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    required: Boolean = false,
    enabled: Boolean = true,
    minLines: Int = 3,
    maxLines: Int = 8,
    maxLength: Int? = null,
) {
    val c = DsTheme.colors
    DsFormField(
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        trailingSupportingText = maxLength?.let { "${value.length}/$it" },
        isError = isError,
        required = required,
        enabled = enabled,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { if (maxLength == null || it.length <= maxLength) onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false,
            minLines = minLines,
            maxLines = maxLines,
            isError = isError,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            textStyle = DsTheme.typography.body,
            shape = DsShapes.md,
            placeholder = placeholder?.let { { Text(it, style = DsTheme.typography.body, color = c.textTertiary) } },
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = c.surface,
                    unfocusedContainerColor = c.surfaceSubtle,
                    focusedBorderColor = c.primary,
                    unfocusedBorderColor = c.border,
                    errorBorderColor = c.error,
                    cursorColor = c.primary,
                    focusedTextColor = c.textPrimary,
                    unfocusedTextColor = c.textPrimary,
                ),
        )
    }
}

@DsComponentPreview
@Composable
private fun DsTextAreaPreview() =
    DsPreview {
        DsTextArea("Sans oignons, servir le café après le plat.", {}, label = "Note de commande", maxLength = 140)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsTextArea("", {}, label = "Motif de l'avoir", placeholder = "Décrivez le motif", isError = true, supportingText = "Obligatoire")
    }
