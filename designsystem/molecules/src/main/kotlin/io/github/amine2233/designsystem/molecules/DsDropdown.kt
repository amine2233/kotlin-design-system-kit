package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsFieldBox
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Select field ("Motif : Fidélité ▾", TVA rate per line). */
@Composable
fun DsDropdown(
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Sélectionner",
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
) {
    val c = DsTheme.colors
    var open by remember { mutableStateOf(false) }
    DsFormField(
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        isError = isError,
        required = required,
        enabled = enabled,
    ) {
        Box {
            DsFieldBox(
                valueText = selectedIndex?.let { options.getOrNull(it) },
                placeholder = placeholder,
                trailingIcon = DsIcons.ChevronDown,
                onClick = { open = true },
                isError = isError,
                enabled = enabled,
                active = open,
            )
            DropdownMenu(expanded = open, onDismissRequest = { open = false }, containerColor = c.surface, shape = DsShapes.md) {
                options.forEachIndexed { i, option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                style = DsTheme.typography.body,
                                color = if (i == selectedIndex) c.primary else c.textPrimary,
                            )
                        },
                        onClick = {
                            open = false
                            onSelect(i)
                        },
                    )
                }
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsDropdownPreview() =
    DsPreview {
        DsDropdown(listOf("Fidélité", "Geste commercial", "Erreur de saisie"), 0, {}, label = "Motif")
        Spacer(Modifier.height(8.dp))
        DsDropdown(listOf("20%", "10%"), null, {}, placeholder = "Taux de TVA")
    }
