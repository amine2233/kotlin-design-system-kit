package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import io.github.amine2233.designsystem.atoms.DsRadioIndicator
import io.github.amine2233.designsystem.atoms.DsTextArea
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsQuantityStepper
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.molecules.DsTotalsRow

/** Single groups pick at most one option (radio rows); Multiple groups pick a range (checkbox rows). */
enum class DsModifierGroupType { Single, Multiple }

/** One choice in a [DsModifierGroup]; [priceDeltaMinor] is added to the base price, in minor units. */
@Immutable
data class DsModifierOption(
    val id: String,
    val name: String,
    val priceDeltaMinor: Long = 0L,
)

/**
 * A set of [options] with its selection rule. A Single group is required when [minSelections] is 1 and
 * optional when it is 0, and picks at most one option. A Multiple group picks between [minSelections] and
 * [maxSelections] options.
 */
@Immutable
data class DsModifierGroup(
    val id: String,
    val title: String,
    val options: List<DsModifierOption>,
    val type: DsModifierGroupType = DsModifierGroupType.Single,
    val minSelections: Int = 1,
    val maxSelections: Int = 1,
) {
    init {
        require(minSelections >= 0) { "minSelections must not be negative: $minSelections" }
        require(maxSelections >= 1) { "maxSelections must be at least 1: $maxSelections" }
        require(minSelections <= maxSelections) { "minSelections $minSelections exceeds maxSelections $maxSelections" }
        require(minSelections <= options.size) { "minSelections $minSelections exceeds the ${options.size} options" }
        if (type == DsModifierGroupType.Single) {
            require(minSelections <= 1) { "a Single group needs minSelections 0 or 1: $minSelections" }
            require(maxSelections == 1) { "a Single group needs maxSelections 1: $maxSelections" }
        }
        require(options.none { it.priceDeltaMinor < 0 }) { "priceDeltaMinor must not be negative" }
    }
}

enum class DsModifierGroupError { MissingRequired, BelowMinimum, AboveMaximum }

private val DsModifierGroup.effectiveMax: Int get() = if (type == DsModifierGroupType.Single) 1 else maxSelections

private fun DsModifierGroup.selectedCount(selectedOptionIds: Set<String>): Int = options.count { it.id in selectedOptionIds }

/** What is wrong with [group]'s selection, or `null` when it is valid. Only the ids of [group]'s options count. */
fun dsModifierGroupError(
    group: DsModifierGroup,
    selectedOptionIds: Set<String>,
): DsModifierGroupError? {
    val count = group.selectedCount(selectedOptionIds)
    return when {
        count > group.effectiveMax -> DsModifierGroupError.AboveMaximum
        group.type == DsModifierGroupType.Single && group.minSelections == 1 && count == 0 -> DsModifierGroupError.MissingRequired
        group.type == DsModifierGroupType.Multiple && count < group.minSelections -> DsModifierGroupError.BelowMinimum
        else -> null
    }
}

/** True when no group in [groups] has an error: the caller enables its confirm button with it. */
fun dsIsModifierSelectionValid(
    groups: List<DsModifierGroup>,
    selectedOptionIds: Set<String>,
): Boolean = groups.all { dsModifierGroupError(it, selectedOptionIds) == null }

/**
 * The selection after a tap on [optionId] in [group]. Single replaces the group's selection, and a tap on the
 * selected option clears it only when the group is optional. Multiple toggles, ignoring an add at the max.
 * Ids outside [group] are kept as they are.
 */
fun dsToggleModifierOption(
    group: DsModifierGroup,
    optionId: String,
    selectedOptionIds: Set<String>,
): Set<String> {
    val groupIds = group.options.map { it.id }.toSet()
    if (optionId !in groupIds) return selectedOptionIds
    val selected = optionId in selectedOptionIds
    return when (group.type) {
        DsModifierGroupType.Single -> {
            when {
                !selected -> selectedOptionIds - groupIds + optionId
                group.minSelections == 0 -> selectedOptionIds - optionId
                else -> selectedOptionIds
            }
        }

        DsModifierGroupType.Multiple -> {
            when {
                selected -> selectedOptionIds - optionId
                group.selectedCount(selectedOptionIds) >= group.maxSelections -> selectedOptionIds
                else -> selectedOptionIds + optionId
            }
        }
    }
}

/**
 * `(basePriceMinor + selected deltas) * quantity` in minor units. Selected ids that match no option are ignored.
 * Throws [IllegalArgumentException] on a negative price or a quantity below 1, and [ArithmeticException] on overflow.
 */
fun dsModifierTotal(
    basePriceMinor: Long,
    groups: List<DsModifierGroup>,
    selectedOptionIds: Set<String>,
    quantity: Int,
): Long {
    require(basePriceMinor >= 0) { "basePriceMinor must not be negative: $basePriceMinor" }
    require(quantity >= 1) { "quantity must be at least 1: $quantity" }
    val options = groups.flatMap { it.options }
    require(options.none { it.priceDeltaMinor < 0 }) { "priceDeltaMinor must not be negative" }
    val unit =
        options
            .filter { it.id in selectedOptionIds }
            .fold(basePriceMinor) { sum, option -> Math.addExact(sum, option.priceDeltaMinor) }
    return Math.multiplyExact(unit, quantity.toLong())
}

/**
 * Configures a product before it goes to the cart: option groups, notes, quantity and the running total.
 *
 * Stateless. The caller owns [selectedOptionIds] (one set of option ids across all groups), [notes] and
 * [quantity]; on a tap the next selection comes from [dsToggleModifierOption]. The caller also owns the
 * surrounding sheet or dialog and its confirm button, enabled with [dsIsModifierSelectionValid], and sets
 * [showErrors] when it wants each invalid group to say what is missing.
 */
@Composable
fun DsProductModifier(
    groups: List<DsModifierGroup>,
    selectedOptionIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    notes: String,
    onNotesChange: (String) -> Unit,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    basePriceMinor: Long,
    unit: String,
    modifier: Modifier = Modifier,
    showErrors: Boolean = false,
    maxQuantity: Int = 99,
    notesMaxLength: Int? = 200,
    requiredLabel: String = "Required",
    optionalLabel: String = "Optional",
    upToLabel: (Int) -> String = { "Up to $it" },
    rangeLabel: (Int, Int) -> String = { min, max -> "Choose $min to $max" },
    missingRequiredLabel: String = "Choose one option",
    belowMinimumLabel: (Int) -> String = { "Choose at least $it" },
    aboveMaximumLabel: (Int) -> String = { "Choose at most $it" },
    notesLabel: String = "Notes",
    notesPlaceholder: String? = null,
    quantityLabel: String = "Quantity",
    totalLabel: String = "Total",
) {
    val c = DsTheme.colors
    val spacing = DsTheme.spacing
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spacing.md)) {
        groups.forEach { group ->
            val hint =
                when {
                    group.type == DsModifierGroupType.Single -> if (group.minSelections == 1) requiredLabel else optionalLabel
                    group.minSelections == 0 -> upToLabel(group.maxSelections)
                    group.minSelections < group.maxSelections -> rangeLabel(group.minSelections, group.maxSelections)
                    else -> belowMinimumLabel(group.minSelections)
                }
            val error =
                if (showErrors) {
                    when (dsModifierGroupError(group, selectedOptionIds)) {
                        DsModifierGroupError.MissingRequired -> missingRequiredLabel
                        DsModifierGroupError.BelowMinimum -> belowMinimumLabel(group.minSelections)
                        DsModifierGroupError.AboveMaximum -> aboveMaximumLabel(group.effectiveMax)
                        null -> null
                    }
                } else {
                    null
                }
            ModifierGroupSection(group, hint, error, selectedOptionIds, onSelectionChange, unit)
        }
        DsTextArea(
            notes,
            onNotesChange,
            label = notesLabel,
            placeholder = notesPlaceholder,
            maxLength = notesMaxLength,
        )
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(min = spacing.minTouchTarget),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(quantityLabel, modifier = Modifier.weight(1f), style = DsTheme.typography.body, color = c.textPrimary)
            DsQuantityStepper(quantity, onQuantityChange, min = 1, max = maxQuantity)
        }
        val value = "${dsFormatMinorUnits(dsModifierTotal(basePriceMinor, groups, selectedOptionIds, quantity))} $unit"
        DsTotalsRow(
            totalLabel,
            value,
            Modifier.clearAndSetSemantics {
                contentDescription = "$totalLabel $value"
                liveRegion = LiveRegionMode.Polite
            },
            emphasis = DsTotalsEmphasis.Total,
        )
    }
}

@Composable
private fun ModifierGroupSection(
    group: DsModifierGroup,
    hint: String,
    error: String?,
    selectedOptionIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    unit: String,
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val spacing = DsTheme.spacing
    val atMax = group.type == DsModifierGroupType.Multiple && group.selectedCount(selectedOptionIds) >= group.maxSelections
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(group.title, modifier = Modifier.weight(1f), style = t.title, color = c.textPrimary)
            Text(hint, style = t.caption, color = c.textSecondary)
        }
        if (error != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(DsIcons.Error, contentDescription = null, tint = c.error)
                Text(error, style = t.label, color = c.error)
            }
        }
        group.options.forEach { option ->
            val selected = option.id in selectedOptionIds
            ModifierOptionRow(
                option = option,
                type = group.type,
                selected = selected,
                enabled = selected || !atMax,
                unit = unit,
                onClick = { onSelectionChange(dsToggleModifierOption(group, option.id, selectedOptionIds)) },
            )
        }
    }
}

@Composable
private fun ModifierOptionRow(
    option: DsModifierOption,
    type: DsModifierGroupType,
    selected: Boolean,
    enabled: Boolean,
    unit: String,
    onClick: () -> Unit,
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val textColor = if (enabled) c.textPrimary else c.textDisabled
    val touch =
        when (type) {
            DsModifierGroupType.Single -> {
                Modifier.selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick)
            }

            DsModifierGroupType.Multiple -> {
                Modifier.toggleable(value = selected, enabled = enabled, role = Role.Checkbox, onValueChange = { onClick() })
            }
        }
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = DsTheme.spacing.minTouchTarget).then(touch),
        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.smd),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (type) {
            DsModifierGroupType.Single -> {
                DsRadioIndicator(selected)
            }

            DsModifierGroupType.Multiple -> {
                Checkbox(
                    checked = selected,
                    onCheckedChange = null,
                    enabled = enabled,
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = c.primary,
                            uncheckedColor = c.borderStrong,
                            checkmarkColor = c.onPrimary,
                        ),
                )
            }
        }
        Text(
            option.name,
            modifier = Modifier.weight(1f),
            style = if (selected) t.bodyStrong else t.body,
            color = textColor,
        )
        if (option.priceDeltaMinor > 0) {
            Text(
                "+ ${dsFormatMinorUnits(option.priceDeltaMinor)} $unit",
                style = t.body,
                color = if (enabled) c.textSecondary else c.textDisabled,
            )
        }
    }
}

private val PreviewSize =
    DsModifierGroup(
        id = "size",
        title = "Taille",
        options =
            listOf(
                DsModifierOption("size-s", "Petit"),
                DsModifierOption("size-m", "Moyen", 5_000L),
                DsModifierOption("size-l", "Grand", 10_000L),
            ),
    )

private val PreviewExtras =
    DsModifierGroup(
        id = "extras",
        title = "Suppléments",
        options =
            listOf(
                DsModifierOption("extra-cheese", "Fromage", 2_500L),
                DsModifierOption("extra-bacon", "Bacon", 4_000L),
                DsModifierOption("extra-egg", "Œuf", 1_500L),
            ),
        type = DsModifierGroupType.Multiple,
        minSelections = 0,
        maxSelections = 2,
    )

private val PreviewSauce =
    DsModifierGroup(
        id = "sauce",
        title = "Sauce",
        options = listOf(DsModifierOption("sauce-none", "Sans sauce"), DsModifierOption("sauce-bbq", "Barbecue", 1_000L)),
        minSelections = 0,
    )

private val PreviewGroups = listOf(PreviewSize, PreviewExtras, PreviewSauce)

@Composable
private fun ProductModifierPreview(
    selected: Set<String> = emptySet(),
    notes: String = "",
    quantity: Int = 1,
    showErrors: Boolean = false,
    groups: List<DsModifierGroup> = PreviewGroups,
) = DsProductModifier(
    groups = groups,
    selectedOptionIds = selected,
    onSelectionChange = {},
    notes = notes,
    onNotesChange = {},
    quantity = quantity,
    onQuantityChange = {},
    basePriceMinor = 25_000L,
    unit = "DA",
    showErrors = showErrors,
)

@DsComponentPreview
@Composable
private fun DsProductModifierDefaultPreview() = DsPreview { ProductModifierPreview() }

@DsComponentPreview
@Composable
private fun DsProductModifierSelectedPreview() =
    DsPreview {
        ProductModifierPreview(selected = setOf("size-m", "extra-cheese"), notes = "Sans oignons", quantity = 2)
    }

@DsComponentPreview
@Composable
private fun DsProductModifierExtrasAtMaxPreview() =
    DsPreview { ProductModifierPreview(selected = setOf("size-l", "extra-cheese", "extra-bacon")) }

@DsComponentPreview
@Composable
private fun DsProductModifierErrorPreview() = DsPreview { ProductModifierPreview(selected = setOf("extra-egg"), showErrors = true) }

@DsComponentPreview
@Composable
private fun DsProductModifierRequiredOnlyPreview() =
    DsPreview {
        ProductModifierPreview(selected = setOf("size-s"), groups = listOf(PreviewSize))
    }
