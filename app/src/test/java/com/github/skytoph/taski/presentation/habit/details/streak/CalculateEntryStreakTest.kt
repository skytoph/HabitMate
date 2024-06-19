package com.github.skytoph.taski.presentation.habit.details.streak

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.domain.habit.Entry
import com.github.skytoph.taski.domain.habit.EntryList
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class CalculateEntryStreakTest {
    private val goal = 1
    private val data = EntryList(
        entries = mapOf(
            0 to Entry(timestamp = 1000L, goal), // 1 .07
            1 to Entry(timestamp = 7000L, goal), // 30.06
//          2 to Entry(timestamp = 6000L, goal), // 29
            3 to Entry(timestamp = 5000L, goal), // 28
            4 to Entry(timestamp = 4000L, goal), // 27
            5 to Entry(timestamp = 3000L, goal), // 26
//          6 to Entry(timestamp = 2000L, goal), // 25
            7 to Entry(timestamp = 1000L, goal), // 24
            8 to Entry(timestamp = 7000L, goal), // 23
//          9 to Entry(timestamp = 6000L, goal), // 22
            10 to Entry(timestamp = 5000L, goal),// 21
//          11 to Entry(timestamp = 4000L, goal),// 20
            12 to Entry(timestamp = 3000L, goal),// 19
            13 to Entry(timestamp = 2000L, goal),// 18
            14 to Entry(timestamp = 1000L, goal),// 17
            15 to Entry(timestamp = 7000L, goal),// 16
            16 to Entry(timestamp = 6000L, goal),// 15
//          ...
            29 to Entry(timestamp = 7000L, goal),// 2
            30 to Entry(timestamp = 6000L, goal),// 1 .06
            31 to Entry(timestamp = 5000L, goal),// 31.05
//          ...
            61 to Entry(timestamp = 3000L, goal),// 1 .05
            62 to Entry(timestamp = 2000L, goal),// 30.04
        )
    )
    private val dataSecond = EntryList(
        entries = mapOf(
            61 to Entry(timestamp = 3000L, goal),// 1 .05
            62 to Entry(timestamp = 3000L, goal),// 30.04
//          ...
            92 to Entry(timestamp = 3000L, goal),// 31.03
//          ...
            121 to Entry(timestamp = 3000L, goal),// 2.03
            122 to Entry(timestamp = 3000L, goal),// 1.03
            123 to Entry(timestamp = 3000L, goal),//29.02
        )
    )

    private val now = TestNow()

    private val streakEveryday: CalculateStreak = CalculateEverydayStreak()

    @Test
    fun test() {
        assertEquals(5, streakEveryday.streaks(data.entries, goal).max())
        assertEquals(2, streakEveryday.currentStreak(data.entries, goal))

        assertEquals(2, CalculateDailyStreak(now, setOf(6)).currentStreak(data.entries, goal))
        assertEquals(8, CalculateDailyStreak(now, setOf(4)).currentStreak(data.entries, goal))
        assertEquals(13, CalculateDailyStreak(now, setOf(3, 5)).currentStreak(data.entries, goal))
        assertEquals(5, CalculateDailyStreak(now, setOf(2, 4)).currentStreak(data.entries, goal))

        assertEquals(
            listOf(2, 5, 6, 3, 2),
            CalculateDailyStreak(now, setOf(1, 6)).streaks(data.entries, goal)
        )
        assertEquals(
            listOf(2, 3, 2, 6, 3, 2),
            CalculateDailyStreak(now, setOf(2, 6)).streaks(data.entries, goal)
        )
        assertEquals(
            listOf(8, 5, 3, 2),
            CalculateDailyStreak(now, setOf(4)).streaks(data.entries, goal)
        )
        assertEquals(
            listOf(2, 5, 6, 3, 2),
            CalculateDailyStreak(now, setOf(6)).streaks(data.entries, goal)
        )

        assertEquals(
            16,
            CalculateMonthlyStreak(now, setOf(28, 27)).currentStreak(data.entries, goal)
        )
        assertEquals(2, CalculateMonthlyStreak(now, setOf(29)).currentStreak(data.entries, goal))
        assertEquals(
            listOf(7, 1, 8, 2),
            CalculateMonthlyStreak(now, setOf(20, 22)).streaks(data.entries, goal)
        )

        val dataWithout31 = data.entries.toMutableMap().apply { remove(31) }
        assertEquals(
            listOf(15, 2),
            CalculateMonthlyStreak(now, setOf(31)).streaks(dataWithout31, goal)
        )
        assertEquals(
            listOf(6),
            CalculateMonthlyStreak(now, setOf(31)).streaks(dataSecond.entries, goal)
        )
        val dataWithout121 = dataSecond.entries.toMutableMap().apply { remove(121) }
        assertEquals(
            listOf(3, 2),
            CalculateMonthlyStreak(now, setOf(31)).streaks(dataWithout121, goal)
        )
    }
}

private class TestNow(
    // 1.07.2024 (MON - 1)
    private val today: Calendar = Calendar.getInstance().apply { set(2024, 6, 1) }
) : Now by Now.Base() {

    override fun dayOfWeek(): Int = 0
    override fun dayOfMonths(daysAgo: Int): Int = calendar(daysAgo)
        .get(Calendar.DAY_OF_MONTH)

    private fun calendar(daysAgo: Int) =
        (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -daysAgo) }

    override fun daysInMonth(daysAgo: Int): Int =
        calendar(daysAgo).getActualMaximum(Calendar.DAY_OF_MONTH)
}