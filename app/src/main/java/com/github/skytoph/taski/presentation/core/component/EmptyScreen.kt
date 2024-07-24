package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun EmptyScreen(
    title: String,
    icon: ImageVector,
    button: @Composable () -> Unit = {}
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            button()
        }
    }
}

@Composable
@Preview
fun EmptyScreenPreview() {
    HabitMateTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            EmptyScreen(
                title = stringResource(R.string.list_of_habits_is_empty_create_label),
                icon = ImageVector.vectorResource(R.drawable.sparkles),
                button = {
                    ButtonWithIconOnBackground(
                        modifier = Modifier.padding(top = 4.dp),
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        title = "Create",
                        icon = Icons.Default.Add,
                        color = Color.White
                    )
                }
            )
        }
    }
}