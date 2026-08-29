package com.skytoph.taski.test_habit.list.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.skytoph.taski.R
import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.mock.const.TestConst
import com.skytoph.taski.presentation.core.semantics.ColorRes

class HabitListScreen<A : ComponentActivity>(private val rule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>) {

    fun wait_titleLabel_isDisplayed() = with(rule) {
        waitUntil(TestConst.WAIT_TIMEOUT) {
            onNodeWithText(activity.getString(R.string.habit_list_title))
                .isDisplayed()
        }
    }

    fun assertStateIsEmpty() = with(rule) {
        onNodeWithText(activity.getString(R.string.list_of_habits_is_empty_create_label))
            .assertIsDisplayed()
        onNodeWithText(activity.getString(R.string.action_create))
            .assertIsDisplayed()
        onNodeWithContentDescription(activity.getString(R.string.action_add))
            .assertDoesNotExist()
    }

    fun clickCreateButton() = with(rule) {
        onNodeWithText(activity.getString(R.string.action_create))
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertIsEnabled()
            .performClick()
        waitForIdle()
    }

    fun clickCreateMenuItem() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.action_add))
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertIsEnabled()
            .performClick()
        waitForIdle()
    }

    fun assertHabitIsVisible(title: String) = with(rule) {
        onNodeWithText(title)
            .assertIsDisplayed()
    }

    fun clickHabit(name: String, icon: Int, color: Color) = with(rule) {
        onHabitNode(name, activity.resources.getResourceEntryName(icon), color)
            .assertIsDisplayed()
            .performClick()
    }

    fun assertCheckButtonIsVisible(habitName: String, view: ViewType) = with(rule) {
        when (view) {
            is ViewType.Calendar ->
                onNodeWithContentDescription(
                    activity.getString(R.string.content_description_habit_calendar_check_icon, habitName)
                )
                    .assertHasClickAction()
                    .assertIsEnabled()
                    .assertExists()

            is ViewType.Daily ->
                repeat(view.entries) { index ->
                    onNodeWithContentDescription(
                        activity.getString(R.string.content_description_habit_daily_check_icon, habitName, index)
                    )
                        .assertHasClickAction()
                        .assertIsEnabled()
                }
        }
    }

    fun scrollToIndex(index: Int) = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_list_of_habits))
            .performScrollToIndex(index)
    }

    fun assertHabitIsVisible(habitName: String, habitColor: Color, habitIconName: String) = with(rule) {
        onAllNodesWithText(habitName)
            .onFirst()
            .assertExists()

        onHabitNode(habitName, habitIconName, habitColor)
            .assertIsDisplayed()
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onHabitNode(
        habitName: String,
        habitIconName: String,
        habitColor: Color
    ): SemanticsNodeInteraction = onAllNodes(
        hasTestTag(activity.getString(R.string.test_tag_habit_item, habitName, habitIconName))
                and hasAnyChild(hasText(habitName))
                and hasAnyChild(SemanticsMatcher.expectValue(ColorRes, habitColor)),
        true
    ).onFirst()

    fun assertStateIsLoading() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_loading))
            .assertIsDisplayed()
    }

    fun assertCrashlyticsDialogIsVisible() = with(rule) {
        waitUntil(TestConst.WAIT_TIMEOUT) {
            onNodeWithText(activity.getString(R.string.allow_crashlytics_title))
                .isDisplayed()
        }
        onNodeWithText(activity.getString(R.string.allow_crashlytics_description))
            .assertIsDisplayed()
    }

    fun clickCrashlyticsDialog(confirm: Boolean) = with(rule) {
        if (confirm)
            onNodeWithText(activity.getString(R.string.action_allow))
                .performClick()
        else
            onNodeWithText(activity.getString(R.string.action_dismiss))
                .performClick()
    }
}

