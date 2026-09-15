package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

@Composable
fun DsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val c = DsTheme.colors
    Column(modifier) {
        if (label != null) {
            Text(label, style = DsTheme.typography.caption, color = c.textSecondary)
            Spacer(Modifier.height(4.dp))
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = singleLine,
            isError = isError,
            keyboardOptions = keyboardOptions,
            textStyle = DsTheme.typography.bodyStrong,
            shape = DsShapes.input,
            placeholder = placeholder?.let { { Text(it, style = DsTheme.typography.body, color = c.textTertiary) } },
            leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null, tint = c.textTertiary) } },
            trailingIcon = trailingContent,
            supportingText = supportingText?.let { { Text(it, style = DsTheme.typography.caption) } },
            colors = OutlinedTextFieldDefaults.colors(
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
private fun DsTextFieldPreview() = DsPreview {
    DsTextField("", {}, label = "Libellé (optionnel)", placeholder = "Misc service")
    Spacer(Modifier.height(8.dp))
    DsTextField("nom@exemple", {}, leadingIcon = DsIcons.Account, isError = true, supportingText = "Email invalide")
}
