package com.skytoph.taski.core.crashlytics

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

interface Crashlytics {
    fun allow(allow: Boolean)

    class Base : Crashlytics {
        override fun allow(allow: Boolean) {
            Firebase.crashlytics.isCrashlyticsCollectionEnabled = allow
        }
    }
}
