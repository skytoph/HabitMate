package com.skytoph.taski.test_habit.icon.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.skytoph.taski.mock.data.HabitIconData
import com.skytoph.taski.presentation.MainActivity
import com.skytoph.taski.test_habit.icon.screen.IconAndColorScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class IconAndColorScreenSuccessTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun unlockIcon_success() {
        val screen = IconAndColorScreen(rule)
        val icon = HabitIconData.getLocked()

        screen.navigateTo_selectIconScreen()

        screen.click_icon(icon)
        screen.assert_unlockIconDialog_isVisible()
        screen.click_unlockIconButton()
        screen.assert_iconUnlockedMessage_isVisible()
    }
}