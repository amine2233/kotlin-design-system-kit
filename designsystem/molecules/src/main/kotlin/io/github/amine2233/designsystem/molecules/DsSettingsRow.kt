package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsColorScheme
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

enum class DsSettingsTone { Neutral, Primary, Success, Warning, Info, Error }

sealed interface DsSettingsVariant {
    class Navigation(
        val onClick: () -> Unit,
    ) : DsSettingsVariant

    class Toggle(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit,
    ) : DsSettingsVariant

    class Value(
        val value: String,
        val onClick: (() -> Unit)? = null,
    ) : DsSettingsVariant

    class Destructive(
        val onClick: () -> Unit,
    ) : DsSettingsVariant
}

/** (tile background, glyph tint) for a tone. */
internal fun dsSettingsTileColors(
    tone: DsSettingsTone,
    colors: DsColorScheme,
): Pair<Color, Color> =
    when (tone) {
        DsSettingsTone.Neutral -> colors.surfaceSubtle to colors.textSecondary
        DsSettingsTone.Primary -> colors.primaryContainer to colors.primary
        DsSettingsTone.Success -> colors.successContainer to colors.success
        DsSettingsTone.Warning -> colors.warningContainer to colors.warning
        DsSettingsTone.Info -> colors.infoContainer to colors.info
        DsSettingsTone.Error -> colors.errorContainer to colors.error
    }

/** Flat settings row (one focus stop); group several in a DsCard separated by DsDivider. Destructive forces the error tone. */
@Composable
fun DsSettingsRow(
    title: String,
    variant: DsSettingsVariant,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    icon: ImageVector? = null,
    tone: DsSettingsTone = DsSettingsTone.Neutral,
    enabled: Boolean = true,
) {
    val c = DsTheme.colors
    val spacing = DsTheme.spacing
    val interaction =
        when (variant) {
            is DsSettingsVariant.Navigation -> {
                Modifier.clickable(enabled = enabled, role = Role.Button, onClick = variant.onClick)
            }

            is DsSettingsVariant.Toggle -> {
                Modifier.toggleable(
                    value = variant.checked,
                    enabled = enabled,
                    role = Role.Switch,
                    onValueChange = variant.onCheckedChange,
                )
            }

            is DsSettingsVariant.Value -> {
                if (variant.onClick != null) {
                    Modifier.clickable(enabled = enabled, role = Role.Button, onClick = variant.onClick)
                } else {
                    Modifier.semantics(mergeDescendants = true) {}
                }
            }

            is DsSettingsVariant.Destructive -> {
                Modifier.clickable(enabled = enabled, role = Role.Button, onClick = variant.onClick)
            }
        }
    val titleColor =
        when {
            !enabled -> c.textDisabled
            variant is DsSettingsVariant.Destructive -> c.error
            else -> c.textPrimary
        }
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = spacing.minTouchTarget)
                .then(interaction)
                .padding(horizontal = spacing.md, vertical = spacing.smd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        if (icon != null) {
            val tileTone = if (variant is DsSettingsVariant.Destructive) DsSettingsTone.Error else tone
            val (tileBackground, glyph) =
                if (enabled) dsSettingsTileColors(tileTone, c) else c.surfaceSubtle to c.textDisabled
            Box(
                modifier = Modifier.size(spacing.xl).background(tileBackground, DsShapes.sm),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = glyph, modifier = Modifier.size(spacing.mlg))
            }
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = DsTheme.typography.body, color = titleColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (supporting != null) {
                Text(
                    supporting,
                    style = DsTheme.typography.caption,
                    color = if (enabled) c.textTertiary else c.textDisabled,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        when (variant) {
            is DsSettingsVariant.Navigation -> {
                DsSettingsChevron(enabled)
            }

            is DsSettingsVariant.Toggle -> {
                Switch(
                    checked = variant.checked,
                    onCheckedChange = null,
                    enabled = enabled,
                    colors =
                        SwitchDefaults.colors(
                            checkedThumbColor = c.onPrimary,
                            checkedTrackColor = c.primary,
                            uncheckedThumbColor = c.surface,
                            uncheckedTrackColor = c.borderStrong,
                            uncheckedBorderColor = c.borderStrong,
                        ),
                )
            }

            is DsSettingsVariant.Value -> {
                Text(
                    variant.value,
                    style = DsTheme.typography.body,
                    color = if (enabled) c.textSecondary else c.textDisabled,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (variant.onClick != null) DsSettingsChevron(enabled)
            }

            is DsSettingsVariant.Destructive -> {
                Unit
            }
        }
    }
}

@Composable
private fun DsSettingsChevron(enabled: Boolean) {
    Icon(
        DsIcons.ChevronRight,
        contentDescription = null,
        tint = if (enabled) DsTheme.colors.textTertiary else DsTheme.colors.textDisabled,
        modifier = Modifier.size(DsTheme.spacing.lg),
    )
}

@DsComponentPreview
@Composable
private fun DsSettingsRowPreview() =
    DsPreview {
        DsSettingsRow("Compte", DsSettingsVariant.Navigation {}, icon = DsIcons.Account, tone = DsSettingsTone.Primary)
        DsDivider()
        DsSettingsRow(
            "Confidentialité",
            DsSettingsVariant.Navigation {},
            supporting = "Code PIN, verrouillage",
            icon = DsIcons.Lock,
            tone = DsSettingsTone.Info,
        )
        DsDivider()
        DsSettingsRow("Impression auto", DsSettingsVariant.Toggle(true) {}, icon = DsIcons.Print, tone = DsSettingsTone.Success)
        DsDivider()
        DsSettingsRow("Mode hors ligne", DsSettingsVariant.Toggle(false) {}, icon = DsIcons.Offline, tone = DsSettingsTone.Warning)
        DsDivider()
        DsSettingsRow("Langue", DsSettingsVariant.Value("Français") {}, icon = DsIcons.Settings)
        DsDivider()
        DsSettingsRow("Stockage", DsSettingsVariant.Value("42 Go"), icon = DsIcons.Info)
        DsDivider()
        DsSettingsRow("Supprimer le compte", DsSettingsVariant.Destructive {}, icon = DsIcons.Delete)
        DsDivider()
        DsSettingsRow("À propos", DsSettingsVariant.Navigation {})
        DsDivider()
        DsSettingsRow(
            "Synchronisation",
            DsSettingsVariant.Toggle(true) {},
            supporting = "Indisponible hors ligne",
            icon = DsIcons.Store,
            tone = DsSettingsTone.Primary,
            enabled = false,
        )
        DsDivider()
        DsSettingsRow(
            "Imprimante de cuisine principale du comptoir",
            DsSettingsVariant.Value("Epson TM-T20III") {},
            icon = DsIcons.Print,
        )
    }
