package io.github.amine2233.designsystem.organisms

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsMotion
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

enum class DsPaymentOutcome { Success, Failure }

/**
 * Animated result mark: disc pops in, then the check (success) or cross (failure) is stroked.
 * [progress] null = play the animation; a fixed value renders that frame (previews / screenshot tests).
 */
@Composable
fun DsResultMark(
    outcome: DsPaymentOutcome,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    progress: Float? = null,
    onFinished: () -> Unit = {},
) {
    val c = DsTheme.colors
    val color = if (outcome == DsPaymentOutcome.Success) c.success else c.error
    val disc = remember { Animatable(progress ?: 0f) }
    val mark = remember { Animatable(progress ?: 0f) }
    LaunchedEffect(outcome, progress) {
        if (progress != null) return@LaunchedEffect
        disc.snapTo(0f); mark.snapTo(0f)
        disc.animateTo(1f, tween(DsMotion.normal, easing = DsMotion.emphasizedEasing))
        mark.animateTo(1f, tween(DsMotion.slow, easing = DsMotion.standardEasing))
        onFinished()
    }
    val discValue = progress?.coerceIn(0f, 1f) ?: disc.value
    val markValue = progress?.coerceIn(0f, 1f) ?: mark.value
    Canvas(modifier.size(size)) {
        val r = this.size.minDimension / 2f
        val center = Offset(r, r)
        val overshoot = 1f + 0.12f * kotlin.math.sin(discValue * Math.PI).toFloat()
        drawCircle(color = color, radius = r * discValue * overshoot, center = center)
        val stroke = Stroke(width = r * 0.14f, cap = StrokeCap.Round)
        if (outcome == DsPaymentOutcome.Success) {
            // check: two segments, drawn in sequence
            val a = Offset(r * 0.58f, r * 1.02f)
            val b = Offset(r * 0.88f, r * 1.32f)
            val d = Offset(r * 1.46f, r * 0.72f)
            val first = (markValue * 2f).coerceIn(0f, 1f)
            val second = (markValue * 2f - 1f).coerceIn(0f, 1f)
            if (first > 0f) drawLine(Color.White, a, lerp(a, b, first), stroke.width, StrokeCap.Round)
            if (second > 0f) drawLine(Color.White, b, lerp(b, d, second), stroke.width, StrokeCap.Round)
        } else {
            val k = r * 0.38f
            val first = (markValue * 2f).coerceIn(0f, 1f)
            val second = (markValue * 2f - 1f).coerceIn(0f, 1f)
            val tl = Offset(r - k, r - k); val br = Offset(r + k, r + k)
            val tr = Offset(r + k, r - k); val bl = Offset(r - k, r + k)
            if (first > 0f) drawLine(Color.White, tl, lerp(tl, br, first), stroke.width, StrokeCap.Round)
            if (second > 0f) drawLine(Color.White, tr, lerp(tr, bl, second), stroke.width, StrokeCap.Round)
        }
    }
}

private fun lerp(a: Offset, b: Offset, t: Float) = Offset(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t)

/** Full result panel: animated mark, amount, message and actions ("Nouvelle vente" / "Réessayer"). */
@Composable
fun DsPaymentResult(
    outcome: DsPaymentOutcome,
    title: String,
    modifier: Modifier = Modifier,
    amount: String? = null,
    description: String? = null,
    primaryAction: String? = null,
    onPrimary: () -> Unit = {},
    secondaryAction: String? = null,
    onSecondary: () -> Unit = {},
    progress: Float? = null,
) {
    val c = DsTheme.colors
    Column(modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        DsResultMark(outcome, progress = progress)
        Spacer(Modifier.height(20.dp))
        if (amount != null) Text(amount, style = DsTheme.typography.displayMedium, color = c.textPrimary)
        Text(title, style = DsTheme.typography.titleLarge, color = if (outcome == DsPaymentOutcome.Success) c.success else c.error, textAlign = TextAlign.Center)
        if (description != null) {
            Spacer(Modifier.height(6.dp))
            Text(description, style = DsTheme.typography.body, color = c.textSecondary, textAlign = TextAlign.Center)
        }
        if (primaryAction != null) {
            Spacer(Modifier.height(24.dp))
            DsButton(primaryAction, onClick = onPrimary, modifier = Modifier.fillMaxWidth())
        }
        if (secondaryAction != null) {
            Spacer(Modifier.height(8.dp))
            DsButton(secondaryAction, onClick = onSecondary, variant = DsButtonVariant.Ghost, modifier = Modifier.fillMaxWidth())
        }
    }
}

@DsComponentPreview
@Composable
private fun DsResultMarkPreview() = DsPreview {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        DsResultMark(DsPaymentOutcome.Success, size = 72.dp, progress = 1f)
        DsResultMark(DsPaymentOutcome.Failure, size = 72.dp, progress = 1f)
        DsResultMark(DsPaymentOutcome.Success, size = 72.dp, progress = 0.6f)
    }
}

@DsComponentPreview
@Composable
private fun DsPaymentResultSuccessPreview() = DsPreview {
    DsPaymentResult(DsPaymentOutcome.Success, "Paiement accepté", amount = "1 188 DA", description = "Ticket n° 0421", primaryAction = "Nouvelle vente", secondaryAction = "Imprimer", progress = 1f)
}

@DsComponentPreview
@Composable
private fun DsPaymentResultFailurePreview() = DsPreview {
    DsPaymentResult(DsPaymentOutcome.Failure, "Paiement refusé", amount = "1 188 DA", description = "Carte refusée par la banque.", primaryAction = "Réessayer", progress = 1f)
}
