package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset

class DsDateRangePresetsTest {
    private fun day(
        year: Int,
        month: Int,
        dayOfMonth: Int,
    ) = LocalDate
        .of(year, month, dayOfMonth)
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()

    // Friday 2026-03-06.
    private val friday = day(2026, 3, 6)

    @Test
    fun `today is a single day and yesterday the one before`() {
        assertEquals(friday to friday, dsRangeToday(friday))
        assertEquals(day(2026, 3, 5) to day(2026, 3, 5), dsRangeYesterday(friday))
    }

    @Test
    fun `this week runs from the week start to today, not to the end of the week`() {
        assertEquals(day(2026, 3, 2) to friday, dsRangeThisWeek(friday))
        assertEquals(day(2026, 3, 1) to friday, dsRangeThisWeek(friday, weekStart = DayOfWeek.SUNDAY))
    }

    @Test
    fun `on the week start day the week is that single day`() {
        val monday = day(2026, 3, 2)
        assertEquals(monday to monday, dsRangeThisWeek(monday))
    }

    @Test
    fun `this month starts on the first and last 7 days includes today`() {
        assertEquals(day(2026, 3, 1) to friday, dsRangeThisMonth(friday))
        assertEquals(day(2026, 2, 28) to friday, dsRangeLast7Days(friday))
    }

    @Test
    fun `the default presets are the four a report asks for`() {
        assertEquals(
            listOf("Aujourd'hui", "Hier", "Cette semaine", "Ce mois"),
            dsDefaultDateRangePresets().map { it.label },
        )
    }
}
