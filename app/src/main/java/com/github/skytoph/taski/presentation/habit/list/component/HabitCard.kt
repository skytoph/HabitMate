package com.github.skytoph.taski.presentation.habit.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.presentation.core.habitColor
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.icon.GreenBright
import com.github.skytoph.taski.presentation.habit.list.EntryUi
import com.github.skytoph.taski.presentation.habit.list.HistoryUi
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun HabitCard(
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
    habit: HabitUi,
    history: HistoryUi,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(Modifier.padding(8.dp)) {
                    Icon(
                        imageVector = habit.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(30)
                            )
                            .padding(4.dp),
                        tint = habit.color
                    )
                }
                Text(text = habit.title, Modifier.weight(1f))
                IconButton(onClick = onDone) {
                    val defaultColor = MaterialTheme.colorScheme.secondaryContainer
                    val colorPercent =
                        history.entries.getOrNull(history.todayPosition)?.colorPercent
                    val color = habitColor(colorPercent ?: 0F, defaultColor, habit.color)
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = color,
                                shape = RoundedCornerShape(30)
                            )
                            .padding(4.dp),
                        tint = Color.White
                    )
                }
            }
            HabitCalendar(
                Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                habit.color,
                history
            )
        }
    }
}

private val history = HistoryUi((0..360).map { EntryUi() }.toList())

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitCardPreview() {
    TaskiTheme {
        val habit = HabitUi(0, "dev", 1, Icons.Outlined.Code, GreenBright)
        HabitCard(habit = habit, history = history, onDone = {})
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DarkHabitCardPreview() {
    TaskiTheme(darkTheme = true) {
        val habit = HabitUi(0, "dev", 1, Icons.Outlined.Code, GreenBright)
        HabitCard(habit = habit, history = history, onDone = {})
    }
}