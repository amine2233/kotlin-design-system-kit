package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsColorScheme
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme

enum class DsPresence { Online, Away, Busy, Offline }

internal fun dsPresenceColor(
    presence: DsPresence,
    colors: DsColorScheme,
): Color =
    when (presence) {
        DsPresence.Online -> colors.success
        DsPresence.Away -> colors.warning
        DsPresence.Busy -> colors.error
        DsPresence.Offline -> colors.textTertiary
    }

/** Presence indicator; `contentDescription = null` keeps it decorative. */
@Composable
fun DsStatusDot(
    presence: DsPresence,
    modifier: Modifier = Modifier,
    size: Dp = 10.dp,
    contentDescription: String? = null,
) {
    val description =
        if (contentDescription != null) {
            Modifier.semantics {
                this.contentDescription = contentDescription
                role = Role.Image
            }
        } else {
            Modifier
        }
    Box(
        modifier =
            modifier
                .then(description)
                .size(size)
                .background(dsPresenceColor(presence, DsTheme.colors), DsShapes.pill),
    )
}

@DsComponentPreview
@Composable
private fun DsStatusDotPreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsSpacing.sm)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(DsSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DsPresence.entries.forEach { DsStatusDot(it) }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(DsSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DsPresence.entries.forEach { DsStatusDot(it, size = DsSpacing.smd) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(DsSpacing.sm)) {
                DsAvatar("AK", status = DsPresence.Online)
                DsAvatar("ab", size = DsSpacing.xxl, status = DsPresence.Busy)
            }
        }
    }
