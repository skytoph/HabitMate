package com.github.skytoph.taski.presentation.core.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R

@Composable
fun BasicAuthTextField(
    value: String,
    error: String?,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = R.string.email)) },
        singleLine = true,
        maxLines = 1,
        isError = error != null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        supportingText = {
            Text(
                text = error ?: "",
                minLines = 2,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
        )
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun TextFieldPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        BasicAuthTextField(
            value = "value",
            error = stringResource(R.string.error_password_should_contain_numeric_character),
            onValueChange = {}
        )
    }
}