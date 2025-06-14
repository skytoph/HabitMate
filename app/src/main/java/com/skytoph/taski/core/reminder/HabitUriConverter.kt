package com.skytoph.taski.core.reminder

import android.net.Uri

interface HabitUriConverter {
    fun uri(id: Long, day: Int): Uri
    fun uri(value: String): Uri
    fun id(uri: Uri): Long

    class Base : HabitUriConverter {
        override fun uri(id: Long, day: Int): Uri = Uri.parse("habit/$id/$day")

        override fun uri(value: String): Uri = Uri.parse(value)

        override fun id(uri: Uri): Long =
            uri.pathSegments.let { segments -> segments[segments.size - 2].toLongOrNull() ?: 0L }
    }
}
