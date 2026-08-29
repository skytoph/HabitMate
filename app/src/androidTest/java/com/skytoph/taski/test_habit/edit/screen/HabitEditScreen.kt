package com.skytoph.taski.test_habit.edit.screen

import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.semantics.ColorRes

class HabitEditScreen<A: ComponentActivity>(private val rule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>) {

    fun createAndSaveHabit(title: String, description: String) {
        setTitle(title)
        setDescription(description)
    }

    fun setTitle(title: String) = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_enter_habit_title))
            .performTextInput(title)
    }

    fun setDescription(description: String) = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_enter_habit_description))
            .performTextInput(description)
    }

    fun iconIsSelected(@DrawableRes icon: Int) = with(rule) {
        onNodeWithContentDescription(rule.activity.resources.getResourceEntryName(icon))
            .assertIsDisplayed()
    }

    fun colorIsSelected(color: Color) = with(rule) {
        onNode(
            hasContentDescription(activity.getString(R.string.content_description_select_habit_icon_and_color))
                    and SemanticsMatcher.Companion.expectValue(ColorRes, color)
        ).assertIsDisplayed()
    }

    fun clickIncreaseGoal() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_increase_habit_goal))
            .performClick()
    }

    fun clickDecreaseGoal() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_decrease_habit_goal))
            .performClick()
    }

    fun increaseGoalIsEnabled() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_increase_habit_goal))
            .assertHasClickAction()
            .assertIsEnabled()
    }

    fun decreaseGoalIsEnabled() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_decrease_habit_goal))
            .assertHasClickAction()
            .assertIsEnabled()
    }

    fun increaseGoalIsDisabled() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_increase_habit_goal))
            .assertHasClickAction()
            .assertIsNotEnabled()
    }

    fun decreaseGoalIsDisabled() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_decrease_habit_goal))
            .assertHasClickAction()
            .assertIsNotEnabled()
    }

    fun clickSelectIconAndColor() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_select_habit_icon_and_color))
            .assertHasClickAction()
            .assertIsEnabled()
            .performClick()
    }

    fun emptyTitleErrorIsVisible() = with(rule) {
        onNodeWithText(activity.getString(R.string.error_habit_title_is_empty))
            .assertIsDisplayed()
    }

    fun clickSave() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.action_save_habit))
            .performClick()
    }

    // todo test reminders and frequency
}