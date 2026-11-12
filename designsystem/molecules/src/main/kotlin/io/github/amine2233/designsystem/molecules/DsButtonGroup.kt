package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * One button in a [DsButtonGroup]: what it says, what it does and how it looks.
 *
 * Everything a `DsButton` takes except the size, which the group sets once so a pair never mixes
 * a tall button with a short one.
 */
@Immutable
data class DsButtonGroupItem(
    val text: String,
    val onClick: () -> Unit,
    val variant: DsButtonVariant = DsButtonVariant.Primary,
    val enabled: Boolean = true,
    val loading: Boolean = false,
    val leadingIcon: ImageVector? = null,
)

/**
 * How a [DsButtonGroup] lays its buttons out.
 *
 * [Horizontal] puts them side by side and shares the width equally — "Annuler / Valider" at the
 * foot of a form, where neither action should look bigger than the other. [Vertical] stacks
 * full-width buttons, primary action first — a narrow screen, or labels too long to sit side by side.
 *
 * The group never switches axis by itself: the screen picks one and keeps it.
 */
enum class DsButtonGroupOrientation { Horizontal, Vertical }

/**
 * Two or three related actions laid out together with one gap and one size.
 *
 * Each button keeps its own variant, enabled, loading and icon. The group always fills the width,
 * so a single button is full width in either orientation. Buttons follow list order along the
 * layout direction, so a right-to-left locale mirrors a horizontal group.
 *
 * This is a set of actions, not a choice: for a pick-one control, use `DsSegmentedControl`.
 */
@Composable
fun DsButtonGroup(
    buttons: List<DsButtonGroupItem>,
    modifier: Modifier = Modifier,
    orientation: DsButtonGroupOrientation = DsButtonGroupOrientation.Horizontal,
    size: DsButtonSize = DsButtonSize.Large,
    spacing: Dp = DsTheme.spacing.sm,
) {
    if (buttons.isEmpty()) return
    val button: @Composable (DsButtonGroupItem, Modifier) -> Unit = { item, itemModifier ->
        DsButton(
            text = item.text,
            onClick = item.onClick,
            modifier = itemModifier,
            variant = item.variant,
            size = size,
            enabled = item.enabled,
            loading = item.loading,
            leadingIcon = item.leadingIcon,
        )
    }
    when (orientation) {
        DsButtonGroupOrientation.Horizontal -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) {
                buttons.forEach { button(it, Modifier.weight(1f)) }
            }
        }

        DsButtonGroupOrientation.Vertical -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) {
                buttons.forEach { button(it, Modifier.fillMaxWidth()) }
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsButtonGroupPreview() =
    DsPreview {
        DsButtonGroup(
            listOf(
                DsButtonGroupItem("Annuler", {}, variant = DsButtonVariant.Ghost),
                DsButtonGroupItem("Valider", {}),
            ),
        )
        Spacer(Modifier.height(DsTheme.spacing.md))
        DsButtonGroup(
            listOf(
                DsButtonGroupItem("Supprimer", {}, variant = DsButtonVariant.Danger),
                DsButtonGroupItem("Brouillon", {}, variant = DsButtonVariant.Outlined, enabled = false),
                DsButtonGroupItem("Publier", {}),
            ),
            size = DsButtonSize.Medium,
        )
        Spacer(Modifier.height(DsTheme.spacing.md))
        DsButtonGroup(
            listOf(
                DsButtonGroupItem("Imprimer le ticket", {}, leadingIcon = DsIcons.Print),
                DsButtonGroupItem("Fermer", {}, variant = DsButtonVariant.Outlined),
            ),
            orientation = DsButtonGroupOrientation.Vertical,
        )
        Spacer(Modifier.height(DsTheme.spacing.md))
        DsButtonGroup(
            listOf(
                DsButtonGroupItem("Payer", {}, loading = true),
                DsButtonGroupItem("Mettre en attente", {}, variant = DsButtonVariant.Outlined),
                DsButtonGroupItem("Annuler", {}, variant = DsButtonVariant.Ghost),
            ),
            orientation = DsButtonGroupOrientation.Vertical,
        )
    }
