package com.skytoph.taski.presentation

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.skytoph.taski.core.crashlytics.Crashlytics
import com.skytoph.taski.core.datastore.SettingsCache
import com.skytoph.taski.presentation.settings.InitStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(settings: SettingsCache, private val crashlytics: Crashlytics) :
    InitStateViewModel(settings) {

    fun init(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        MobileAds.initialize(context)
        settings().map { it.allowCrashlytics }.distinctUntilChanged().collect { it?.let { crashlytics.allow(it) } }
    }
}