package io.github.amine2233.designsystem.molecules

import androidx.compose.runtime.Immutable

/** How a discount value is read: a share of the subtotal, or a fixed amount taken off it. */
enum class DsDiscountMode { Percent, FixedAmount }

/** The [discount] taken off a subtotal and the [total] left to pay, both in minor units. */
@Immutable
data class DsDiscountResult(
    val discount: Long,
    val total: Long,
)

private const val FULL_BASIS_POINTS = 10_000L

/**
 * The discount on [subtotal] (minor units) in plain `Long` maths, so no amount is ever rounded through
 * a floating point.
 *
 * [value] is basis points in [DsDiscountMode.Percent] (`1000` = 10,00 %) and minor units in
 * [DsDiscountMode.FixedAmount]. Percent results round half up to the nearest minor unit. Negative
 * inputs count as 0 and the discount is clamped to the subtotal, so the total is never negative.
 * Splitting the subtotal around 10 000 keeps every product in range for any `Long` subtotal.
 */
fun dsDiscount(
    subtotal: Long,
    mode: DsDiscountMode,
    value: Long,
): DsDiscountResult {
    val base = subtotal.coerceAtLeast(0)
    val discount =
        when (mode) {
            DsDiscountMode.Percent -> {
                val bp = value.coerceIn(0, FULL_BASIS_POINTS)
                (base / FULL_BASIS_POINTS) * bp + ((base % FULL_BASIS_POINTS) * bp + FULL_BASIS_POINTS / 2) / FULL_BASIS_POINTS
            }

            DsDiscountMode.FixedAmount -> {
                value.coerceIn(0, base)
            }
        }
    return DsDiscountResult(discount = discount, total = base - discount)
}
