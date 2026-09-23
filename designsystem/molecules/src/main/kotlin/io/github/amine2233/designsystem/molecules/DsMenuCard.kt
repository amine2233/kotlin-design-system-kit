package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.atoms.DsTag
import io.github.amine2233.designsystem.atoms.DsTagTone
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/**
 * The two shapes a menu card takes on a POS.
 *
 * [Tile] is the product grid: media on top, text below, sized by the grid cell.
 * [Row] is a list — the order review, a search result, a tablet side pane — media at the start and
 * the footer trailing, so a stepper stays under the thumb.
 */
enum class DsMenuCardVariant { Tile, Row }

/**
 * Product card for a menu-order screen: header, body, optional footer, optional media.
 *
 * Slots rather than fixed fields, because what a menu shows changes per venue — allergens here, a
 * preparation time there, a stepper only once the item is in the cart. The card owns the frame
 * (surface, border, selected state, padding, the two layouts); the screen owns the content.
 * The convenience overload below covers the common name/price/description case.
 */
@Composable
fun DsMenuCard(
    modifier: Modifier = Modifier,
    variant: DsMenuCardVariant = DsMenuCardVariant.Tile,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    media: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    header: @Composable () -> Unit,
    body: @Composable () -> Unit,
) {
    val c = DsTheme.colors
    DsCard(
        modifier = modifier,
        onClick = onClick,
        selected = selected,
        containerColor = if (selected) c.primaryTint else c.surface,
    ) {
        when (variant) {
            DsMenuCardVariant.Tile -> {
                Column {
                    if (media != null) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(96.dp)
                                .background(if (selected) c.primaryContainerBorder else c.surfaceMuted),
                            contentAlignment = Alignment.Center,
                            content = { media() },
                        )
                    }
                    Column(
                        Modifier.padding(DsTheme.spacing.smd),
                        verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
                    ) {
                        header()
                        body()
                        if (footer != null) {
                            Box(Modifier.padding(top = DsTheme.spacing.xs)) { footer() }
                        }
                    }
                }
            }

            DsMenuCardVariant.Row -> {
                Row(
                    Modifier.padding(DsTheme.spacing.smd),
                    horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.smd),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (media != null) {
                        Box(
                            Modifier
                                .size(64.dp)
                                .clip(DsShapes.sm)
                                .background(if (selected) c.primaryContainerBorder else c.surfaceMuted),
                            contentAlignment = Alignment.Center,
                            content = { media() },
                        )
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xxs)) {
                        header()
                        body()
                    }
                    if (footer != null) footer()
                }
            }
        }
    }
}

/**
 * The common menu item: name and price in the header, description and an optional tag in the body.
 * Reach for the slot version above when a venue needs something else in there.
 */
@Composable
fun DsMenuCard(
    name: String,
    price: String,
    modifier: Modifier = Modifier,
    variant: DsMenuCardVariant = DsMenuCardVariant.Tile,
    description: String? = null,
    tag: String? = null,
    tagTone: DsTagTone = DsTagTone.Primary,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    media: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    DsMenuCard(
        modifier = modifier,
        variant = variant,
        selected = selected,
        onClick = onClick,
        media = media,
        footer = footer,
        header = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DsText(
                    name,
                    style = DsTheme.typography.bodyStrong,
                    color = c.textPrimary,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                )
                DsText(price, style = DsTheme.typography.labelStrong, color = c.primary, maxLines = 1)
            }
        },
        body = {
            Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs)) {
                if (description != null) {
                    DsText(
                        description,
                        style = DsTheme.typography.caption,
                        color = c.textSecondary,
                        maxLines = if (variant == DsMenuCardVariant.Tile) 2 else 1,
                    )
                }
                if (tag != null) DsTag(tag, tone = tagTone)
            }
        },
    )
}

@DsComponentPreview
@Composable
private fun DsMenuCardPreview() =
    DsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
            DsMenuCard(
                "Cappuccino",
                "200 DA",
                modifier = Modifier.weight(1f),
                description = "Double expresso, lait vapeur",
                tag = "Nouveau",
                onClick = {},
            )
            DsMenuCard(
                "Crème brûlée",
                "350 DA",
                modifier = Modifier.weight(1f),
                description = "Vanille de Madagascar",
                selected = true,
                onClick = {},
            )
        }
        DsMenuCard(
            "Espresso",
            "150 DA",
            variant = DsMenuCardVariant.Row,
            description = "Ristretto, 25 ml",
            onClick = {},
            footer = { DsText("×2", style = DsTheme.typography.labelStrong, color = DsTheme.colors.primary) },
        )
    }
