package com.github.skytoph.taski.core

import java.util.Calendar

interface Now {
    fun dayOfWeek(): Int

    class Base : Now {
        override fun dayOfWeek(): Int =
            Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }
}