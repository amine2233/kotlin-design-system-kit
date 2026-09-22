package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsCheckbox
import io.github.amine2233.designsystem.atoms.DsFieldBox
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Summary shown in the closed box: the labels joined, or "n sélectionnés" past [max] of them.
 * Keeps the box on one line whatever the selection size.
 */
fun dsSummarizeSelection(
    labels: List<String>,
    max: Int = 2,
    more: (Int) -> String = { "$it sélectionnés" },
): String? =
    when {
        labels.isEmpty() -> null
        labels.size <= max -> labels.joinToString(", ")
        else -> more(labels.size)
    }

/**
 * Select field that keeps several options at once — allergens on a product, printers to send a
 * ticket to.
 *
 * The menu stays open while the user ticks boxes: closing on every pick makes choosing three
 * options a three-trip job. The selection crosses the boundary as a [Set] of indices into
 * [options], so the caller keeps its own option type.
 */
@Composable
fun DsMultiSelectField(
    options: List<String>,
    selected: Set<Int>,
    onSelectedChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Sélectionner",
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    summary: (List<String>) -> String? = { dsSummarizeSelection(it) },
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
                valueText = summary(selected.sorted().mapNotNull(options::getOrNull)),
                placeholder = placeholder,
                trailingIcon = DsIcons.ChevronDown,
                onClick = { open = true },
                isError = isError,
                enabled = enabled,
                active = open,
            )
            DropdownMenu(
                expanded = open,
                onDismissRequest = { open = false },
                modifier = Modifier.heightIn(max = 320.dp),
                containerColor = c.surface,
                shape = DsShapes.md,
            ) {
                options.forEachIndexed { i, option ->
                    val checked = i in selected
                    DropdownMenuItem(
                        text = { DsCheckbox(checked, { toggle(selected, i, onSelectedChange) }, label = option) },
                        onClick = { toggle(selected, i, onSelectedChange) },
                    )
                }
            }
        }
    }
}

private fun toggle(
    selected: Set<Int>,
    index: Int,
    onSelectedChange: (Set<Int>) -> Unit,
) {
    onSelectedChange(if (index in selected) selected - index else selected + index)
}

@DsComponentPreview
@Composable
private fun DsMultiSelectFieldPreview() =
    DsPreview {
        DsMultiSelectField(listOf("Gluten", "Lait", "Fruits à coque", "Soja"), setOf(0, 1), {}, label = "Allergènes")
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsMultiSelectField(listOf("Cuisine", "Bar", "Comptoir"), setOf(0, 1, 2), {}, label = "Imprimantes")
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsMultiSelectField(listOf("Cuisine", "Bar"), emptySet(), {}, label = "Vide", isError = true, supportingText = "Au moins une")
    }
