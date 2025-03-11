package com.skytoph.taski.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.skytoph.taski.ui.theme.HabitMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(this)

        setContent {
            val settings = viewModel.settings().collectAsState().value
            LaunchedEffect(viewModel) {
                viewModel.initState()
            }
            HabitMateTheme(theme = settings.theme) {
                HabitMateApp()
            }
        }
    }
}