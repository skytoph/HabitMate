package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SquareButton(onClick: () -> Unit, label: String, size: Dp, isEnabled: Boolean) {
    TextButton(
        onClick = onClick,
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = Modifier.size(size),
        enabled = isEnabled
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onTertiary)
    }
}