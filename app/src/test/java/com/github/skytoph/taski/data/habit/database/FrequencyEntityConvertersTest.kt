package com.github.skytoph.taski.data.habit.database

import junit.framework.TestCase.assertEquals
import org.junit.Test

class FrequencyEntityConvertersTest {

    @Test
    fun test() {
        val frequency = FrequencyEntity.Daily(listOf(1))
        val json = FrequencyConverters.fromFrequency(frequency)
        val fromJson = FrequencyConverters.fromString(json)
        val convertedJson = FrequencyConverters.fromFrequency(fromJson)
        assertEquals(json, convertedJson)

        val frequencyFromJson = FrequencyConverters.fromString(convertedJson)
        assertEquals(frequency, frequencyFromJson)
    }
}