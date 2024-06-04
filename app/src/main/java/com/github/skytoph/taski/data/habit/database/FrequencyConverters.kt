package com.github.skytoph.taski.data.habit.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass

object FrequencyConverters {
    @TypeConverter
    fun fromString(value: String?): FrequencyEntity = gson.fromJson(value, FrequencyEntity::class.java)

    @TypeConverter
    fun fromFrequency(frequency: FrequencyEntity): String = gson.toJson(frequency, FrequencyEntity::class.java)

    private val gson = GsonBuilder().registerTypeAdapterFactory(
        object : TypeAdapterFactory {
            override fun <T : Any> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
                val classType = Reflection.getOrCreateKotlinClass(type.rawType)
                return if (classType.sealedSubclasses.any())
                    SealedClassTypeAdapter(classType, gson)
                else
                    gson.getDelegateAdapter(this, type)
            }
        }).create()
}

class SealedClassTypeAdapter<T : Any>(
    private val classType: KClass<Any>,
    private val gson: Gson
) : TypeAdapter<T>() {
    override fun read(jsonReader: JsonReader): T {
        jsonReader.beginObject()
        val nextName = jsonReader.nextName()
        val innerClass = classType.sealedSubclasses
            .firstOrNull { it.simpleName!!.contains(nextName) }
            ?: throw Exception("$nextName is not found among subclasses of the sealed class ${classType.qualifiedName}")
        val data = gson.fromJson<T>(jsonReader, innerClass.javaObjectType)
        jsonReader.endObject()
        return data
    }

    override fun write(out: JsonWriter, value: T) {
        val jsonString = gson.toJson(value)
        out.beginObject()
        out.name(value.javaClass.canonicalName?.splitToSequence(".")?.last()).jsonValue(jsonString)
        out.endObject()
    }
}
