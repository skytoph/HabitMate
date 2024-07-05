package com.github.skytoph.taski.core.alarm

import android.net.Uri

interface HabitUriConverter {
    fun uri(id: Long, day: Int): Uri
    fun id(uri: Uri): Long

    class Base : HabitUriConverter {
        override fun uri(id: Long, day: Int): Uri = Uri.parse("habit/$id/$day")

        override fun id(uri: Uri): Long =
            uri.pathSegments.let { segments -> segments[segments.size - 2].toLongOrNull() ?: 0L }
    }
}
