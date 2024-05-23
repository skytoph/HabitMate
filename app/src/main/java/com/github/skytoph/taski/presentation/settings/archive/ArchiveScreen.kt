package com.github.skytoph.taski.presentation.settings.archive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skytoph.taski.presentation.core.component.HabitTitleWithIcon
import com.github.skytoph.taski.presentation.habit.reorder.ReorderHabitsViewModel

@Composable
fun ArchiveScreen(viewModel: ReorderHabitsViewModel = hiltViewModel()) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(viewModel.habits().value) { habit ->
            HabitTitleWithIcon(
                modifier = Modifier.fillMaxWidth(),
                icon = habit.icon.vector(LocalContext.current),
                color = habit.color,
                title = habit.title
            )
        }
    }
}