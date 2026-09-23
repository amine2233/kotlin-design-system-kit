package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Search input: pill, leading magnifier, a clear button once there is something to clear.
 *
 * The clear button appears only when [value] is not empty — a permanent × invites a tap that does
 * nothing — and it clears through [onValueChange], so the caller has one place where the query
 * changes rather than two code paths to keep in step.
 *
 * [onSearch] switches the keyboard's action key to Search and fires on it; without it the field is
 * a live filter, which is what most POS lists want.
 */
@Composable
fun DsSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Rechercher...",
    enabled: Boolean = true,
    showClear: Boolean = true,
    clearContentDescription: String = "Effacer la recherche",
    onSearch: (() -> Unit)? = null,
    height: Dp = 36.dp,
    trailing: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(height)
                .background(if (enabled) c.surfaceSubtle else c.surfaceMuted, DsShapes.pill)
                .border(1.dp, c.border, DsShapes.pill)
                .padding(start = 12.dp, end = if (showClear && value.isNotEmpty() || trailing != null) 4.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(
            DsIcons.Search,
            contentDescription = null,
            tint = if (enabled) c.textTertiary else c.textDisabled,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.size(6.dp))
        Box(Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    placeholder,
                    style = DsTheme.typography.body,
                    color = if (enabled) c.textTertiary else c.textDisabled,
                    maxLines = 1,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = true,
                textStyle = DsTheme.typography.body.copy(color = if (enabled) c.textPrimary else c.textDisabled),
                cursorBrush = SolidColor(c.primary),
                keyboardOptions = KeyboardOptions(imeAction = if (onSearch != null) ImeAction.Search else ImeAction.Default),
                keyboardActions = KeyboardActions(onSearch = onSearch?.let { { it() } }),
            )
        }
        if (showClear && value.isNotEmpty()) {
            DsIconButton(
                DsIcons.Close,
                contentDescription = clearContentDescription,
                onClick = { onValueChange("") },
                tint = c.textSecondary,
                enabled = enabled,
                modifier = Modifier.size(height),
            )
        }
        trailing?.invoke()
    }
}

@DsComponentPreview
@Composable
private fun DsSearchBarPreview() =
    DsPreview {
        DsSearchBar("", {})
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsSearchBar("cappu", {})
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsSearchBar("crème", {}, height = DsTheme.spacing.minTouchTarget, trailing = {
            DsIconButton(DsIcons.Settings, contentDescription = "Filtres", onClick = {})
        })
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsSearchBar("", {}, enabled = false, placeholder = "Recherche indisponible")
    }
