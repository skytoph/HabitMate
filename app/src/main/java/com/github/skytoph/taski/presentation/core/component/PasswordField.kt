package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun PasswordField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "password",
    isVisible: Boolean = false,
    error: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onVisibleClick: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        isError = error != null,
        supportingText = {
            Text(
                text = error ?: "",
                minLines = 2
            )
        },
        trailingIcon = {
            val image = if (isVisible) Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff
            val description = if (isVisible) "Hide password" else "Show password"
            IconButton(onClick = onVisibleClick) {
                Icon(imageVector = image, description)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
            focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordFieldPreview() {
    HabitMateTheme {
        PasswordField()
    }
}

@Preview(showBackground = true)
@Composable
fun DarkPasswordFieldPreview() {
    HabitMateTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            PasswordField()
        }
    }
}