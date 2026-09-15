package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Full-width labelled switch (Catalogue "Variantes", "Requis"). */
@Composable
fun DsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
) {
    val c = DsTheme.colors
    Row(
        modifier = modifier
            .then(if (label != null) Modifier.fillMaxWidth() else Modifier)
            .heightIn(min = DsTheme.spacing.minTouchTarget)
            .toggleable(value = checked, enabled = enabled, role = Role.Switch, onValueChange = onCheckedChange),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (label != null) Text(label, style = DsTheme.typography.bodyStrong, color = if (enabled) c.textPrimary else c.textDisabled)
        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = c.onPrimary,
                checkedTrackColor = c.primary,
                uncheckedThumbColor = c.surface,
                uncheckedTrackColor = c.borderStrong,
                uncheckedBorderColor = c.borderStrong,
            ),
        )
    }
}

@Composable
fun DsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
) {
    val c = DsTheme.colors
    Row(
        modifier = modifier
            .heightIn(min = DsTheme.spacing.minTouchTarget)
            .toggleable(value = checked, enabled = enabled, role = Role.Checkbox, onValueChange = onCheckedChange),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = CheckboxDefaults.colors(checkedColor = c.primary, uncheckedColor = c.borderStrong, checkmarkColor = c.onPrimary),
        )
        if (label != null) Text(label, style = DsTheme.typography.body, color = if (enabled) c.textPrimary else c.textDisabled)
    }
}

/** Labelled radio built on [DsRadioIndicator] so it matches the article-picker rows. */
@Composable
fun DsRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val c = DsTheme.colors
    Row(
        modifier = modifier
            .heightIn(min = DsTheme.spacing.minTouchTarget)
            .selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        DsRadioIndicator(selected)
        Text(label, style = if (selected) DsTheme.typography.bodyStrong else DsTheme.typography.body, color = if (!enabled) c.textDisabled else if (selected) c.primary else c.textPrimary)
    }
}

@DsComponentPreview
@Composable
private fun DsSelectionControlsPreview() = DsPreview {
    DsSwitch(true, {}, label = "Variantes activées")
    DsSwitch(false, {}, label = "Désactivé", enabled = false)
    DsCheckbox(true, {}, label = "Imprimer le ticket")
    DsCheckbox(false, {}, label = "Envoyer par e-mail")
    DsRadioButton(true, {}, "Espèces")
    DsRadioButton(false, {}, "Carte")
}
