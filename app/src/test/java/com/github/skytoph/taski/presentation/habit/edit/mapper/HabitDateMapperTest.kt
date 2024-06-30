package com.github.skytoph.taski.presentation.habit.edit.mapper

import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class HabitDateMapperTest {

    private val mapper: HabitDateMapper = HabitDateMapper.Base()

    @Test
    fun test() {
        val everyday = FrequencyUi.Everyday(FrequencyUi.Daily()).dates(mapper)
        assertEquals(1, everyday.size)
        assertEquals(
            Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
            everyday.first().get(Calendar.DAY_OF_YEAR)
        )

        val daily = FrequencyUi.Daily(setOf(1, 5)).dates(mapper)
        assertEquals(2, daily.size)
        assertEquals(Calendar.MONDAY, daily[0].get(Calendar.DAY_OF_WEEK))
        assertEquals(Calendar.FRIDAY, daily[1].get(Calendar.DAY_OF_WEEK))

        val monthly = FrequencyUi.Monthly(setOf(1, 5)).dates(mapper)
        assertEquals(2, monthly.size)
        assertEquals(1, monthly[0].get(Calendar.DAY_OF_MONTH))
        assertEquals(5, monthly[1].get(Calendar.DAY_OF_MONTH))

        val customDaily = FrequencyUi.Custom(
            timesCount = GoalState(2),
            typeCount = GoalState(5),
            frequencyType = FrequencyCustomType.Day
        ).dates(mapper)
        val today = Calendar.getInstance()
        assertEquals(2, customDaily.size)
        assertEquals(today.get(Calendar.DAY_OF_MONTH), customDaily[0].get(Calendar.DAY_OF_MONTH))
        today.add(Calendar.DAY_OF_YEAR, 2)
        assertEquals(today.get(Calendar.DAY_OF_MONTH), customDaily[1].get(Calendar.DAY_OF_MONTH))

        val customWeekly = FrequencyUi.Custom(
            timesCount = GoalState(2),
            typeCount = GoalState(1),
            frequencyType = FrequencyCustomType.Week
        ).dates(mapper)
        assertEquals(2, customWeekly.size)
        assertEquals(Calendar.MONDAY, customWeekly[0].get(Calendar.DAY_OF_WEEK))
        assertEquals(Calendar.THURSDAY, customWeekly[1].get(Calendar.DAY_OF_WEEK))

        val customMonthly = FrequencyUi.Custom(
            timesCount = GoalState(2),
            typeCount = GoalState(1),
            frequencyType = FrequencyCustomType.Month
        ).dates(mapper)
        assertEquals(2, customMonthly.size)
        assertEquals(1, customMonthly[0].get(Calendar.DAY_OF_MONTH))
        assertEquals(16, customMonthly[1].get(Calendar.DAY_OF_MONTH))
    }
}