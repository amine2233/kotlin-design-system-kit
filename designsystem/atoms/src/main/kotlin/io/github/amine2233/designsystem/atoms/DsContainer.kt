package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsAlpha
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** What a [DsContainer] is saying: the tone its border and background are drawn from. */
enum class DsContainerStyle { Neutral, Info, Success, Warning, Danger, Primary }

/**
 * The tone's full-strength color — for the text, icons or links the caller puts inside, so they
 * match the frame around them.
 */
@Composable
@ReadOnlyComposable
fun DsContainerStyle.accent(): Color {
    val c = DsTheme.colors
    return when (this) {
        DsContainerStyle.Neutral -> c.textSecondary
        DsContainerStyle.Info -> c.info
        DsContainerStyle.Success -> c.success
        DsContainerStyle.Warning -> c.warning
        DsContainerStyle.Danger -> c.error
        DsContainerStyle.Primary -> c.primary
    }
}

/**
 * Bordered box holding a message or anything else, tinted by [style].
 *
 * Background and border are the same tone at two opacities rather than two opaque colors, so the
 * container sits correctly on whatever is behind it — surface, card, a tinted row — in both themes,
 * and a new tone needs one color instead of a light/dark pair. Content keeps its own colors;
 * [DsContainerStyle.accent] gives the caller the tone when text should match the frame.
 *
 * For a message that needs an icon, a title and a dismiss action, use `DsBanner` (molecules) — it
 * owns that layout and draws its tint from the opaque `*Container` tokens instead.
 */
@Composable
fun DsContainer(
    modifier: Modifier = Modifier,
    style: DsContainerStyle = DsContainerStyle.Neutral,
    shape: Shape = DsShapes.md,
    contentPadding: Dp = DsTheme.spacing.smd,
    borderWidth: Dp = 1.dp,
    backgroundAlpha: Float = DsAlpha.TINT,
    borderAlpha: Float = DsAlpha.BORDER,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(DsTheme.spacing.xs),
    content: @Composable ColumnScope.() -> Unit,
) {
    val accent = style.accent()
    Column(
        modifier =
            modifier
                .background(accent.copy(alpha = backgroundAlpha), shape)
                .border(borderWidth, accent.copy(alpha = borderAlpha), shape)
                .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        content = content,
    )
}

@DsComponentPreview
@Composable
private fun DsContainerPreview() =
    DsPreview {
        DsContainer(Modifier.fillMaxWidth(), style = DsContainerStyle.Info) {
            DsText("Terminal connecté", style = DsTheme.typography.bodyStrong, color = DsContainerStyle.Info.accent())
            DsText("Le paiement par carte est disponible.", style = DsTheme.typography.caption)
        }
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsContainer(Modifier.fillMaxWidth(), style = DsContainerStyle.Danger) {
            DsText("Paiement refusé", style = DsTheme.typography.bodyStrong, color = DsContainerStyle.Danger.accent())
        }
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsContainer(Modifier.fillMaxWidth(), style = DsContainerStyle.Warning) {
            DsText("Stock bas — 3 articles", style = DsTheme.typography.body)
        }
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsContainer(Modifier.fillMaxWidth()) {
            DsText("Note interne visible par l'équipe", style = DsTheme.typography.body)
        }
    }
