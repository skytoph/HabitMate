package com.github.skytoph.taski.presentation.habit.frequency

import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import junit.framework.TestCase.assertEquals
import org.junit.Test

class FrequencyUiTest{

    @Test
    fun test(){
        var frequency = FrequencyUi.Daily(setOf())
        frequency = frequency.update(2)
        frequency = frequency.update(1)
        assertEquals(frequency.days, mutableSetOf(1, 2))
        frequency = frequency.update(2)
        assertEquals(frequency.days, mutableSetOf(1))
        frequency = frequency.update(1)
        assertEquals(frequency.days, mutableSetOf(1))
    }
}