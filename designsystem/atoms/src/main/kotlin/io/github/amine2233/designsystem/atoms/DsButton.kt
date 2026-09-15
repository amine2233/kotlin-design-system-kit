package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled

enum class DsButtonVariant { Primary, Outlined, Ghost, Danger }

enum class DsButtonSize(
    val height: Dp,
    val horizontalPadding: Dp,
) {
    Small(36.dp, 12.dp),
    Medium(44.dp, 16.dp),
    Large(52.dp, 24.dp),
}

@Composable
fun DsButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: DsButtonVariant = DsButtonVariant.Primary,
    size: DsButtonSize = DsButtonSize.Large,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: ImageVector? = null,
) {
    val colors = DsTheme.colors
    val container: Color
    val content: Color
    val border: BorderStroke?
    when (variant) {
        DsButtonVariant.Primary -> {
            container = colors.primary
            content = colors.onPrimary
            border = null
        }

        DsButtonVariant.Outlined -> {
            container = Color.Transparent
            content = colors.primary
            border = BorderStroke(1.5.dp, colors.primary)
        }

        DsButtonVariant.Ghost -> {
            container = Color.Transparent
            content = colors.primary
            border = null
        }

        DsButtonVariant.Danger -> {
            container = Color.Transparent
            content = colors.error
            border = BorderStroke(1.dp, colors.error)
        }
    }
    Button(
        onClick = { if (!loading) onClick() },
        modifier =
            modifier
                .heightIn(min = size.height)
                .semantics { if (loading) stateDescription = "Chargement" },
        enabled = enabled,
        shape = DsShapes.button,
        border = border,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = container,
                contentColor = content,
                disabledContainerColor = if (variant == DsButtonVariant.Primary) colors.textDisabled else Color.Transparent,
                disabledContentColor = if (variant == DsButtonVariant.Primary) colors.surface else colors.textDisabled,
            ),
        contentPadding = PaddingValues(horizontal = size.horizontalPadding, vertical = 0.dp),
    ) {
        if (loading) {
            if (DsTheme.animationsEnabled) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = LocalContentColor.current, strokeWidth = 2.dp)
            } else {
                CircularProgressIndicator(
                    progress = { 0.75f },
                    modifier = Modifier.size(18.dp),
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp,
                )
            }
            Spacer(Modifier.width(8.dp))
        } else if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style =
                if (size ==
                    DsButtonSize.Large
                ) {
                    DsTheme.typography.title.copy(fontWeight = FontWeight.Bold)
                } else {
                    DsTheme.typography.bodyStrong
                },
            maxLines = 1,
        )
    }
}

@Composable
fun DsIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = DsTheme.colors.textPrimary,
    containerColor: Color = Color.Transparent,
    bordered: Boolean = false,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    IconButton(
        onClick = { if (!loading) onClick() },
        modifier =
            modifier
                .size(DsTheme.spacing.minTouchTarget)
                .then(
                    if (bordered) {
                        Modifier
                            .padding(4.dp)
                            .border(1.dp, DsTheme.colors.border, DsShapes.pill)
                    } else {
                        Modifier
                    },
                ),
        enabled = enabled,
        colors =
            IconButtonDefaults.iconButtonColors(
                containerColor = containerColor,
                contentColor = tint,
                disabledContentColor = DsTheme.colors.textDisabled,
            ),
    ) {
        when {
            loading && DsTheme.animationsEnabled -> {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = LocalContentColor.current, strokeWidth = 2.dp)
            }

            loading -> {
                CircularProgressIndicator(
                    progress = { 0.75f },
                    modifier = Modifier.size(20.dp),
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp,
                )
            }

            else -> {
                Icon(icon, contentDescription = contentDescription, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsButtonPreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DsButton("Se connecter", onClick = {}, modifier = Modifier.fillMaxWidth())
            DsButton("Créer un compte", onClick = {}, variant = DsButtonVariant.Outlined, modifier = Modifier.fillMaxWidth())
            DsButton("Paiement en cours", onClick = {}, loading = true, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsButton("Annuler", onClick = {}, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small)
                DsButton("Vider", onClick = {}, variant = DsButtonVariant.Danger, size = DsButtonSize.Small)
                DsButton("Payer", onClick = {}, size = DsButtonSize.Medium, leadingIcon = DsIcons.Card)
                DsButton("Off", onClick = {}, size = DsButtonSize.Small, enabled = false)
            }
        }
    }

@DsComponentPreview
@Composable
private fun DsIconButtonPreview() =
    DsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DsIconButton(DsIcons.Settings, contentDescription = null, onClick = {}, bordered = true)
            DsIconButton(DsIcons.Close, contentDescription = null, onClick = {}, tint = DsTheme.colors.primary)
            DsIconButton(DsIcons.Delete, contentDescription = null, onClick = {}, enabled = false)
            DsIconButton(DsIcons.Print, contentDescription = "Impression", onClick = {}, loading = true, bordered = true)
        }
    }
