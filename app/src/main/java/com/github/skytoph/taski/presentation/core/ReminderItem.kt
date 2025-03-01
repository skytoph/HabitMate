package com.github.skytoph.taski.presentation.core

import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.LoadingItems
import com.github.skytoph.taski.presentation.core.preview.IconLockedProvider
import com.github.skytoph.taski.presentation.habit.icon.IconsLockedGroup
import com.github.skytoph.taski.presentation.habit.icon.component.IconsWarning
import com.github.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun ReminderItem(
    modifier: Modifier = Modifier,
    confirm: () -> Unit,
    isLoading: Boolean,
    dismiss: () -> Unit,
    icon: ImageVector,
    text: String,
    buttonConfirm: String,
    background: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Column(
        modifier = modifier
            .background(color = background, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(enabled = !isLoading, onClick = dismiss)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = stringResource(R.string.action_dismiss),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            ButtonWithLoading(
                title = buttonConfirm,
                onClick = confirm,
                isLoading = isLoading,
                enabledColor = MaterialTheme.colorScheme.primary,
                disabledColor = MaterialTheme.colorScheme.secondaryContainer,
                enabled = !isLoading,
            )
        }
    }
}

@Composable
fun ButtonWithLoading(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    title: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    enabledColor: Color = MaterialTheme.colorScheme.primary,
    disabledColor: Color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
) {
    val color = remember { Animatable(if (isLoading) disabledColor else enabledColor) }
    LaunchedEffect(isLoading) {
        color.animateTo(
            targetValue = if (isLoading) disabledColor else enabledColor,
            animationSpec = tween(durationMillis = 400)
        )
    }
    Box(modifier = modifier
        .background(color = color.value, shape = MaterialTheme.shapes.large)
        .clip(MaterialTheme.shapes.small)
        .clickable(enabled = !isLoading && enabled) { onClick() }
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .animateContentSize(),
        contentAlignment = Alignment.Center) {
        Crossfade(
            targetState = isLoading,
            label = "reminder_button_crossfade",
            animationSpec = tween(durationMillis = 150)
        ) { load ->
            if (load) LoadingItems(spaceSize = 4.dp)
            else Text(
                text = title,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ReminderPreview(@PreviewParameter(IconLockedProvider::class) icons: List<IconsLockedGroup>) {
    HabitMateTheme(darkTheme = true) {
        IconsWarning(modifier = Modifier.fillMaxWidth())
    }
}