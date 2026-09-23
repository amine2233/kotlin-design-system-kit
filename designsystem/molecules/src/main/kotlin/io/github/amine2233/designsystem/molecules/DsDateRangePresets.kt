package io.github.amine2233.designsystem.molecules

import androidx.compose.runtime.Immutable
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters

/**
 * A named period offered above the range calendar — "Cette semaine" and the like.
 *
 * [range] takes the day the user considers "today" as UTC millis and returns the period's two ends,
 * so nothing here reads a clock: the caller decides what today is, which keeps reports reproducible
 * and snapshots deterministic.
 */
@Immutable
data class DsDateRangePreset(
    val label: String,
    val range: (todayUtcMillis: Long) -> Pair<Long, Long>,
)

private fun dayOf(utcMillis: Long): LocalDate = Instant.ofEpochMilli(utcMillis).atZone(ZoneOffset.UTC).toLocalDate()

private fun LocalDate.utcMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

/** The day itself, start and end being the same day. */
fun dsRangeToday(todayUtcMillis: Long): Pair<Long, Long> = todayUtcMillis to todayUtcMillis

fun dsRangeYesterday(todayUtcMillis: Long): Pair<Long, Long> = dayOf(todayUtcMillis).minusDays(1).utcMillis().let { it to it }

/** [weekStart] to today — the running week, not the full seven days, since the rest has not happened. */
fun dsRangeThisWeek(
    todayUtcMillis: Long,
    weekStart: DayOfWeek = DayOfWeek.MONDAY,
): Pair<Long, Long> = dayOf(todayUtcMillis).with(TemporalAdjusters.previousOrSame(weekStart)).utcMillis() to todayUtcMillis

/** The 1st of the month to today. */
fun dsRangeThisMonth(todayUtcMillis: Long): Pair<Long, Long> = dayOf(todayUtcMillis).withDayOfMonth(1).utcMillis() to todayUtcMillis

/** The seven days ending today, today included. */
fun dsRangeLast7Days(todayUtcMillis: Long): Pair<Long, Long> = dayOf(todayUtcMillis).minusDays(6).utcMillis() to todayUtcMillis

/**
 * The four periods a POS report asks for. Labels are French like the rest of the defaults; pass your
 * own list to translate them or to offer different periods (a fiscal quarter, the last shift).
 */
fun dsDefaultDateRangePresets(
    today: String = "Aujourd'hui",
    yesterday: String = "Hier",
    thisWeek: String = "Cette semaine",
    thisMonth: String = "Ce mois",
    weekStart: DayOfWeek = DayOfWeek.MONDAY,
): List<DsDateRangePreset> =
    listOf(
        DsDateRangePreset(today, ::dsRangeToday),
        DsDateRangePreset(yesterday, ::dsRangeYesterday),
        DsDateRangePreset(thisWeek) { dsRangeThisWeek(it, weekStart) },
        DsDateRangePreset(thisMonth, ::dsRangeThisMonth),
    )

/** Today at UTC day precision — the unit every date component in the system speaks. */
fun dsTodayUtcMillis(): Long = LocalDate.now(ZoneOffset.UTC).utcMillis()
