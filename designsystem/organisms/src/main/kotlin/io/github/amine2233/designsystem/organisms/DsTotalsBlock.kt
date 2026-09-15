package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.molecules.DsTotalsRow

@Immutable
data class DsTotalsLine(val label: String, val value: String, val emphasis: DsTotalsEmphasis = DsTotalsEmphasis.Normal)

/** HT → TVA breakdown → discount → tip → TTC. Order is the caller's responsibility (see CLAUDE.md calc order). */
@Composable
fun DsTotalsBlock(
    lines: List<DsTotalsLine>,
    total: DsTotalsLine,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { DsTotalsRow(it.label, it.value, emphasis = it.emphasis) }
        Spacer(Modifier.height(4.dp))
        DsDivider()
        Spacer(Modifier.height(4.dp))
        DsTotalsRow(total.label, total.value, emphasis = DsTotalsEmphasis.Total, valueColor = null)
    }
}

@DsComponentPreview
@Composable
private fun DsTotalsBlockPreview() = DsPreview {
    DsTotalsBlock(
        lines = listOf(DsTotalsLine("Sous-total HT", "1 025 DA"), DsTotalsLine("TVA 10%", "98 DA"), DsTotalsLine("Remise totale", "−49 DA", DsTotalsEmphasis.Discount)),
        total = DsTotalsLine("Total TTC", "1 080 DA"),
    )
}
