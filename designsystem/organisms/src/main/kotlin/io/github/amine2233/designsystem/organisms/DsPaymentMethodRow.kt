package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsCheckBullet
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsListItem

/** Payment method selector row: icon tile · title/subtitle · check when selected. */
@Composable
fun DsPaymentMethodRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = DsTheme.colors
    DsListItem(
        headline = title,
        supporting = subtitle,
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        leading = {
            Box(
                Modifier.size(44.dp).background(if (selected) c.primaryContainerBorder else c.surfaceSubtle, DsShapes.md),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = if (selected) c.primary else c.textSecondary, modifier = Modifier.size(22.dp))
            }
        },
        trailing = { if (selected) DsCheckBullet(size = 22.dp) },
    )
}
