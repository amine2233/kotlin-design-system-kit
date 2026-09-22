package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Label + control + supporting text: the frame shared by every form field.
 *
 * Fields own their control and delegate everything around it here, so a text field, a dropdown
 * and a date picker line up with the same label style, spacing and error treatment.
 * Use it directly only when building a new field; product code uses the fields themselves.
 */
@Composable
fun DsFormField(
    modifier: Modifier = Modifier,
    label: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val c = DsTheme.colors
    Column(modifier) {
        if (label != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, style = DsTheme.typography.caption, color = if (enabled) c.textSecondary else c.textDisabled)
                if (required) Text(" *", style = DsTheme.typography.caption, color = c.error)
            }
            Spacer(Modifier.height(DsTheme.spacing.xs))
        }
        content()
        if (supportingText != null) {
            Spacer(Modifier.height(DsTheme.spacing.xs))
            if (isError) {
                DsErrorText(supportingText)
            } else {
                Text(supportingText, style = DsTheme.typography.caption, color = c.textTertiary)
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsFormFieldPreview() =
    DsPreview {
        DsFormField(label = "Libellé", supportingText = "Visible sur le ticket") {
            DsText("Contrôle du champ", style = DsTheme.typography.bodyStrong)
        }
        Spacer(Modifier.height(DsTheme.spacing.smd))
        DsFormField(label = "E-mail", required = true, isError = true, supportingText = "Adresse invalide") {
            DsText("nom@exemple", style = DsTheme.typography.bodyStrong)
        }
    }
