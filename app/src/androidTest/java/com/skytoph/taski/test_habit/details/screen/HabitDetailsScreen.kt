package com.skytoph.taski.test_habit.details.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.skytoph.taski.R
import com.skytoph.taski.mock.const.TestConst
import com.skytoph.taski.presentation.core.semantics.ColorRes
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.details.BarItemUi
import com.skytoph.taski.presentation.habit.details.ChartFrequencyUi
import com.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import java.util.Locale

class HabitDetailsScreen<A : ComponentActivity>(private val rule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>) {

    fun wait_titleLabel_isDisplayed() = with(rule) {
        waitUntil(TestConst.WAIT_TIMEOUT) {
            onNodeWithText(activity.getString(R.string.habit_details_label))
                .isDisplayed()
        }
    }

    fun habitDetails_isDisplayed(
        habit: HabitUi,
        entries: List<EditableHistoryUi>,
        barChart: List<BarItemUi>,
        stats: HabitStatisticsUi,
        barChartFrequency: ChartFrequencyUi.Week
    ) {
        assert_habitInfo_isDisplayed(habit)
        assert_statistics_isDisplayed(stats)
        assert_historyCalendar_isDisplayed(entries)
        assert_barChart_isDisplayed(barChartFrequency, barChart)
    }

    fun assert_habitInfo_isDisplayed(habit: HabitUi) {
        assert_habitName_isDisplayed(habit.title)
        assert_habitDescription_isDisplayed(habit.description)
        assert_habitIconAndColor_isDisplayed(
            habitName = habit.title,
            habitColor = habit.color,
            habitIconName = habit.icon.name(rule.activity.resources)
        )

        assert_habitFrequency_isDisplayed(
            frequency = habit.frequency.summarize(rule.activity.resources, true, Locale.getDefault()).toString()
        )
        assert_habitGoal_isDisplayed(habit.goal)
        assert_habitReminder_isDisplayed(rule.activity.getString(R.string.reminder_turned_off))
    }

    fun assert_statistics_isDisplayed(stats: HabitStatisticsUi) {
        assert_totalCompletedDays_isDisplayed(stats.total)
        assert_bestStreak_isDisplayed(stats.bestStreak)
        assert_currentStreak_isDisplayed(stats.currentStreak)
    }

    fun assert_historyCalendar_isDisplayed(entries: List<EditableHistoryUi>) {
        assert_habitHistoryCalendar_isDisplayed()
        // todo check items
    }

    fun assert_historyHeatmap_isDisplayed(entries: List<EditableHistoryUi>) {
        assert_habitHistoryHeatmap_isDisplayed()
        // todo check items
    }

    fun assert_barChart_isDisplayed(frequency: ChartFrequencyUi, items: List<BarItemUi>) {
        scrollTo_barChart()
        assert_barChart_isVisible()
        assert_barChartFrequency_isSelected(rule.activity.getString(frequency.label))
        items.forEachIndexed { index, item ->
            scrollTo_barChartItem(index)
            assert_barChartHistoryItem_isDisplayed(
                date = item.getDisplayDate(Locale.getDefault(), rule.activity),
                timesDone = item.timesDone
            )
        }
    }

    fun assert_habitName_isDisplayed(name: String) = with(rule) {
        onNode(hasTestTag(activity.getString(R.string.test_tag_habit_details_title)) and hasText(name))
            .assertIsDisplayed()
    }

    fun assert_habitDescription_isDisplayed(description: String) = with(rule) {
        onNodeWithTag(activity.getString(R.string.test_tag_habit_details_description))
            .assertTextEquals(description)
    }

    fun assert_habitIconAndColor_isDisplayed(habitName: String, habitColor: Color, habitIconName: String) = with(rule) {
        onNode(
            hasTestTag(activity.getString(R.string.test_tag_habit_details_icon))
                    and SemanticsMatcher.expectValue(ColorRes, habitColor)
                    and hasAnyChild(hasTestTag(habitIconName))
        ).assertIsDisplayed()
    }

    fun assert_habitFrequency_isDisplayed(frequency: String) = with(rule) {
        onNode(
            matcher = hasTestTag(activity.getString(R.string.test_tag_habit_details_frequency)), useUnmergedTree = true
        )
            .assertIsDisplayed()
            .assert(hasAnyChild(hasText(frequency)))
    }

    fun assert_habitGoal_isDisplayed(goal: Int) = with(rule) {
        onNode(
            matcher = hasTestTag(activity.getString(R.string.test_tag_habit_details_goal)), useUnmergedTree = true
        ).assert(hasAnyChild(hasText(activity.getString(R.string.goal_value, goal))))
    }

    fun assert_habitReminder_isDisplayed(reminder: String) = with(rule) {
        onNode(
            matcher = hasTestTag(activity.getString(R.string.test_tag_habit_details_reminder)), useUnmergedTree = true
        ).assertIsDisplayed()
            .assert(hasAnyChild(hasText(reminder)))
    }

    fun assert_bestStreak_isDisplayed(streak: Int) = with(rule) {
        onNode(
            matcher = hasTestTag(activity.getString(R.string.test_tag_habit_details_best_streak)),
            useUnmergedTree = true
        ).assertIsDisplayed()
            .assert(hasAnyChild(hasText(streak.toString())))
    }

    fun assert_totalCompletedDays_isDisplayed(total: Int) = with(rule) {
        onNode(
            matcher = hasTestTag(activity.getString(R.string.test_tag_habit_details_total)), useUnmergedTree = true
        ).assertIsDisplayed()
            .assert(hasAnyChild(hasText(total.toString())))
    }

    fun assert_currentStreak_isDisplayed(streak: Int) = with(rule) {
        onNode(
            matcher = hasTestTag(activity.getString(R.string.test_tag_habit_details_current_streak)),
            useUnmergedTree = true
        ).assertIsDisplayed()
            .assert(hasAnyChild(hasText(streak.toString())))
    }

    fun assert_habitHistoryCalendar_isDisplayed() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_habit_history_switch_to_heatmap_view))
            .assertIsDisplayed()
    }

    fun assert_habitHistoryHeatmap_isDisplayed() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_habit_history_switch_to_calendar_view))
            .assertIsDisplayed()
    }

    fun click_switchToHeatmapView() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_habit_history_switch_to_heatmap_view))
            .performClick()
    }

    fun click_switchToCalendarView() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_habit_history_switch_to_calendar_view))
            .performClick()
    }

    fun click_editHistory() = with(rule) {
        onNodeWithText(activity.getString(R.string.edit_habit))
            .assertHasClickAction()
            .performClick()
    }

    fun click_editHistoryView() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.button_edit))
            .assertHasClickAction()
            .performClick()
    }

    fun assert_editHistoryDialogIsVisible() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_edit_history_dialog))
            .assertIsDisplayed()
    }

    fun click_entry(daysAgo: Int) = with(rule) {
        onNodeWithTag(activity.getString(R.string.test_tag_entry, daysAgo))
            .assertIsDisplayed()
            .performClick()
    }

    fun assert_entryTimesDone_equals(daysAgo: Int, timesDone: Int) = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_entry_item, daysAgo, timesDone))
            .assertIsDisplayed()
    }

    fun navigateCalendar(forward: Boolean) = with(rule) {
        val contentDescription =
            if (forward) activity.getString(R.string.content_description_next_month)
            else activity.getString(R.string.content_description_previous_month)

        onNodeWithContentDescription(contentDescription)
            .assertIsDisplayed()
            .performClick()
    }

    fun assert_calendarNavigation_isEnabled(forward: Boolean, isEnabled: Boolean) = with(rule) {
        val contentDescription =
            if (forward) activity.getString(R.string.content_description_next_month)
            else activity.getString(R.string.content_description_previous_month)

        onNodeWithContentDescription(contentDescription)
            .assertIsDisplayed()
            .assertHasClickAction()
            .apply { if (isEnabled) assertIsEnabled() else assertIsNotEnabled() }
    }

    fun swipe_calendarView(forward: Boolean) = with(rule) {
        val calendar =
            onNodeWithContentDescription(activity.getString(R.string.content_description_habit_history_calendar_view))
        calendar.assertIsDisplayed()

        if (forward)
            calendar.performTouchInput { swipeLeft() }
        else
            calendar.performTouchInput { swipeRight() }
    }

    fun assert_monthLabel_equals(month: String) = with(rule) {
        onNodeWithText(month)
            .assertIsDisplayed()
    }

    fun scrollTo_barChart() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_history_bar_chart))
            .performScrollTo()
    }

    fun assert_barChart_isVisible() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_history_bar_chart))
            .isDisplayed()
    }

    fun scrollTo_barChartItem(index: Int) = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_history_bar_chart))
            .performScrollToIndex(index)
    }

    fun assert_barChartHistoryItem_isDisplayed(date: String, timesDone: Int) = with(rule) {
        onNodeWithTag(activity.getString(R.string.test_tag_bar_chart_item, date, timesDone))
            .assertIsDisplayed()
    }

    fun click_barChartFrequencyDropdown() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_chart_frequency_dropdown))
            .assertIsDisplayed()
            .performClick()
    }

    fun select_barChartFrequency(frequency: ChartFrequencyUi) = with(rule) {
        onNodeWithText(activity.getString(frequency.label))
            .assertIsDisplayed()
            .performClick()
    }

    fun assert_barChartFrequency_isSelected(frequency: String) = with(rule) {
        onNodeWithContentDescription(
            activity.getString(R.string.content_description_chart_frequency_dropdown),
            useUnmergedTree = true
        )
            .assert(hasAnyChild(hasText(frequency)))
    }

    fun navigateBack() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.action_navigate_up))
            .performClick()
    }

    fun click_expandDropdownMenu() = with(rule) {
        onNodeWithContentDescription(activity.getString(R.string.content_description_expand_dropdown_menu))
            .performClick()
    }

    fun click_editHabit() = with(rule) {
        onNodeWithText(activity.getString(R.string.edit_habit))
            .performClick()
    }

    fun click_deleteHabit() = with(rule) {
        onNodeWithText(activity.getString(R.string.action_delete))
            .performClick()
    }
}