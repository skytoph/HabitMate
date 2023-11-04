package com.github.skytoph.taski

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.skytoph.taski.presentation.TaskiApp
import com.github.skytoph.taski.ui.theme.TaskiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskiTheme {
                    TaskiApp()
                }
            }
    }
}