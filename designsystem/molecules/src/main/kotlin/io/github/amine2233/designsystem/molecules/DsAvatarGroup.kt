package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsAvatar
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

// Each avatar slides under the next by this share of its size.
private const val OVERLAP_FRACTION = 0.25f

/** One person in a [DsAvatarGroup]: the initials drawn, and the name read aloud when there is one. */
@Immutable
data class DsAvatarEntry(
    val initials: String,
    val name: String? = null,
)

/**
 * Splits [count] avatars into how many are drawn and how many collapse into the "+n" bubble.
 *
 * Kept separate from the composable so the rule — "show [maxVisible], count the rest" — can be
 * tested. [maxVisible] below 1 is treated as 1: a group always shows at least one face.
 */
fun dsVisibleAvatars(
    count: Int,
    maxVisible: Int,
): Pair<Int, Int> {
    if (count <= 0) return 0 to 0
    val visible = minOf(count, maxOf(maxVisible, 1))
    return visible to count - visible
}

/**
 * The default French description of a [DsAvatarGroup]: "A, B et C", or "A, B et 2 autres" when
 * some people are hidden behind the "+n" bubble.
 */
fun dsAvatarGroupDescription(
    names: List<String>,
    hidden: Int,
): String =
    when {
        hidden > 0 -> {
            val others = if (hidden == 1) "1 autre" else "$hidden autres"
            (names + others).joinWithEt()
        }

        else -> {
            names.joinWithEt()
        }
    }

private fun List<String>.joinWithEt(): String =
    when (size) {
        0 -> ""
        1 -> first()
        else -> dropLast(1).joinToString(", ") + " et " + last()
    }

/**
 * A stack of overlapping initials — who is on a table, who shares an order.
 *
 * Past [maxVisible] the rest collapse into one neutral "+n" bubble rather than disappearing, so the
 * group never claims three people when there are seven. Each item wears a ring in the page
 * background so the overlaps stay readable in light and dark.
 *
 * TalkBack reads the group once, as a single description built by [contentDescription] from the
 * visible names (initials when there is no name) and the hidden count. Not clickable.
 */
@Composable
fun DsAvatarGroup(
    avatars: List<DsAvatarEntry>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 3,
    size: Dp = 36.dp,
    overflowLabel: (Int) -> String = { "+$it" },
    contentDescription: (names: List<String>, hidden: Int) -> String = ::dsAvatarGroupDescription,
) {
    val (visibleCount, hidden) = dsVisibleAvatars(avatars.size, maxVisible)
    if (visibleCount == 0) return
    val visible = avatars.take(visibleCount)
    val description = contentDescription(visible.map { it.name ?: it.initials }, hidden)
    val ring = Modifier.border(DsTheme.spacing.xxs, DsTheme.colors.background, DsShapes.pill)
    Row(
        modifier = modifier.clearAndSetSemantics { this.contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(-size * OVERLAP_FRACTION),
    ) {
        visible.forEach { DsAvatar(it.initials, modifier = ring, size = size) }
        if (hidden > 0) {
            Box(
                modifier =
                    Modifier
                        .size(size)
                        .background(DsTheme.colors.surfaceMuted, DsShapes.pill)
                        .then(ring),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = overflowLabel(hidden),
                    style = DsTheme.typography.labelStrong,
                    color = DsTheme.colors.textSecondary,
                    maxLines = 1,
                    softWrap = false,
                )
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsAvatarGroupPreview() =
    DsPreview {
        val people =
            listOf(
                DsAvatarEntry("AK", "Amine Kacem"),
                DsAvatarEntry("B", "Bob"),
                DsAvatarEntry("CL", "Chloé"),
                DsAvatarEntry("DM", "David"),
                DsAvatarEntry("EV", "Eva"),
                DsAvatarEntry("FN", "Fanny"),
                DsAvatarEntry("GR", "Gaël"),
            )
        DsAvatarGroup(people.take(3))
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsAvatarGroup(people)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsAvatarGroup(people.take(1))
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsAvatarGroup(people, size = 48.dp)
    }
