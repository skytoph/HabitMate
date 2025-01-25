package com.github.skytoph.taski.presentation.core

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

interface Logger {
    fun log(string: String)
    fun log(exception: Exception)

    class Crashlytics(private val crashlytics: FirebaseCrashlytics = Firebase.crashlytics) : Logger {
        override fun log(string: String) {
            crashlytics.log(string)
        }

        override fun log(exception: Exception) {
            crashlytics.recordException(exception)
        }
    }
}