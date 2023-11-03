package com.github.skytoph.taski.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    errorResId: Int?,
    onVisibleClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = errorResId != null,
        supportingText = {
            Text(
                text = if (errorResId != null) stringResource(id = errorResId) else "",
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
        }
    )
}