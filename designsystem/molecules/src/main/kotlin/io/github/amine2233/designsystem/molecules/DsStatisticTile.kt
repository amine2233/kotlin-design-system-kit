package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.atoms.DsIconTile
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Which way a metric moved since the period it is compared with. */
enum class DsTrendDirection { Up, Down, Flat }

/** Whether a trend is good news, bad news or neither — what the trend row is coloured by. */
enum class DsTrendTone { Positive, Negative, Neutral }

/**
 * The trend shown under a [DsStatisticTile]: the [direction] arrow and the caller-formatted [delta].
 *
 * [upIsGood] is false for metrics where a rise is bad news — refunds, waiting time — so a fall reads
 * as positive.
 */
@Immutable
data class DsStatisticTrend(
    val direction: DsTrendDirection,
    val delta: String,
    val upIsGood: Boolean = true,
)

/** The tone of a trend: flat is always neutral, otherwise [upIsGood] decides which way is good. */
fun dsTrendTone(
    direction: DsTrendDirection,
    upIsGood: Boolean,
): DsTrendTone =
    when (direction) {
        DsTrendDirection.Flat -> DsTrendTone.Neutral
        DsTrendDirection.Up -> if (upIsGood) DsTrendTone.Positive else DsTrendTone.Negative
        DsTrendDirection.Down -> if (upIsGood) DsTrendTone.Negative else DsTrendTone.Positive
    }

/**
 * The default French description of a [DsStatisticTile]: "Ventes, 1 250 DA", followed by the
 * direction in words and the delta when there is a trend — "Ventes, 1 250 DA, en hausse +12 %".
 */
fun dsStatisticTileDescription(
    label: String,
    value: String,
    trend: DsStatisticTrend?,
): String {
    val base = "$label, $value"
    if (trend == null) return base
    val direction =
        when (trend.direction) {
            DsTrendDirection.Up -> "en hausse"
            DsTrendDirection.Down -> "en baisse"
            DsTrendDirection.Flat -> "stable"
        }
    return "$base, $direction ${trend.delta}"
}

/**
 * One figure on a dashboard: a prominent value, its label, and optionally how it moved.
 *
 * The tile fills the width it is given so several sit side by side with `Modifier.weight(1f)`; give
 * that row `Modifier.height(IntrinsicSize.Min)` and each tile `fillMaxHeight()` so tiles with and
 * without a trend line up. Long values ellipsize on one line rather than growing the tile. The trend
 * colour comes from [dsTrendTone], never from the caller.
 *
 * TalkBack reads the tile once, as a single description built by [contentDescription]. With
 * [onClick] it stays one clickable element.
 */
@Composable
fun DsStatisticTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    trend: DsStatisticTrend? = null,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    contentDescription: (label: String, value: String, trend: DsStatisticTrend?) -> String = ::dsStatisticTileDescription,
) {
    val c = DsTheme.colors
    val description = contentDescription(label, value, trend)
    DsCard(
        modifier = modifier.fillMaxWidth().heightIn(min = DsTheme.spacing.minTouchTarget),
        onClick = onClick,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(DsTheme.spacing.smd)
                    .clearAndSetSemantics { this.contentDescription = description },
            horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.smd),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) DsIconTile(icon)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xxs)) {
                DsText(value, style = DsTheme.typography.titleLarge, color = c.textPrimary, maxLines = 1)
                DsText(label, style = DsTheme.typography.caption, color = c.textSecondary, maxLines = 1)
                if (trend != null) {
                    val tint =
                        when (dsTrendTone(trend.direction, trend.upIsGood)) {
                            DsTrendTone.Positive -> c.success
                            DsTrendTone.Negative -> c.error
                            DsTrendTone.Neutral -> c.textSecondary
                        }
                    val arrow =
                        when (trend.direction) {
                            DsTrendDirection.Up -> DsIcons.TrendUp
                            DsTrendDirection.Down -> DsIcons.TrendDown
                            DsTrendDirection.Flat -> DsIcons.TrendFlat
                        }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(arrow, contentDescription = null, tint = tint, modifier = Modifier.size(DsTheme.spacing.md))
                        DsText(trend.delta, style = DsTheme.typography.labelStrong, color = tint, maxLines = 1)
                    }
                }
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsStatisticTilePreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
            DsStatisticTile("1 250 DA", "Ventes")
            DsStatisticTile("48", "Commandes", icon = DsIcons.Receipt)
            DsStatisticTile("1 250 DA", "Ventes", trend = DsStatisticTrend(DsTrendDirection.Up, "+12 %"))
            DsStatisticTile("36", "Clients", trend = DsStatisticTrend(DsTrendDirection.Down, "−4 %"))
            DsStatisticTile("820 DA", "Panier moyen", trend = DsStatisticTrend(DsTrendDirection.Flat, "0 %"))
            DsStatisticTile(
                "3",
                "Remboursements",
                trend = DsStatisticTrend(DsTrendDirection.Down, "−2", upIsGood = false),
            )
            DsStatisticTile("12 min", "Attente moyenne", onClick = {})
            Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                DsStatisticTile(
                    "1 234 567 890,00 DA",
                    "Chiffre d'affaires cumulé de l'exercice",
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
                DsStatisticTile("48", "Commandes", modifier = Modifier.weight(1f).fillMaxHeight())
                DsStatisticTile(
                    "92 %",
                    "Satisfaction",
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    trend = DsStatisticTrend(DsTrendDirection.Up, "+3 %"),
                )
            }
        }
    }
