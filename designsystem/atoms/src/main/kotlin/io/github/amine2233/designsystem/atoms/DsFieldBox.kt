package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/**
 * The closed box of a field that opens something: value or placeholder, optional leading content,
 * trailing affordance.
 *
 * It carries no label and no supporting text — [DsPickerField] adds those. Components that need to
 * anchor a popup to the box itself (a select, a multi-select) use this directly inside their own
 * [androidx.compose.foundation.layout.Box] and pass [active] while the popup is open.
 */
@Composable
fun DsFieldBox(
    valueText: String?,
    placeholder: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    active: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = DsTheme.spacing.minTouchTarget)
                .clip(DsShapes.sm)
                .background(if (enabled) c.surfaceSubtle else c.surfaceMuted)
                .border(
                    width = if (active) 2.dp else 1.dp,
                    color =
                        if (isError) {
                            c.error
                        } else if (active) {
                            c.primary
                        } else {
                            c.border
                        },
                    shape = DsShapes.sm,
                ).clickable(enabled = enabled, onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
    ) {
        leadingContent?.invoke()
        Text(
            valueText ?: placeholder,
            style = DsTheme.typography.bodyStrong,
            color =
                if (!enabled) {
                    c.textDisabled
                } else if (valueText == null) {
                    c.textTertiary
                } else {
                    c.textPrimary
                },
            modifier = Modifier.weight(1f),
            maxLines = 1,
        )
        Icon(trailingIcon, contentDescription = null, tint = if (enabled) c.textSecondary else c.textDisabled)
    }
}

@DsComponentPreview
@Composable
private fun DsFieldBoxPreview() =
    DsPreview {
        DsFieldBox("20%", "Sélectionner", DsIcons.ChevronDown, {})
        DsFieldBox(null, "Sélectionner", DsIcons.ChevronDown, {}, active = true)
        DsFieldBox(null, "Sélectionner", DsIcons.ChevronDown, {}, isError = true)
        DsFieldBox("Indisponible", "Sélectionner", DsIcons.ChevronDown, {}, enabled = false)
    }
