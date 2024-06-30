package com.github.skytoph.taski.core

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class NowTest {

    @Test
    fun test() {
        assertEquals(Calendar.MONDAY, dayOfWeek(1))     //2
        assertEquals(Calendar.TUESDAY, dayOfWeek(2))    //3
        assertEquals(Calendar.WEDNESDAY, dayOfWeek(3))  //4
        assertEquals(Calendar.THURSDAY, dayOfWeek(4))   //5
        assertEquals(Calendar.FRIDAY, dayOfWeek(5))     //6
        assertEquals(Calendar.SATURDAY, dayOfWeek(6))   //7
        assertEquals(Calendar.SUNDAY, dayOfWeek(7))     //1
    }

    fun dayOfWeek(day: Int): Int =
        Calendar.getInstance().let { (day + it.firstDayOfWeek + 5) % 7 + 1  }

}