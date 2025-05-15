package com.skytoph.taski.core.crashlytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.skytoph.taski.core.datastore.CrashlyticsIdDataStore

interface Crashlytics {
    suspend fun allow(allow: Boolean)

    class Base(private val idDatastore: CrashlyticsIdDataStore) : Crashlytics {

        override suspend fun allow(allow: Boolean) {
            Firebase.crashlytics.isCrashlyticsCollectionEnabled = allow
            Firebase.analytics.setAnalyticsCollectionEnabled(allow)
            if (allow) Firebase.crashlytics.setUserId(idDatastore.id())
        }
    }
}
