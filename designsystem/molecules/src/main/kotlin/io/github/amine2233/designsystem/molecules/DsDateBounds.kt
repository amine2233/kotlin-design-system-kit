package io.github.amine2233.designsystem.molecules

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Immutable
import java.time.Instant
import java.time.ZoneOffset

/**
 * Which days a calendar accepts: a closing report refuses the future, a delivery slot refuses the
 * past, a shop closed on Sunday refuses that weekday.
 *
 * A design-system type rather than Material's `SelectableDates` so callers never touch an
 * experimental Material API — and so the rule stays a plain value that can be unit-tested.
 * Bounds are UTC epoch millis, inclusive, like every other date in the system.
 */
@Immutable
data class DsDateBounds(
    val minUtcMillis: Long? = null,
    val maxUtcMillis: Long? = null,
    val isDayAllowed: (Long) -> Boolean = { true },
) {
    companion object {
        /** Nothing before [utcMillis] — a delivery date, a next appointment. */
        fun from(utcMillis: Long): DsDateBounds = DsDateBounds(minUtcMillis = utcMillis)

        /** Nothing after [utcMillis] — a closing report, a birth date. */
        fun upTo(utcMillis: Long): DsDateBounds = DsDateBounds(maxUtcMillis = utcMillis)

        fun between(
            startUtcMillis: Long,
            endUtcMillis: Long,
        ): DsDateBounds = DsDateBounds(minUtcMillis = startUtcMillis, maxUtcMillis = endUtcMillis)
    }
}

/** Whether [utcMillis] passes the bounds and the caller's own day rule. */
fun DsDateBounds.allows(utcMillis: Long): Boolean =
    (minUtcMillis == null || utcMillis >= minUtcMillis) &&
        (maxUtcMillis == null || utcMillis <= maxUtcMillis) &&
        isDayAllowed(utcMillis)

private fun yearOf(utcMillis: Long): Int = Instant.ofEpochMilli(utcMillis).atZone(ZoneOffset.UTC).year

/** Years the picker's year grid may offer — outside the bounds there is nothing to pick. */
fun DsDateBounds.allowsYear(year: Int): Boolean =
    (minUtcMillis == null || year >= yearOf(minUtcMillis)) &&
        (maxUtcMillis == null || year <= yearOf(maxUtcMillis))

@OptIn(ExperimentalMaterial3Api::class)
internal fun DsDateBounds.toSelectableDates(): SelectableDates =
    object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean = allows(utcTimeMillis)

        override fun isSelectableYear(year: Int): Boolean = allowsYear(year)
    }
