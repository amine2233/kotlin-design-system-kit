package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.amine2233.designsystem.atoms.DsTag
import io.github.amine2233.designsystem.atoms.DsTagTone
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** One tag in a [DsTagGroup]: what it says and what it means. */
@Immutable
data class DsTagEntry(
    val text: String,
    val tone: DsTagTone = DsTagTone.Neutral,
)

/**
 * How a [DsTagGroup] deals with more tags than fit on a line.
 *
 * [Wrap] flows onto further lines and never scrolls — a detail panel, where every tag must be
 * readable without interaction. [Scroll] keeps one line and scrolls horizontally — a dense list
 * row, where the group must not push the rest of the row down.
 */
enum class DsTagLayout { Wrap, Scroll }

/**
 * Splits [tags] into what is shown and how many are hidden.
 *
 * Kept separate from the composable so the rule — "show [max], count the rest" — can be tested and
 * reused for an accessibility label or a tooltip listing the hidden ones.
 */
fun dsVisibleTags(
    tags: List<DsTagEntry>,
    max: Int? = null,
): Pair<List<DsTagEntry>, Int> =
    when {
        max == null || max >= tags.size -> tags to 0
        max <= 0 -> emptyList<DsTagEntry>() to tags.size
        else -> tags.take(max) to tags.size - max
    }

/**
 * A set of read-only tags — allergens, order states, a product's labels.
 *
 * Tags describe; they are not tappable. For a set the user chooses from, use `DsFilterGroup`.
 * Past [max] the extra tags collapse into a single "+n" tag rather than being dropped, so a row
 * never claims a product has three labels when it has eight.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DsTagGroup(
    tags: List<DsTagEntry>,
    modifier: Modifier = Modifier,
    layout: DsTagLayout = DsTagLayout.Wrap,
    max: Int? = null,
    overflowLabel: (Int) -> String = { "+$it" },
    spacing: Dp = DsTheme.spacing.xs,
) {
    val (visible, hidden) = dsVisibleTags(tags, max)
    if (visible.isEmpty() && hidden == 0) return
    val items: @Composable () -> Unit = {
        visible.forEach { DsTag(it.text, tone = it.tone) }
        if (hidden > 0) DsTag(overflowLabel(hidden), tone = DsTagTone.Neutral)
    }
    when (layout) {
        DsTagLayout.Wrap -> {
            FlowRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) { items() }
        }

        DsTagLayout.Scroll -> {
            Row(
                modifier = modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) { items() }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsTagGroupPreview() =
    DsPreview {
        DsTagGroup(
            listOf(
                DsTagEntry("Sans gluten", DsTagTone.Success),
                DsTagEntry("Épicé", DsTagTone.Warning),
                DsTagEntry("Nouveau", DsTagTone.Primary),
                DsTagEntry("Fruits à coque", DsTagTone.Error),
                DsTagEntry("Végétarien"),
            ),
        )
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsTagGroup(
            listOf(
                DsTagEntry("Sans gluten", DsTagTone.Success),
                DsTagEntry("Épicé", DsTagTone.Warning),
                DsTagEntry("Nouveau", DsTagTone.Primary),
                DsTagEntry("Fruits à coque", DsTagTone.Error),
            ),
            layout = DsTagLayout.Scroll,
            max = 2,
        )
    }
