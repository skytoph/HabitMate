package com.github.skytoph.taski.presentation.core.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorText(error: String?) {
    Text(
        text = error ?: "",
        maxLines = 3,
        minLines = 3,
        color = MaterialTheme.colorScheme.error
    )
}