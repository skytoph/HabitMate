package com.skytoph.benchmark

import android.view.KeyEvent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    fun startup(mode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.skytoph.taski",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = mode
    ) {
        pressHome()
        startActivityAndWait()
    }

    fun selectHabit(mode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.skytoph.taski",
        metrics = listOf(FrameTimingMetric()),
        iterations = 1,
        startupMode = StartupMode.COLD,
        compilationMode = mode
    ) {
        pressHome()
        startActivityAndWait()

        createAndClickHabit()
    }

    @Test
    fun startupCompilationModeNone() = startup(CompilationMode.None())

    @Test
    fun startupCompilationModePartial() = startup(CompilationMode.Partial())

    @Test
    fun selectCompilationModeNone() = selectHabit(CompilationMode.None())

    @Test
    fun selectCompilationModePartial() = selectHabit(CompilationMode.Partial())
}

fun MacrobenchmarkScope.createAndClickHabit() {
    device.wait(Until.hasObject(By.desc("add")), 3000)
    device.findObject(By.desc("add")).click()
    device.wait(Until.hasObject(By.desc("text field")), 3000)
    device.findObject(By.desc("text field")).click()
    device.pressKeyCode(KeyEvent.KEYCODE_1)
    device.findObject(By.desc("save habit")).click()
    device.waitForIdle()
    device.wait(Until.hasObject(By.desc("habit")), 2000)
    device.findObject(By.desc("habit")).click()
    device.waitForIdle()
    device.wait(Until.hasObject(By.desc("navigate up")), 3000)
    device.findObject(By.desc("navigate up")).click()
    device.waitForIdle()
}