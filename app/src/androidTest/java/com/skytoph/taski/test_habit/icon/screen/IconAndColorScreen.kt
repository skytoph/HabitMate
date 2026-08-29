package com.skytoph.taski.test_habit.icon.screen

import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.skytoph.taski.R
import com.skytoph.taski.mock.const.TestConst
import com.skytoph.taski.test_habit.edit.screen.HabitEditScreen
import com.skytoph.taski.test_habit.list.screen.HabitListScreen

class IconAndColorScreen<A: ComponentActivity>(private val rule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>) {

    fun click_icon(@DrawableRes icon: Int) = with(rule) {
        val iconName = rule.activity.resources.getResourceEntryName(icon)
        onNodeWithTag(iconName)
            .assertIsDisplayed()
            .performClick()
    }

    fun click_color(color: Color) = with(rule) {
        waitUntil(TestConst.WAIT_TIMEOUT) {
            onNodeWithTag(color.toString())
                .isDisplayed()
        }
        onNodeWithTag(color.toString())
            .assertIsDisplayed()
            .performClick()
    }

    fun navigateBack() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.action_navigate_up))
            .performClick()
    }

    fun assert_unlockIconDialog_isVisible() = with(rule) {
        onNodeWithText(activity.getString(R.string.unlock_icon_title))
            .assertIsDisplayed()
        onNodeWithText(activity.getString(R.string.unlock_icon_description))
            .assertIsDisplayed()
        onNodeWithText(activity.getString(R.string.action_watch_ad))
            .assertHasClickAction()
            .assertIsEnabled()
    }

    fun click_unlockIconButton() = with(rule) {
        onNodeWithText(activity.getString(R.string.action_watch_ad))
            .performClick()
    }

    fun assert_iconUnlockedMessage_isVisible() = with(rule) {
        onNodeWithText(activity.getString(R.string.reward_unlocked))
            .isDisplayed()
    }

    fun assert_noConnectionError_isVisible() = with(rule) {
        onNodeWithText(activity.getString(R.string.error_no_connection))
            .isDisplayed()
    }

    fun navigateTo_selectIconScreen() = with(rule) {
        HabitListScreen(rule).clickCreateButton()
        HabitEditScreen(rule).clickSelectIconAndColor()
    }

    // todo test icons sync
}