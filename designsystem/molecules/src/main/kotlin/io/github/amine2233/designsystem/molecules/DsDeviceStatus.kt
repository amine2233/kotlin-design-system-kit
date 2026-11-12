package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import io.github.amine2233.designsystem.atoms.DsSpinner
import io.github.amine2233.designsystem.atoms.DsSpinnerSize
import io.github.amine2233.designsystem.atoms.DsSpinnerStyle
import io.github.amine2233.designsystem.atoms.DsTagTone
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled

/** What the caller knows about a peripheral; the kit never detects nor polls it. */
enum class DsDeviceStatus { Connected, Connecting, Offline, Attention }

/** The [DsTagTone] a status is drawn in, so a device chip and a [io.github.amine2233.designsystem.atoms.DsTag] of the same state match. */
fun dsDeviceStatusTone(status: DsDeviceStatus): DsTagTone =
    when (status) {
        DsDeviceStatus.Connected -> DsTagTone.Success
        DsDeviceStatus.Connecting -> DsTagTone.Info
        DsDeviceStatus.Offline -> DsTagTone.Neutral
        DsDeviceStatus.Attention -> DsTagTone.Error
    }

/** One peripheral in a [DsDeviceStatusRow]. */
@Immutable
data class DsDeviceStatusEntry(
    val icon: ImageVector,
    val label: String,
    val status: DsDeviceStatus,
    val statusText: String,
    val onClick: (() -> Unit)? = null,
)

/**
 * A compact pill telling the cashier how one peripheral is doing: printer, card terminal, cash drawer, network.
 *
 * The caller supplies the icon, the label and the visible [statusText]; each status also has its own glyph
 * (a spinner while connecting), so colour is never the only signal. TalkBack reads one item, "label, status".
 * With [onClick] the chip becomes a button with a [DsTheme.spacing] `minTouchTarget` touch area; without it,
 * it announces no action. The label is what ellipsizes when space runs out.
 */
@Composable
fun DsDeviceStatusChip(
    icon: ImageVector,
    label: String,
    status: DsDeviceStatus,
    statusText: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    animate: Boolean = DsTheme.animationsEnabled,
) {
    val description = "$label, $statusText"
    if (onClick == null) {
        DeviceStatusPill(icon, label, status, statusText, animate, modifier.clearAndSetSemantics { contentDescription = description })
        return
    }
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier =
            modifier
                .sizeIn(minWidth = DsTheme.spacing.minTouchTarget, minHeight = DsTheme.spacing.minTouchTarget)
                .clickable(interactionSource = interactionSource, indication = null, role = Role.Button, onClick = onClick)
                .clearAndSetSemantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        DeviceStatusPill(
            icon,
            label,
            status,
            statusText,
            animate,
            Modifier.clip(DsShapes.pill).indication(interactionSource, LocalIndication.current),
        )
    }
}

@Composable
private fun DeviceStatusPill(
    icon: ImageVector,
    label: String,
    status: DsDeviceStatus,
    statusText: String,
    animate: Boolean,
    modifier: Modifier,
) {
    val (background, content) = toneColors(dsDeviceStatusTone(status))
    Row(
        modifier =
            modifier
                .background(background, DsShapes.pill)
                .padding(horizontal = DsTheme.spacing.sm, vertical = DsTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(DsTheme.spacing.md))
        DsText(
            label,
            modifier = Modifier.weight(1f, fill = false),
            style = DsTheme.typography.labelStrong,
            color = content,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        when (status) {
            DsDeviceStatus.Connecting -> {
                Box(Modifier.size(DsTheme.spacing.md), contentAlignment = Alignment.Center) {
                    DsSpinner(size = DsSpinnerSize.Tiny, style = DsSpinnerStyle.Info, contentDescription = statusText, animate = animate)
                }
            }

            DsDeviceStatus.Connected -> {
                StatusGlyph(DsIcons.Success, content)
            }

            DsDeviceStatus.Offline -> {
                StatusGlyph(DsIcons.Offline, content)
            }

            DsDeviceStatus.Attention -> {
                StatusGlyph(DsIcons.Error, content)
            }
        }
        DsText(
            statusText,
            style = DsTheme.typography.caption,
            color = content,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun StatusGlyph(
    icon: ImageVector,
    tint: Color,
) = Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(DsTheme.spacing.md))

@Composable
private fun toneColors(tone: DsTagTone): Pair<Color, Color> {
    val c = DsTheme.colors
    return when (tone) {
        DsTagTone.Neutral -> c.surfaceMuted to c.textSecondary
        DsTagTone.Primary -> c.primaryContainer to c.primary
        DsTagTone.Success -> c.successContainer to c.success
        DsTagTone.Warning -> c.warningContainer to c.warning
        DsTagTone.Error -> c.errorContainer to c.error
        DsTagTone.Info -> c.infoContainer to c.info
    }
}

/**
 * The peripherals of a till, one [DsDeviceStatusChip] each, wrapping onto as many lines as they need.
 *
 * [animate] reaches every chip, so one switch stills all the connecting spinners. An empty list draws nothing.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DsDeviceStatusRow(
    devices: List<DsDeviceStatusEntry>,
    modifier: Modifier = Modifier,
    spacing: Dp = DsTheme.spacing.xs,
    animate: Boolean = DsTheme.animationsEnabled,
) {
    if (devices.isEmpty()) return
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        devices.forEach {
            DsDeviceStatusChip(it.icon, it.label, it.status, it.statusText, onClick = it.onClick, animate = animate)
        }
    }
}

@DsComponentPreview
@Composable
private fun DsDeviceStatusPreview() =
    DsPreview {
        val devices =
            listOf(
                DsDeviceStatusEntry(DsIcons.Print, "Imprimante", DsDeviceStatus.Connected, "Connectée"),
                DsDeviceStatusEntry(DsIcons.Card, "Terminal", DsDeviceStatus.Connecting, "Connexion…"),
                DsDeviceStatusEntry(DsIcons.CashDrawer, "Tiroir-caisse", DsDeviceStatus.Offline, "Hors ligne"),
                DsDeviceStatusEntry(DsIcons.Network, "Réseau", DsDeviceStatus.Attention, "Instable"),
            )
        Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
            devices.forEach { DsDeviceStatusChip(it.icon, it.label, it.status, it.statusText) }
            DsDeviceStatusChip(DsIcons.Print, "Imprimante", DsDeviceStatus.Attention, "Papier épuisé", onClick = {})
            DsDeviceStatusChip(
                DsIcons.Card,
                "Terminal de paiement du comptoir principal",
                DsDeviceStatus.Connected,
                "Connecté",
                modifier = Modifier.width(DsTheme.spacing.xxxl * 3),
            )
            DsDeviceStatusRow(devices)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                DsDeviceStatusRow(devices)
            }
        }
    }
