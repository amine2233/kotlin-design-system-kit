package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled

/**
 * Identity-provider sign-in button ("Continuer avec …").
 *
 * The provider logo is a slot, not a DS token: brand marks belong to the provider and their usage
 * rules change, so the app supplies the asset while the design system guarantees the shape, the
 * height and — through the fixed logo slot — that labels line up down a stack of providers.
 */
@Composable
fun DsOAuthButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    logo: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().heightIn(min = 52.dp),
        enabled = enabled && !loading,
        shape = DsShapes.button,
        border = BorderStroke(1.dp, if (enabled) c.borderStrong else c.border),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = c.surface,
                contentColor = c.textPrimary,
                disabledContainerColor = c.surface,
                disabledContentColor = c.textDisabled,
            ),
        contentPadding =
            androidx.compose.foundation.layout
                .PaddingValues(horizontal = 20.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                if (loading) {
                    if (DsTheme.animationsEnabled) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = c.primary)
                    } else {
                        CircularProgressIndicator(
                            progress = { 0.75f },
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = c.primary,
                        )
                    }
                } else {
                    logo?.invoke()
                }
            }
            Row(
                Modifier.weight(1f).padding(end = 24.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                DsText(
                    text,
                    style = DsTheme.typography.labelStrong,
                    color = if (enabled) c.textPrimary else c.textDisabled,
                    maxLines = 1,
                )
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsOAuthButtonPreview() =
    DsPreview {
        DsOAuthButton("Continuer avec Google", {}, logo = {
            Icon(DsIcons.Account, contentDescription = null, tint = Color.Unspecified)
        })
        DsOAuthButton("Continuer avec Apple", {}, logo = { Icon(DsIcons.Store, contentDescription = null) })
        DsOAuthButton("Connexion en cours", {}, loading = true)
        DsOAuthButton("Indisponible", {}, enabled = false, logo = { Icon(DsIcons.Offline, contentDescription = null) })
    }
