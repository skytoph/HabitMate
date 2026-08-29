package com.skytoph.taski.test_habit.list.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.skytoph.taski.core.datastore.settings.ViewType
import com.skytoph.taski.mock.data.HabitMockData
import com.skytoph.taski.presentation.habit.list.HabitListState
import com.skytoph.taski.presentation.habit.list.component.Habits
import com.skytoph.taski.test_habit.list.screen.HabitListScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@HiltAndroidTest
class HabitListTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private fun init(state: HabitListState, view: ViewType = ViewType.Daily()) {
        rule.setContent {
            Habits(state = state, view = view)
        }
    }

    @Test
    fun listOfHabits_init_isLoading() {
        val state = HabitListState()
        init(state = state, view = ViewType.Calendar())

        val screen = HabitListScreen(rule)

        screen.assertStateIsLoading()
    }

    @Test
    fun listOfHabits_isEmpty() {
        val state = HabitListState(habits = emptyList(), isLoading = false)
        init(state = state, view = ViewType.Calendar())

        val screen = HabitListScreen(rule)

        screen.assertStateIsEmpty()
    }

    @Test
    fun listOfHabits_viewCalendar() {
        val state = HabitListState(habits = HabitMockData.HABITS_WITH_HISTORY, isLoading = false)
        val view = ViewType.Calendar()
        init(state = state, view = view)

        val screen = HabitListScreen(rule)

        HabitMockData.HABITS_WITH_HISTORY.map { it.habit }.forEachIndexed { index, habit ->
            screen.scrollToIndex(index)
            screen.assertHabitIsVisible(
                habitName = habit.title,
                habitColor = habit.color,
                habitIconName = habit.icon.name(rule.activity.resources)
            )
            screen.assertCheckButtonIsVisible(habitName = habit.title, view = view)
        }
    }

    @Test
    fun listOfHabits_viewDaily() {
        val state = HabitListState(habits = HabitMockData.HABITS_WITH_HISTORY, isLoading = false)
        val view = ViewType.Daily()
        init(state = state, view = view)

        val screen = HabitListScreen(rule)

        HabitMockData.HABITS_WITH_HISTORY.map { it.habit }.forEachIndexed { index, habit ->
            screen.scrollToIndex(index)
            screen.assertHabitIsVisible(
                habitName = habit.title,
                habitColor = habit.color,
                habitIconName = habit.icon.name(rule.activity.resources)
            )
            screen.assertCheckButtonIsVisible(habitName = habit.title, view = view)
        }
    }
}