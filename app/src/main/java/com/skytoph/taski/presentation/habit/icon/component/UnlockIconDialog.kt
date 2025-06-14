@file:OptIn(ExperimentalMaterial3Api::class)

package com.skytoph.taski.presentation.habit.icon.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.component.LoadingItems
import com.skytoph.taski.presentation.habit.icon.IconsColors
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun UnlockIconDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
    icon: ImageVector,
    isLoading: Boolean = false,
    color: Color
) {
    AlertDialog(
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = AnnotatedString(text = stringResource(id = R.string.unlock_icon_description)),
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .size(size = 44.dp)
                        .background(color = color, shape = RoundedCornerShape(10)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(2.dp),
                        tint = Color.White
                    )
                }
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.unlock_icon_title),
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
                    Box(
                        modifier = Modifier
                            .heightIn(min = 40.dp)
                            .widthIn(min = 128.dp)
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                shape = MaterialTheme.shapes.small
                            )
                            .clip(MaterialTheme.shapes.small)
                            .clickable { onConfirm() }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .animateContentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Crossfade(
                            targetState = isLoading,
                            label = "loading_backup_time_crossfade",
                            animationSpec = tween(durationMillis = 150),
                        ) { isLoading ->
                            if (isLoading) LoadingItems()
                            else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.action_watch_ad),
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.play_filled),
                                        contentDescription = stringResource(id = R.string.unlock_icon_description),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
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
        UnlockIconDialog(icon = ImageVector.vectorResource(id = R.drawable.lock), color = IconsColors.Default)
    }
}

@Composable
@Preview
private fun LoadingUnlockIconDialogPreview() {
    HabitMateTheme(darkTheme = true) {
        UnlockIconDialog(
            icon = ImageVector.vectorResource(id = R.drawable.lock),
            color = IconsColors.Default,
            isLoading = true
        )
    }
}