package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis

@Immutable
data class DsReceiptItem(
    val quantity: String,
    val name: String,
    val total: String,
    val detail: String? = null,
    val originalTotal: String? = null,
)

/**
 * Read-only receipt: merchant header, items, totals, payment, timestamp/reference, then the receipt actions.
 *
 * Every value is a preformatted string; nothing is computed. A null callback hides its action, and with no
 * callbacks the receipt is read-only. It does not scroll by itself.
 */
@Composable
fun DsReceipt(
    merchantName: String,
    items: List<DsReceiptItem>,
    totals: List<DsTotalsLine>,
    total: DsTotalsLine,
    paymentIcon: ImageVector,
    paymentLabel: String,
    timestamp: String,
    reference: String,
    modifier: Modifier = Modifier,
    merchantDetails: List<String> = emptyList(),
    onPrint: (() -> Unit)? = null,
    onEmail: (() -> Unit)? = null,
    onSms: (() -> Unit)? = null,
    onNoReceipt: (() -> Unit)? = null,
    printLabel: String = "Print",
    emailLabel: String = "Email",
    smsLabel: String = "SMS",
    noReceiptLabel: String = "No receipt",
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val spacing = DsTheme.spacing
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spacing.smd)) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(merchantName, style = t.titleLarge, color = c.textPrimary, textAlign = TextAlign.Center)
            merchantDetails.forEach { Text(it, style = t.caption, color = c.textSecondary, textAlign = TextAlign.Center) }
        }
        DsDivider()
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            items.forEach { ReceiptItemRow(it) }
        }
        DsDivider()
        DsTotalsBlock(lines = totals, total = total, rowModifier = Modifier.semantics(mergeDescendants = true) {})
        Row(
            modifier = Modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = paymentLabel },
            horizontalArrangement = Arrangement.spacedBy(spacing.smd),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.background(c.surfaceSubtle, DsShapes.md).padding(spacing.sm), contentAlignment = Alignment.Center) {
                Icon(paymentIcon, contentDescription = null, tint = c.textSecondary)
            }
            Text(paymentLabel, style = t.bodyStrong, color = c.textPrimary)
        }
        Column {
            Text(timestamp, style = t.caption, color = c.textSecondary)
            Text(reference, style = t.caption, color = c.textSecondary)
        }
        if (onPrint != null || onEmail != null || onSms != null || onNoReceipt != null) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                if (onPrint != null) ReceiptAction(printLabel, DsIcons.Print, onPrint)
                if (onEmail != null) ReceiptAction(emailLabel, DsIcons.Email, onEmail, DsButtonVariant.Outlined)
                if (onSms != null) ReceiptAction(smsLabel, DsIcons.Sms, onSms, DsButtonVariant.Outlined)
                if (onNoReceipt != null) ReceiptAction(noReceiptLabel, null, onNoReceipt, DsButtonVariant.Ghost)
            }
        }
    }
}

@Composable
private fun ReceiptItemRow(item: DsReceiptItem) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val description =
        listOfNotNull(item.quantity, item.name, item.detail, item.originalTotal, item.total).joinToString(" ")
    Row(
        modifier = Modifier.fillMaxWidth().clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
        verticalAlignment = Alignment.Top,
    ) {
        Text(item.quantity, style = t.label, color = c.textSecondary)
        Column(Modifier.weight(1f)) {
            Text(item.name, style = t.body, color = c.textPrimary)
            if (item.detail != null) Text(item.detail, style = t.caption, color = c.textSecondary)
        }
        Column(horizontalAlignment = Alignment.End) {
            if (item.originalTotal != null) {
                Text(
                    item.originalTotal,
                    style = t.caption.copy(textDecoration = TextDecoration.LineThrough),
                    color = c.warning,
                )
            }
            Text(item.total, style = t.bodyStrong, color = c.textPrimary)
        }
    }
}

@Composable
private fun ReceiptAction(
    label: String,
    icon: ImageVector?,
    onClick: () -> Unit,
    variant: DsButtonVariant = DsButtonVariant.Primary,
) = DsButton(
    label,
    onClick,
    Modifier.fillMaxWidth(),
    variant = variant,
    size = DsButtonSize.Large,
    leadingIcon = icon,
)

private val PreviewTotals =
    listOf(
        DsTotalsLine("Subtotal", "1 025 DA"),
        DsTotalsLine("Discount", "−49 DA", DsTotalsEmphasis.Discount),
        DsTotalsLine("VAT 10%", "92 DA", DsTotalsEmphasis.Muted),
        DsTotalsLine("VAT 5.5%", "4 DA", DsTotalsEmphasis.Muted),
        DsTotalsLine("Tip", "+ 108 DA", DsTotalsEmphasis.Positive),
    )

@DsComponentPreview
@Composable
private fun DsReceiptFullPreview() =
    DsPreview {
        DsReceipt(
            merchantName = "Café Central",
            merchantDetails = listOf("12 rue Didouche Mourad, Alger", "NIF 000123456789"),
            items =
                listOf(
                    DsReceiptItem("2×", "Cappuccino", "400 DA", detail = "Oat milk"),
                    DsReceiptItem("1×", "Lunch menu", "441 DA", originalTotal = "490 DA"),
                    DsReceiptItem("1×", "Seasonal pistachio and orange blossom layered cake slice", "292 DA"),
                ),
            totals = PreviewTotals,
            total = DsTotalsLine("Total", "1 180 DA"),
            paymentIcon = DsIcons.Card,
            paymentLabel = "Card •••• 4242",
            timestamp = "12 Nov 2026, 14:32",
            reference = "Ref. #A-10423",
            onPrint = {},
            onEmail = {},
            onSms = {},
            onNoReceipt = {},
        )
    }

@DsComponentPreview
@Composable
private fun DsReceiptMinimalPreview() =
    DsPreview {
        DsReceipt(
            merchantName = "Café Central",
            items = listOf(DsReceiptItem("1×", "Espresso", "150 DA")),
            totals = listOf(DsTotalsLine("Subtotal", "136 DA"), DsTotalsLine("VAT 10%", "14 DA", DsTotalsEmphasis.Muted)),
            total = DsTotalsLine("Total", "150 DA"),
            paymentIcon = DsIcons.Cash,
            paymentLabel = "Cash",
            timestamp = "12 Nov 2026, 14:32",
            reference = "Ref. #A-10424",
            onPrint = {},
            onEmail = {},
            onSms = {},
            onNoReceipt = {},
        )
    }

@DsComponentPreview
@Composable
private fun DsReceiptNoActionsPreview() =
    DsPreview {
        DsReceipt(
            merchantName = "Café Central",
            merchantDetails = listOf("12 rue Didouche Mourad, Alger"),
            items = listOf(DsReceiptItem("2×", "Cappuccino", "400 DA"), DsReceiptItem("1×", "Croissant", "120 DA")),
            totals = listOf(DsTotalsLine("Subtotal", "473 DA"), DsTotalsLine("VAT 10%", "47 DA", DsTotalsEmphasis.Muted)),
            total = DsTotalsLine("Total", "520 DA"),
            paymentIcon = DsIcons.Contactless,
            paymentLabel = "Contactless",
            timestamp = "12 Nov 2026, 14:32",
            reference = "Ref. #A-10425",
        )
    }
