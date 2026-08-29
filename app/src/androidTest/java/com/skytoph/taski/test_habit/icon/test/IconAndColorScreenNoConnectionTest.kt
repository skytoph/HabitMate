package com.skytoph.taski.test_habit.icon.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.skytoph.taski.di.TestRewardErrorModule
import com.skytoph.taski.mock.data.HabitIconData
import com.skytoph.taski.presentation.MainActivity
import com.skytoph.taski.presentation.habit.icon.RewardFailResult
import com.skytoph.taski.test_habit.icon.screen.IconAndColorScreen
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(TestRewardErrorModule::class)
class IconAndColorScreenNoConnectionTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val error: RewardFailResult? = RewardFailResult.NoConnection

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun unlockIcon_fail() {
        val screen = IconAndColorScreen(rule)
        val icon = HabitIconData.getLocked()

        screen.navigateTo_selectIconScreen()

        screen.click_icon(icon)
        screen.assert_unlockIconDialog_isVisible()
        screen.click_unlockIconButton()
        screen.assert_noConnectionError_isVisible()
    }
}