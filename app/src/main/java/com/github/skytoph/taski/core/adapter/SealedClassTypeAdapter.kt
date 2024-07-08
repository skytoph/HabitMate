package com.github.skytoph.taski.core.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlin.reflect.KClass

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