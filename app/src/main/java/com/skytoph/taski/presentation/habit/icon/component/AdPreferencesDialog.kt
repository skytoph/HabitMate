@file:OptIn(ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.habit.icon.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun AdPreferencesDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    AlertDialog(
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = AnnotatedString(text = stringResource(id = R.string.manage_ad_options_description)),
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.manage_ad_options_title),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentEnforcement provides false,
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .heightIn(min = 40.dp)
                            .widthIn(min = 128.dp)
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                            .clip(MaterialTheme.shapes.small)
                            .clickable { onConfirm() }
                            .padding(horizontal = 24.dp, vertical = 6.dp)
                            .animateContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.action_manage_ad_options),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        },
        dismissButton = {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentEnforcement provides false,
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .heightIn(min = 40.dp)
                            .widthIn(min = 128.dp)
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                shape = MaterialTheme.shapes.small
                            )
                            .clip(MaterialTheme.shapes.small)
                            .clickable { onDismissRequest() }
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.action_cancel),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        },
        shape = MaterialTheme.shapes.medium,
    )
}

@Composable
@Preview
private fun UnlockIconDialogPreview() {
    HabitMateTheme {
        AdPreferencesDialog()
    }
}

@Composable
@Preview
private fun LoadingUnlockIconDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        AdPreferencesDialog()
    }
}