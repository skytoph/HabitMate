package com.skytoph.taski.core.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import kotlin.jvm.internal.Reflection

class GeneralTypeAdapterFactory : TypeAdapterFactory {
    override fun <T : Any> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
        val classType = Reflection.getOrCreateKotlinClass(type.rawType)
        return if (classType.sealedSubclasses.any())
            SealedClassTypeAdapter(classType, gson)
        else
            gson.getDelegateAdapter(this, type)
    }
}