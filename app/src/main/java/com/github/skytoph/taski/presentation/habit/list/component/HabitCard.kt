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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.create.GreenBright
import com.github.skytoph.taski.ui.theme.TaskiTheme

@Composable
fun HabitCard(modifier: Modifier = Modifier, onDone: () -> Unit, habit: HabitUi) {
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
                    val color = ColorUtils.blendARGB(
                        defaultColor.toArgb(), habit.color.toArgb(), habit.todayDonePercent
                    )
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = Color(color),
                                shape = RoundedCornerShape(30)
                            )
                            .padding(4.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            HabitCalendar(Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp), habit)
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HabitCardPreview() {
    TaskiTheme(darkTheme = true) {
        val habit = HabitUi(
            0, "dev", 1, Icons.Outlined.Code, GreenBright, emptyMap(), 349
        )
        HabitCard(habit = habit, onDone = {})
    }
}