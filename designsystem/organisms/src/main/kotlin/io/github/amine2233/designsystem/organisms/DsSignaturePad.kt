package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsStroke
import io.github.amine2233.designsystem.core.DsTheme

/** A pen position as fractions (`0f..1f`) of the pad's width and height, from its physical left and top edges. */
data class DsSignaturePoint(
    val x: Float,
    val y: Float,
)

/** One continuous pen-down-to-pen-up line. */
data class DsSignatureStroke(
    val points: List<DsSignaturePoint>,
)

object DsSignaturePadDefaults {
    /** Width : height of the drawing area. */
    const val ASPECT_RATIO = 3f
}

/** Converts a pixel position inside a [width] × [height] px pad into a [DsSignaturePoint], clamped to the pad. */
fun dsSignaturePoint(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
): DsSignaturePoint {
    require(width > 0) { "width must be positive: $width" }
    require(height > 0) { "height must be positive: $height" }
    return DsSignaturePoint((x / width).coerceIn(0f, 1f), (y / height).coerceIn(0f, 1f))
}

/** Returns a new list with a one-point stroke starting at [point] appended. */
fun List<DsSignatureStroke>.startStroke(point: DsSignaturePoint): List<DsSignatureStroke> = this + DsSignatureStroke(listOf(point))

/** Returns a new list with [point] appended to the last stroke; on an empty list, same as [startStroke]. */
fun List<DsSignatureStroke>.addPoint(point: DsSignaturePoint): List<DsSignatureStroke> {
    if (isEmpty()) return startStroke(point)
    return dropLast(1) + DsSignatureStroke(last().points + point)
}

/** True when at least one stroke has at least one point. */
val List<DsSignatureStroke>.isSigned: Boolean get() = any { it.points.isNotEmpty() }

/**
 * Signature capture: a drawing area above a Clear and a Confirm button.
 *
 * Stateless. The caller owns [strokes] and stores every list passed to [onStrokesChange]; Clear passes an
 * empty list. Strokes are stored as fractions of the area, so a saved signature renders at any size.
 */
@Composable
fun DsSignaturePad(
    strokes: List<DsSignatureStroke>,
    onStrokesChange: (List<DsSignatureStroke>) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hint: String = "Sign here",
    clearLabel: String = "Clear",
    confirmLabel: String = "Confirm",
    description: String = "Signature pad",
    emptyStateDescription: String = "Empty",
    signedStateDescription: String = "Signed",
) {
    val c = DsTheme.colors
    val spacing = DsTheme.spacing
    val signed = strokes.isSigned
    val latestStrokes by rememberUpdatedState(strokes)
    val latestOnStrokesChange by rememberUpdatedState(onStrokesChange)
    val input =
        if (enabled) {
            Modifier.pointerInput(enabled) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    down.consume()
                    val toPoint = { position: Offset ->
                        dsSignaturePoint(position.x, position.y, size.width.toFloat(), size.height.toFloat())
                    }
                    var current = latestStrokes.startStroke(toPoint(down.position))
                    latestOnStrokesChange(current)
                    while (true) {
                        val change = awaitPointerEvent().changes.firstOrNull { it.id == down.id } ?: break
                        if (!change.pressed) {
                            change.consume()
                            break
                        }
                        if (change.positionChanged()) {
                            current = current.addPoint(toPoint(change.position))
                            latestOnStrokesChange(current)
                        }
                        change.consume()
                    }
                }
            }
        } else {
            Modifier
        }
    val ink = if (enabled) c.textPrimary else c.textDisabled
    val baselineInset = spacing.md
    val baselineFromBottom = spacing.lg
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spacing.smd)) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(DsSignaturePadDefaults.ASPECT_RATIO)
                .clip(DsShapes.card)
                .background(c.surface, DsShapes.card)
                .border(DsStroke.hairline, c.border, DsShapes.card)
                .semantics {
                    contentDescription = description
                    stateDescription = if (signed) signedStateDescription else emptyStateDescription
                    if (!enabled) disabled()
                }.then(input),
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val hairline = DsStroke.hairline.toPx()
                val baselineY = size.height - baselineFromBottom.toPx()
                drawLine(
                    color = c.borderStrong,
                    start = Offset(baselineInset.toPx(), baselineY),
                    end = Offset(size.width - baselineInset.toPx(), baselineY),
                    strokeWidth = hairline,
                )
                val inkWidth = DsStroke.signature.toPx()
                strokes.forEach { stroke ->
                    val points = stroke.points.map { Offset(it.x * size.width, it.y * size.height) }
                    if (points.size == 1) {
                        drawCircle(color = ink, radius = inkWidth / 2f, center = points.first())
                    } else if (points.size > 1) {
                        val path =
                            Path().apply {
                                moveTo(points.first().x, points.first().y)
                                points.drop(1).forEach { lineTo(it.x, it.y) }
                            }
                        drawPath(path, ink, style = Stroke(width = inkWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
                    }
                }
            }
            if (!signed) {
                DsText(
                    hint,
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = baselineInset, bottom = baselineFromBottom + spacing.xs)
                        .clearAndSetSemantics {},
                    style = DsTheme.typography.body,
                    color = c.textSecondary,
                )
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DsButton(
                clearLabel,
                onClick = { onStrokesChange(emptyList()) },
                variant = DsButtonVariant.Outlined,
                enabled = enabled && signed,
            )
            DsButton(
                confirmLabel,
                onClick = onConfirm,
                variant = DsButtonVariant.Primary,
                enabled = enabled && signed,
            )
        }
    }
}

private val PreviewSignature =
    listOf(
        DsSignatureStroke(
            listOf(
                DsSignaturePoint(0.08f, 0.62f),
                DsSignaturePoint(0.14f, 0.3f),
                DsSignaturePoint(0.2f, 0.6f),
                DsSignaturePoint(0.26f, 0.32f),
                DsSignaturePoint(0.32f, 0.62f),
            ),
        ),
        DsSignatureStroke(
            listOf(
                DsSignaturePoint(0.38f, 0.58f),
                DsSignaturePoint(0.46f, 0.36f),
                DsSignaturePoint(0.54f, 0.6f),
                DsSignaturePoint(0.64f, 0.4f),
                DsSignaturePoint(0.74f, 0.58f),
            ),
        ),
        DsSignatureStroke(listOf(DsSignaturePoint(0.8f, 0.5f))),
    )

@DsComponentPreview
@Composable
private fun DsSignaturePadEmptyPreview() = DsPreview { DsSignaturePad(strokes = emptyList(), onStrokesChange = {}, onConfirm = {}) }

@DsComponentPreview
@Composable
private fun DsSignaturePadSignedPreview() = DsPreview { DsSignaturePad(strokes = PreviewSignature, onStrokesChange = {}, onConfirm = {}) }

@DsComponentPreview
@Composable
private fun DsSignaturePadDisabledPreview() =
    DsPreview { DsSignaturePad(strokes = PreviewSignature, onStrokesChange = {}, onConfirm = {}, enabled = false) }
