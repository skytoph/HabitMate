package com.skytoph.taski.test_habit.util

import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.skytoph.taski.R
import com.skytoph.taski.presentation.habit.icon.IconsColors
import com.skytoph.taski.test_habit.edit.screen.HabitEditScreen
import com.skytoph.taski.test_habit.icon.screen.IconAndColorScreen

object HabitsUtils {

    fun <A : ComponentActivity> createHabit(
        rule: AndroidComposeTestRule<ActivityScenarioRule<A>, A>,
        title: String,
        description: String = "",
        goal: Int = 1,
        @DrawableRes icon: Int = R.drawable.star,
        color: Color = IconsColors.allColors.random()
    ) {
        val editHabitScreen = HabitEditScreen(rule)
        editHabitScreen.setTitle(title)
        editHabitScreen.setDescription(description)
        editHabitScreen.increaseGoalIsEnabled()
        editHabitScreen.decreaseGoalIsDisabled()
        repeat(goal - 1) { editHabitScreen.clickIncreaseGoal() }

        editHabitScreen.clickSelectIconAndColor()
        rule.waitForIdle()

        val iconScreen = IconAndColorScreen(rule)
        iconScreen.click_color(color)
        iconScreen.click_icon(icon)
        iconScreen.navigateBack()
        rule.waitForIdle()

        editHabitScreen.iconIsSelected(icon)
        editHabitScreen.colorIsSelected(color)
        editHabitScreen.clickSave()
        rule.waitForIdle()
    }
}