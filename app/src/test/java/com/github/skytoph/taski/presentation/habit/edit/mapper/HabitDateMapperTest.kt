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
            everyday.iterator().next().value.get(Calendar.DAY_OF_YEAR)
        )

        val daily = FrequencyUi.Daily(setOf(1, 5)).dates(mapper)
        val iteratorDaily = daily.iterator()
        assertEquals(2, daily.size)
        assertEquals(Calendar.MONDAY, iteratorDaily.next().value.get(Calendar.DAY_OF_WEEK))
        assertEquals(Calendar.FRIDAY, iteratorDaily.next().value.get(Calendar.DAY_OF_WEEK))

        val monthly = FrequencyUi.Monthly(setOf(1, 5)).dates(mapper)
        val iteratorMonthly = monthly.iterator()
        assertEquals(2, monthly.size)
        assertEquals(1, iteratorMonthly.next().value.get(Calendar.DAY_OF_MONTH))
        assertEquals(5, iteratorMonthly.next().value.get(Calendar.DAY_OF_MONTH))

        val customDaily = FrequencyUi.Custom(
            timesCount = GoalState(2),
            typeCount = GoalState(5),
            frequencyType = FrequencyCustomType.Day
        ).dates(mapper)
        val customDailyIterator = customDaily.iterator()
        val today = Calendar.getInstance()
        assertEquals(2, customDaily.size)
        assertEquals(today.get(Calendar.DAY_OF_MONTH), customDailyIterator.next().value.get(Calendar.DAY_OF_MONTH))
        today.add(Calendar.DAY_OF_YEAR, 2)
        assertEquals(today.get(Calendar.DAY_OF_MONTH), customDailyIterator.next().value.get(Calendar.DAY_OF_MONTH))

        val customWeekly = FrequencyUi.Custom(
            timesCount = GoalState(2),
            typeCount = GoalState(1),
            frequencyType = FrequencyCustomType.Week
        ).dates(mapper)
        val customWeeklyIterator = customWeekly.iterator()
        assertEquals(2, customWeekly.size)
        assertEquals(Calendar.MONDAY, customWeeklyIterator.next().value.get(Calendar.DAY_OF_WEEK))
        assertEquals(Calendar.THURSDAY, customWeeklyIterator.next().value.get(Calendar.DAY_OF_WEEK))

        val customMonthly = FrequencyUi.Custom(
            timesCount = GoalState(2),
            typeCount = GoalState(1),
            frequencyType = FrequencyCustomType.Month
        ).dates(mapper)
        val customMonthlyIterator = customMonthly.iterator()
        assertEquals(2, customMonthly.size)
        assertEquals(1, customMonthlyIterator.next().value.get(Calendar.DAY_OF_MONTH))
        assertEquals(15, customMonthlyIterator.next().value.get(Calendar.DAY_OF_MONTH))
    }
}