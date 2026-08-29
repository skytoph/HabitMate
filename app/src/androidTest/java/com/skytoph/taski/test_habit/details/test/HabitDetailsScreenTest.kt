package com.skytoph.taski.test_habit.details.test

import android.content.res.Resources
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.skytoph.taski.R
import com.skytoph.taski.mock.data.HabitColorData
import com.skytoph.taski.mock.data.HabitIconData
import com.skytoph.taski.presentation.MainActivity
import com.skytoph.taski.test_habit.details.screen.HabitDetailsScreen
import com.skytoph.taski.test_habit.list.screen.HabitListScreen
import com.skytoph.taski.test_habit.util.HabitsUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HabitDetailsScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    private val resources: Resources
        get() = rule.activity.resources

    @Test
    fun habitDetails_isDisplayed() {
        navigateTo_habitDetails()
    }

    private fun navigateTo_habitDetails() {
        val habitsScreen = HabitListScreen(rule)
        habitsScreen.clickCreateButton()

        val title = "habit_title"
        val description = "description"
        val icon = HabitIconData.getRandom()
        val color = HabitColorData.getRandom()
        val goal = 1
        HabitsUtils.createHabit(
            rule = rule,
            title = title,
            description = description,
            goal = goal,
            icon = icon,
            color = color
        )

        habitsScreen.wait_titleLabel_isDisplayed()
        habitsScreen.assertCrashlyticsDialogIsVisible()
        habitsScreen.clickCrashlyticsDialog(false)
        habitsScreen.assertHabitIsVisible(title)
        habitsScreen.clickHabit(title, icon, color)

        val habitDetailsScreen = HabitDetailsScreen(rule)

        habitDetailsScreen.wait_titleLabel_isDisplayed()

        habitDetailsScreen.assert_habitName_isDisplayed(title)
        habitDetailsScreen.assert_habitDescription_isDisplayed(description)
        habitDetailsScreen.assert_habitIconAndColor_isDisplayed(
            habitName = title,
            habitColor = color,
            habitIconName = resources.getResourceEntryName(icon)
        )

        habitDetailsScreen.assert_habitFrequency_isDisplayed(frequency = resources.getString(R.string.everyday))
        habitDetailsScreen.assert_habitGoal_isDisplayed(goal)
        habitDetailsScreen.assert_habitReminder_isDisplayed(resources.getString(R.string.reminder_turned_off))

        habitDetailsScreen.assert_totalCompletedDays_isDisplayed(0)
        habitDetailsScreen.assert_bestStreak_isDisplayed(0)
        habitDetailsScreen.assert_currentStreak_isDisplayed(0)

        habitDetailsScreen.assert_habitHistoryCalendar_isDisplayed()
        habitDetailsScreen.click_switchToHeatmapView()
        habitDetailsScreen.assert_habitHistoryHeatmap_isDisplayed()

        habitDetailsScreen.click_editHistory()
        habitDetailsScreen.assert_entryTimesDone_equals(daysAgo = 0, timesDone = 0)
        habitDetailsScreen.click_entry(daysAgo = 0)
        habitDetailsScreen.assert_entryTimesDone_equals(daysAgo = 0, timesDone = 1)
    }
}