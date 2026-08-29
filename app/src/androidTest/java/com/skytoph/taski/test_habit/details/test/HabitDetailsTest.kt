package com.skytoph.taski.test_habit.details.test

import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.paging.PagingData
import com.skytoph.taski.core.datastore.settings.HabitHistoryView
import com.skytoph.taski.mock.data.HabitMockData
import com.skytoph.taski.presentation.habit.HabitUi
import com.skytoph.taski.presentation.habit.details.BarItemUi
import com.skytoph.taski.presentation.habit.details.ChartFrequencyUi
import com.skytoph.taski.presentation.habit.details.HabitDetailsState
import com.skytoph.taski.presentation.habit.details.HabitStatisticsUi
import com.skytoph.taski.presentation.habit.details.components.HabitDetails
import com.skytoph.taski.presentation.habit.edit.EditableHistoryUi
import com.skytoph.taski.test_habit.details.screen.HabitDetailsScreen
import com.skytoph.taski.ui.theme.HabitMateTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HabitDetailsTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val resources: Resources
        get() = rule.activity.resources

    fun init(
        state: HabitDetailsState = HabitDetailsState(),
        entries: List<EditableHistoryUi> = emptyList(),
        barChart: List<BarItemUi> = emptyList(),
        historyView: HabitHistoryView = HabitHistoryView.Calendar,
        barChartFrequency: ChartFrequencyUi = ChartFrequencyUi.Week
    ) {
        rule.setContent {
            HabitMateTheme {
                HabitDetails(
                    state = remember { mutableStateOf(state) },
                    entries = flowOf(PagingData.from(entries)),
                    barChartItems = flowOf(PagingData.from(barChart)),
                    habitHistoryView = historyView,
                    barChartFrequency = barChartFrequency,
                )
            }
        }
    }

    @Test
    fun habit_isDisplayed() {
        val habit: HabitUi = HabitMockData.HABIT
        val entries: List<EditableHistoryUi> = HabitMockData.HISTORY
        val barChart: List<BarItemUi> = HabitMockData.BAR_CHART
        val barChartFrequency = ChartFrequencyUi.Week
        val statistics = HabitStatisticsUi()

        init(
            state = HabitDetailsState(habit = habit, statistics = statistics),
            entries = entries,
            barChart = barChart,
            barChartFrequency = barChartFrequency
        )

        val screen = HabitDetailsScreen(rule)
        screen.habitDetails_isDisplayed(
            habit = habit,
            entries = entries,
            barChart = barChart,
            stats = statistics,
            barChartFrequency = barChartFrequency
        )
    }
}