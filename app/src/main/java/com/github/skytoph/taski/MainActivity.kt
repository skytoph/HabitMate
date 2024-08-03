package com.github.skytoph.taski

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.github.skytoph.taski.presentation.HabitMateApp
import com.github.skytoph.taski.ui.theme.HabitMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme = viewModel.settings().collectAsState().value.theme
            LaunchedEffect(viewModel) {
                viewModel.initState()
            }
            HabitMateTheme(theme = theme) {
                HabitMateApp()
            }
        }
    }
}