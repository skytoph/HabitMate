package com.github.skytoph.taski

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.skytoph.taski.presentation.HabitMateApp
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitMateTheme {
                HabitMateApp()
            }
        }
    }
}