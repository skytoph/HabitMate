package com.github.skytoph.taski.presentation.settings.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SettingsMenuScreen(archiveClick: () -> Unit) {
    Column {
        Button(onClick = archiveClick) {
            Text(text = "archive")
        }
        Divider()
    }
}