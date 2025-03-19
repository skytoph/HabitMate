package com.skytoph.taski.presentation.core

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

interface Logger {
    fun log(string: String, tag: String = "")
    fun log(exception: Exception, tag: String = "")
    fun logDebug(string: String, tag: String = "")

    class Crashlytics(private val crashlytics: FirebaseCrashlytics = Firebase.crashlytics) : Logger {
        override fun log(string: String, tag: String) {
            log(Exception(string), tag)
        }

        override fun log(exception: Exception, tag: String) {
            crashlytics.recordException(exception)
        }

        override fun logDebug(string: String, tag: String) = Unit
    }

    object Debug : Logger {
        override fun log(string: String, tag: String) {
            log(Exception(string), tag)
        }

        override fun log(exception: Exception, tag: String) {
            Log.e(tag, exception.stackTraceToString())
        }

        override fun logDebug(string: String, tag: String) {
            log(string, tag)
        }
    }
}