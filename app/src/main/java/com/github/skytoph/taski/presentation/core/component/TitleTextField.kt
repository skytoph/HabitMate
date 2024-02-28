package com.github.skytoph.taski.presentation.core.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R

@Composable
fun TitleTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.habit_label),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            shape = RoundedCornerShape(10),
            isError = error != null,
            supportingText = {
                AnimatedContent(
                    targetState = error,
                    transitionSpec = {
                        slideInVertically().togetherWith(fadeOut())
                    },
                    label = ""
                ) { target ->
                    Text(text = target ?: "", minLines = 1)
                }
            },
        )
    }
}