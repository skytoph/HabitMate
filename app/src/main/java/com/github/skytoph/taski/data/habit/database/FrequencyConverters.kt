package com.github.skytoph.taski.data.habit.database

import androidx.room.TypeConverter
import com.github.skytoph.taski.core.adapter.GeneralTypeAdapterFactory
import com.google.gson.GsonBuilder

object FrequencyConverters {
    @TypeConverter
    fun fromString(value: String?): FrequencyEntity = gson.fromJson(value, FrequencyEntity::class.java)

    @TypeConverter
    fun fromFrequency(frequency: FrequencyEntity): String = gson.toJson(frequency, FrequencyEntity::class.java)

    private val gson = GsonBuilder().registerTypeAdapterFactory(GeneralTypeAdapterFactory()).create()
}