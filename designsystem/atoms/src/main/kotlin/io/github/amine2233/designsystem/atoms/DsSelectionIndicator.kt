package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Radio-style circle: teal filled dot when selected, grey ring otherwise. */
@Composable
fun DsRadioIndicator(selected: Boolean, modifier: Modifier = Modifier, size: Dp = 20.dp) {
    val c = DsTheme.colors
    Box(
        modifier = modifier
            .size(size)
            .then(
                if (selected) Modifier.background(c.primary, DsShapes.pill)
                else Modifier.border(2.dp, c.borderStrong, DsShapes.pill),
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) Box(Modifier.size(size * 0.4f).background(c.onPrimary, DsShapes.pill))
    }
}

/** Teal check bullet used in value-prop lists and done steps. */
@Composable
fun DsCheckBullet(modifier: Modifier = Modifier, size: Dp = 20.dp, filled: Boolean = true) {
    val c = DsTheme.colors
    Box(
        modifier = modifier
            .size(size)
            .background(if (filled) c.primary else c.primaryContainer, DsShapes.pill),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = if (filled) c.onPrimary else c.primary,
            modifier = Modifier.size(size * 0.6f),
        )
    }
}

@DsComponentPreview
@Composable
private fun DsSelectionIndicatorPreview() = DsPreview {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { DsRadioIndicator(true); DsRadioIndicator(false); DsCheckBullet(); DsCheckBullet(filled = false) }
}
