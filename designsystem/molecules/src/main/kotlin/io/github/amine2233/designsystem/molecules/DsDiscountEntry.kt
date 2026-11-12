package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import io.github.amine2233.designsystem.atoms.DsErrorText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * The discount being typed at the till: a percent / fixed toggle, the entered [value] and the
 * resulting [discountLabel] / [discountValue] line.
 *
 * Purely presentational: the caller formats [value] and the discount line (see [dsDiscount] for the
 * maths) and drives the keypad, which lives outside this molecule. [error] is announced to TalkBack
 * as soon as it appears.
 */
@Composable
fun DsDiscountEntry(
    mode: DsDiscountMode,
    onModeChange: (DsDiscountMode) -> Unit,
    value: String,
    currencyLabel: String,
    discountLabel: String,
    discountValue: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    percentLabel: String = "Pourcentage",
    fixedAmountLabel: String = "Montant fixe",
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.smd)) {
        DsSegmentedControl(
            options = listOf(percentLabel, fixedAmountLabel),
            selectedIndex = if (mode == DsDiscountMode.Percent) 0 else 1,
            onSelect = { onModeChange(if (it == 0) DsDiscountMode.Percent else DsDiscountMode.FixedAmount) },
            modifier = Modifier.fillMaxWidth(),
            height = DsTheme.spacing.minTouchTarget,
        )
        DsAmountDisplay(value, unit = if (mode == DsDiscountMode.Percent) "%" else currencyLabel)
        DsTotalsRow(discountLabel, discountValue, emphasis = DsTotalsEmphasis.Discount)
        if (error != null) {
            DsErrorText(error, Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        }
    }
}

@DsComponentPreview
@Composable
private fun DsDiscountEntryPreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.lg)) {
            DsDiscountEntry(DsDiscountMode.Percent, {}, "10,00", "DA", "Remise", "− 108 DA")
            DsDiscountEntry(DsDiscountMode.FixedAmount, {}, "500,00", "DA", "Remise", "− 500 DA")
            DsDiscountEntry(
                DsDiscountMode.Percent,
                {},
                "150,00",
                "DA",
                "Remise",
                "− 1 080 DA",
                error = "La remise dépasse le total",
            )
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                DsDiscountEntry(DsDiscountMode.FixedAmount, {}, "500,00", "DA", "Remise", "− 500 DA")
            }
        }
    }
