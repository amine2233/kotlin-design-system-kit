package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Read-only field that opens something else — a date dialog, a color grid, the photo picker.
 *
 * It renders the closed state only: the value, a placeholder when empty, and the trailing
 * affordance. The component that owns the picker passes [onClick] and formats [valueText],
 * so every picker in the system has the same box, height and error treatment.
 */
@Composable
fun DsPickerField(
    valueText: String?,
    placeholder: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    DsFormField(
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        isError = isError,
        required = required,
        enabled = enabled,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = DsTheme.spacing.minTouchTarget)
                    .clip(DsShapes.sm)
                    .background(if (enabled) c.surfaceSubtle else c.surfaceMuted)
                    .border(1.dp, if (isError) c.error else c.border, DsShapes.sm)
                    .clickable(enabled = enabled, onClick = onClick)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
        ) {
            leadingContent?.invoke()
            Text(
                valueText ?: placeholder,
                style = DsTheme.typography.bodyStrong,
                color =
                    if (!enabled) {
                        c.textDisabled
                    } else if (valueText == null) {
                        c.textTertiary
                    } else {
                        c.textPrimary
                    },
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            Icon(trailingIcon, contentDescription = null, tint = if (enabled) c.textSecondary else c.textDisabled)
        }
    }
}

@DsComponentPreview
@Composable
private fun DsPickerFieldPreview() =
    DsPreview {
        DsPickerField("12/03/2026", "Sélectionner une date", DsIcons.Calendar, {}, label = "Date de clôture")
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsPickerField(null, "Sélectionner une heure", DsIcons.Clock, {}, label = "Heure", isError = true, supportingText = "Obligatoire")
    }
