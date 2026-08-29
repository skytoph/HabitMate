package com.skytoph.taski.test_habit.edit.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.skytoph.taski.mock.data.HabitColorData
import com.skytoph.taski.mock.data.HabitIconData
import com.skytoph.taski.presentation.MainActivity
import com.skytoph.taski.test_habit.edit.screen.HabitEditScreen
import com.skytoph.taski.test_habit.list.screen.HabitListScreen
import com.skytoph.taski.test_habit.util.HabitsUtils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@HiltAndroidTest
class CreateHabitTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun createHabit_titleIsEmpty() {
        val habitsScreen = HabitListScreen(rule)
        habitsScreen.clickCreateButton()

        val editHabitScreen = HabitEditScreen(rule)
        editHabitScreen.clickSave()
        editHabitScreen.emptyTitleErrorIsVisible()
    }

    @Test
    fun createHabit_titleIsBlank() {
        val habitsScreen = HabitListScreen(rule)
        habitsScreen.clickCreateButton()

        val editHabitScreen = HabitEditScreen(rule)
        editHabitScreen.setTitle(" ")
        editHabitScreen.clickSave()
        editHabitScreen.emptyTitleErrorIsVisible()
    }

    @Test
    fun createHabit_success() {
        val habitsScreen = HabitListScreen(rule)
        habitsScreen.clickCreateButton()

        val title = "habit_title"
        val color = HabitColorData.getRandom()
        val icon = HabitIconData.getRandom()
        HabitsUtils.createHabit(
            rule = rule,
            title = title,
            description = "habit_description",
            color = color,
            icon = icon
        )
        habitsScreen.assertCrashlyticsDialogIsVisible()
        habitsScreen.clickCrashlyticsDialog(confirm = false)
        habitsScreen.assertHabitIsVisible(
            habitName = title,
            habitColor = color,
            habitIconName = rule.activity.resources.getResourceEntryName(icon)
        )
    }

    @Test
    fun createHabit_multipleTimes() {
        val habitsScreen = HabitListScreen(rule)

        habitsScreen.assertStateIsEmpty()
        habitsScreen.clickCreateButton()

        val title1 = "habit_title_1"
        val color1 = HabitColorData.getRandom()
        val icon1 = HabitIconData.getRandom()
        HabitsUtils.createHabit(
            rule = rule,
            title = title1,
            description = "habit_description_1",
            color = color1,
            icon = icon1
        )
        habitsScreen.assertCrashlyticsDialogIsVisible()
        habitsScreen.clickCrashlyticsDialog(confirm = false)
        habitsScreen.assertHabitIsVisible(
            habitName = title1,
            habitColor = color1,
            habitIconName = rule.activity.resources.getResourceEntryName(icon1)
        )

        habitsScreen.clickCreateMenuItem()

        val title2 = "habit_title_2"
        val color2 = HabitColorData.getRandom()
        val icon2 = HabitIconData.getRandom()
        HabitsUtils.createHabit(
            rule = rule,
            title = title2,
            description = "habit_description_2",
            color = color2,
            icon = icon2
        )
        habitsScreen.assertHabitIsVisible(
            habitName = title2,
            habitColor = color2,
            habitIconName = rule.activity.resources.getResourceEntryName(icon2)
        )
    }
}