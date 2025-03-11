package com.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun SquareButton(
    onClick: () -> Unit,
    icon: ImageVector,
    size: Dp = 48.dp,
    isEnabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    disabledContainerColor: Color = containerColor,
) {
    TextButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = Modifier.size(size),
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
        ),
        enabled = isEnabled
    ) {
        Icon(imageVector = icon, contentDescription = icon.name, modifier = Modifier.size(16.dp))
    }
}

@Composable
@Preview
fun SquareButtonPreview() {
    HabitMateTheme {
        Row {
            SquareButton({}, icon = Icons.Default.Remove, size = 60.dp, isEnabled = false)
            SquareButton({}, icon = Icons.Default.Add, size = 60.dp, isEnabled = true)
        }
    }
}

@Composable
@Preview
fun DarkSquareButtonPreview() {
    HabitMateTheme(darkTheme = true) {
        Row {
            SquareButton({}, icon = Icons.Default.Remove, size = 60.dp, isEnabled = false)
            SquareButton({}, icon = Icons.Default.Add, size = 60.dp, isEnabled = true)
        }
    }
}