package com.skytoph.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@RequiresApi(Build.VERSION_CODES.P)
class BaselineProfileGenerator {

    @get:Rule
    val baselineRule =  BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baselineRule.collect("com.skytoph.taski"){
        pressHome()
        startActivityAndWait()

        createAndClickHabit()
    }
}