package com.github.skytoph.taski.core.datastore

import androidx.annotation.Keep
import androidx.datastore.core.Serializer
import com.github.skytoph.taski.core.adapter.GeneralTypeAdapterFactory
import com.github.skytoph.taski.core.datastore.settings.InitializeEmptyValues
import com.github.skytoph.taski.core.datastore.settings.Settings
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

@Keep
object SettingsSerializer : Serializer<Settings> {

    private val gson = GsonBuilder().registerTypeAdapterFactory(GeneralTypeAdapterFactory()).create()

    override val defaultValue: Settings = Settings.default(InitializeEmptyValues())

    override suspend fun readFrom(input: InputStream): Settings =
        gson.fromJson(input.readBytes().decodeToString(), Settings::class.java)

    override suspend fun writeTo(value: Settings, output: OutputStream) = withContext(Dispatchers.IO) {
        output.write(gson.toJson(value).toByteArray())
    }

    const val FILENAME: String = "settings.json"
}