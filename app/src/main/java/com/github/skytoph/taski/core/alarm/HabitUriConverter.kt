package com.github.skytoph.taski.core.alarm

import android.net.Uri

interface HabitUriConverter {
    fun uri(id: Long, day: Int): Uri

    class Base : HabitUriConverter {
        override fun uri(id: Long, day: Int): Uri = Uri.parse("habit/$id/$day")
    }
}
