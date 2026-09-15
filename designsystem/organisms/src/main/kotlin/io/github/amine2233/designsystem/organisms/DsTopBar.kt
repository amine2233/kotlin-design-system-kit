package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsAvatar
import io.github.amine2233.designsystem.atoms.DsBadge
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** 52dp white top bar with bottom hairline. Slots: back, title (+ optional count badge), actions. */
@Composable
fun DsTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    badge: String? = null,
    titleContent: (@Composable RowScope.() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val c = DsTheme.colors
    Column(modifier.fillMaxWidth().background(c.surface)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (onBack != null) {
                DsIconButton(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", onClick = onBack)
            }
            Row(
                Modifier.weight(1f).padding(
                    start =
                        if (onBack ==
                            null
                        ) {
                            8.dp
                        } else {
                            0.dp
                        },
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(title, style = DsTheme.typography.title, color = c.textPrimary, maxLines = 1)
                if (badge != null) DsBadge(badge)
                titleContent?.invoke(this)
            }
            actions()
        }
        DsDivider()
    }
}

@DsComponentPreview
@Composable
private fun DsTopBarPreview() =
    DsPreview {
        DsTopBar("Panier", onBack = {
        }, badge = "5", actions = { DsButton("Vider", onClick = {}, variant = DsButtonVariant.Danger, size = DsButtonSize.Small) })
        Spacer(Modifier.height(8.dp))
        DsTopBar("Café Central", actions = { DsAvatar("AK") })
    }
