package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import io.github.amine2233.designsystem.atoms.DsChip
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsAmountDisplay
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.molecules.DsTotalsRow

object DsCashTenderDefaults {
    /** 100, 500, 1 000 and 2 000 in a 2-decimal currency. */
    val Steps: List<Long> = listOf(10_000L, 50_000L, 100_000L, 200_000L)
    val MaxRounded: Int = 3

    /** Keypad entry cap: 99 999 999,99. */
    val MaxTendered: Long = 9_999_999_999L
}

sealed interface DsCashTenderStatus {
    data object Empty : DsCashTenderStatus

    data class Change(
        val amount: Long,
    ) : DsCashTenderStatus

    data class Missing(
        val amount: Long,
    ) : DsCashTenderStatus
}

/** True only for [DsCashTenderStatus.Change]: the caller enables its confirm button with it. */
val DsCashTenderStatus.isSufficient: Boolean get() = this is DsCashTenderStatus.Change

/**
 * Quick-tender bills in minor units: [amountDue] first ("exact"), then for each positive step the smallest
 * multiple strictly above it — deduplicated, ascending, at most [maxRounded]. Steps that would overflow are skipped.
 */
fun dsQuickTenders(
    amountDue: Long,
    steps: List<Long> = DsCashTenderDefaults.Steps,
    maxRounded: Int = DsCashTenderDefaults.MaxRounded,
): List<Long> {
    require(amountDue >= 0) { "amountDue must not be negative: $amountDue" }
    if (amountDue == 0L) return listOf(0L)
    val rounded =
        steps
            .filter { it > 0 }
            .mapNotNull { step -> nextMultipleAbove(amountDue, step) }
            .filter { it != amountDue }
            .distinct()
            .sorted()
            .take(maxRounded)
    return listOf(amountDue) + rounded
}

private fun nextMultipleAbove(
    amount: Long,
    step: Long,
): Long? {
    val quotient = amount / step
    return if (quotient >= Long.MAX_VALUE / step) null else (quotient + 1) * step
}

fun dsCashTenderStatus(
    amountDue: Long,
    tendered: Long,
): DsCashTenderStatus {
    require(amountDue >= 0) { "amountDue must not be negative: $amountDue" }
    require(tendered >= 0) { "tendered must not be negative: $tendered" }
    return when {
        tendered == 0L && amountDue > 0 -> DsCashTenderStatus.Empty
        tendered >= amountDue -> DsCashTenderStatus.Change(tendered - amountDue)
        else -> DsCashTenderStatus.Missing(amountDue - tendered)
    }
}

/** Minor-unit counterpart of [String.applyKey]: digits shift in from the right, capped at [DsCashTenderDefaults.MaxTendered]. */
fun Long.applyKey(key: DsKey): Long =
    when (key) {
        is DsKey.Digit -> {
            val digit = key.value.digitToInt()
            if (this > (DsCashTenderDefaults.MaxTendered - digit) / 10) this else this * 10 + digit
        }

        DsKey.Backspace -> {
            this / 10
        }

        DsKey.Comma -> {
            this
        }
    }

/** Formats minor units in the keypad's layout: "1 500,00". */
fun dsFormatMinorUnits(amount: Long): String {
    require(amount >= 0) { "amount must not be negative: $amount" }
    val major =
        (amount / 100)
            .toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()
    return "$major,${(amount % 100).toString().padStart(2, '0')}"
}

/**
 * Cash payment panel: amount due, quick-tender bills, tendered amount, change or missing amount, keypad.
 *
 * Stateless. The caller owns [tendered] and [selectedQuickTender]: set both in [onQuickTender], and in [onKey]
 * apply the key with [Long.applyKey] and set [selectedQuickTender] back to `null`, so typing clears the bill
 * selection even when the typed value matches a bill. Use [dsCashTenderStatus] and [isSufficient] to enable
 * the caller's confirm button.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DsCashTender(
    amountDue: Long,
    tendered: Long,
    onKey: (DsKey) -> Unit,
    onQuickTender: (Long) -> Unit,
    unit: String,
    modifier: Modifier = Modifier,
    selectedQuickTender: Long? = null,
    steps: List<Long> = DsCashTenderDefaults.Steps,
    amountDueLabel: String = "Amount due",
    tenderedLabel: String = "Tendered",
    exactLabel: String = "Exact",
    changeLabel: String = "Change due",
    missingLabel: String = "Still missing",
    keypadRowModifier: Modifier = Modifier,
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val spacing = DsTheme.spacing
    val due = dsFormatMinorUnits(amountDue)
    val tenderedText = dsFormatMinorUnits(tendered)
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spacing.smd)) {
        Row(
            modifier = Modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = "$amountDueLabel $due $unit" },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(amountDueLabel, style = t.body, color = c.textSecondary)
            Text("$due $unit", style = t.headline, color = c.textPrimary)
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth().semantics(mergeDescendants = true) {},
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            dsQuickTenders(amountDue, steps).forEachIndexed { index, bill ->
                DsChip(
                    if (index == 0) exactLabel else dsFormatMinorUnits(bill),
                    Modifier.heightIn(min = spacing.minTouchTarget),
                    selected = bill == selectedQuickTender,
                    onClick = { onQuickTender(bill) },
                )
            }
        }
        DsAmountDisplay(
            tenderedText,
            unit,
            Modifier.clearAndSetSemantics { contentDescription = "$tenderedLabel $tenderedText $unit" },
            label = tenderedLabel,
        )
        CashTenderStatusRow(dsCashTenderStatus(amountDue, tendered), unit, changeLabel, missingLabel)
        DsNumericKeypad(onKey = onKey, rowModifier = keypadRowModifier)
    }
}

@Composable
private fun CashTenderStatusRow(
    status: DsCashTenderStatus,
    unit: String,
    changeLabel: String,
    missingLabel: String,
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    when (status) {
        DsCashTenderStatus.Empty -> {
            val value = "${dsFormatMinorUnits(0)} $unit"
            DsTotalsRow(
                changeLabel,
                value,
                Modifier.clearAndSetSemantics { contentDescription = "$changeLabel $value" },
                emphasis = DsTotalsEmphasis.Total,
                valueColor = c.textTertiary,
            )
        }

        is DsCashTenderStatus.Change -> {
            val value = "${dsFormatMinorUnits(status.amount)} $unit"
            DsTotalsRow(
                changeLabel,
                value,
                Modifier.clearAndSetSemantics { contentDescription = "$changeLabel $value" },
                emphasis = DsTotalsEmphasis.Total,
            )
        }

        is DsCashTenderStatus.Missing -> {
            val value = "${dsFormatMinorUnits(status.amount)} $unit"
            Row(
                modifier = Modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = "$missingLabel $value" },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(DsIcons.Error, contentDescription = null, tint = c.error)
                    Text(missingLabel, style = t.title, color = c.error)
                }
                Text(value, style = t.headline, color = c.error)
            }
        }
    }
}

private const val PREVIEW_DUE = 118_800L

@Composable
private fun CashTenderPreview(
    tendered: Long,
    selectedQuickTender: Long? = null,
) = DsPreview {
    DsCashTender(
        amountDue = PREVIEW_DUE,
        tendered = tendered,
        onKey = {},
        onQuickTender = {},
        unit = "DA",
        selectedQuickTender = selectedQuickTender,
    )
}

@DsComponentPreview
@Composable
private fun DsCashTenderEmptyPreview() = CashTenderPreview(tendered = 0)

@DsComponentPreview
@Composable
private fun DsCashTenderExactPreview() = CashTenderPreview(tendered = PREVIEW_DUE, selectedQuickTender = PREVIEW_DUE)

@DsComponentPreview
@Composable
private fun DsCashTenderChangePreview() = CashTenderPreview(tendered = 150_000, selectedQuickTender = 150_000)

@DsComponentPreview
@Composable
private fun DsCashTenderInsufficientPreview() = CashTenderPreview(tendered = 100_000)
