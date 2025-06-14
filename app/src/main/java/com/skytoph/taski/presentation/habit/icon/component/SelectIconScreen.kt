package com.skytoph.taski.presentation.habit.icon.component

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skytoph.taski.R
import com.skytoph.taski.presentation.core.color.borderColor
import com.skytoph.taski.presentation.core.component.AppBarAction
import com.skytoph.taski.presentation.core.component.LoadingFullscreen
import com.skytoph.taski.presentation.core.preview.IconLockedProvider
import com.skytoph.taski.presentation.core.state.IconResource
import com.skytoph.taski.presentation.core.state.StringResource
import com.skytoph.taski.presentation.habit.icon.IconMessages
import com.skytoph.taski.presentation.habit.icon.IconState
import com.skytoph.taski.presentation.habit.icon.IconsColors
import com.skytoph.taski.presentation.habit.icon.IconsLockedGroup
import com.skytoph.taski.presentation.habit.icon.SelectIconEvent
import com.skytoph.taski.presentation.habit.icon.SelectIconViewModel
import com.skytoph.taski.ui.theme.HabitMateTheme

@Composable
fun SelectIconScreen(viewModel: SelectIconViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state = viewModel.state()
    val iconState = viewModel.iconState()
    val settings = viewModel.settings().collectAsState()
    val actionColor = MaterialTheme.colorScheme.onBackground
    val errorColor = MaterialTheme.colorScheme.error
    val sortIcons = settings.value.sortIcons
    val getErrorColor = { errorColor.toArgb() }

    val actionSortIcons = AppBarAction(
        title = StringResource.ResId(R.string.icons_menu_item_unlocked_first),
        color = actionColor,
        onClick = { viewModel.onEvent(SelectIconEvent.UpdateSort()) },
        checked = sortIcons
    )

    LaunchedEffect(Unit) {
        viewModel.init(context.getActivity()!!)
    }

    LaunchedEffect(sortIcons) {
        viewModel.initAppBar(
            title = R.string.color_and_icon_label,
            dropDownItems = listOf(actionSortIcons.copy(checked = sortIcons))
        )
    }

    SelectIcon(
        state = iconState,
        icons = state.value.icons,
        isLoading = state.value.isLoading,
        isSigningIn = state.value.isSigningIn,
        isWarningShown = state.value.isWarningShown,
        onSelectColor = { viewModel.onEvent(SelectIconEvent.Update(color = it)) },
        onSelectIcon = { viewModel.onEvent(SelectIconEvent.Update(icon = it)) },
        onUnlockIcon = { viewModel.onEvent(SelectIconEvent.UpdateDialog(icon = it, isVisible = true)) },
        logIn = {
            if (viewModel.connected(context)) {
                viewModel.onEvent(SelectIconEvent.IsSigningIn(true))
                viewModel.signIn(context)
            } else viewModel.showMessage(IconMessages.noConnectionMessage)
        },
        dismiss = { viewModel.onEvent(SelectIconEvent.IsWarningDialogShown(true)) },
    )

    if (state.value.isDialogShown) state.value.dialogIcon?.let { icon ->
        UnlockIconDialog(
            icon = icon.vector(context),
            onConfirm = {
                viewModel.unlockIcon(
                    icon = icon,
                    activity = context.getActivity() ?: return@UnlockIconDialog,
                    getErrorColor = getErrorColor
                )
            },
            onDismissRequest = { viewModel.cancelAd() },
            isLoading = state.value.isDialogLoading,
            color = iconState.value.color
        )
    }

    if (state.value.isWarningDialogShown)
        IconWarningDialog(
            onDismissRequest = { viewModel.onEvent(SelectIconEvent.IsWarningDialogShown(false)) },
            onConfirm = { viewModel.hideWarning(it) },
        )

    if (state.value.isRewardErrorDialogShown)
        AdPreferencesDialog(
            onDismissRequest = { viewModel.onEvent(SelectIconEvent.IsRewardPreferencesDialogShown(false)) },
            onConfirm = { context.getActivity()?.let { viewModel.manageAdPreferences(it, getErrorColor) } },
        )
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@Composable
private fun SelectIcon(
    state: State<IconState>,
    icons: List<IconsLockedGroup>,
    isLoading: Boolean = false,
    isWarningShown: Boolean = true,
    isSigningIn: Boolean = false,
    onSelectColor: (Color) -> Unit = {},
    onSelectIcon: (IconResource) -> Unit = {},
    onUnlockIcon: (IconResource) -> Unit = {},
    logIn: () -> Unit = {},
    dismiss: () -> Unit = {},
    iconSize: Dp = 32.dp,
    iconPadding: Dp = 4.dp,
) {
    if (isLoading)
        LoadingFullscreen()
    else Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(max = 424.dp),
            columns = GridCells.Adaptive(iconSize + iconPadding),
            contentPadding = PaddingValues(iconPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = { "warning" }) {
                AnimatedVisibility(
                    visible = isWarningShown,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    IconsWarning(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        logIn = logIn,
                        dismiss = dismiss,
                        isLoading = isSigningIn
                    )
                }
            }
            item(
                span = { GridItemSpan(maxLineSpan) },
                contentType = { "title" }) {
                IconGroupLabel(stringResource(R.string.color), iconPadding)
            }
            items(IconsColors.allColors, contentType = { "color" }) { color ->
                ColorItem(
                    onSelectColor = onSelectColor,
                    color = color,
                    iconSize = iconSize,
                    isSelected = color == state.value.color
                )
            }
            icons.forEach { iconGroup ->
                item(
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = { "title" }) {
                    IconGroupLabel(stringResource(iconGroup.title), iconPadding)
                }
                items(iconGroup.icons, contentType = { "icon" }) { icon ->
                    val iconResource = IconResource.Id(icon.first)
                    IconItem(
                        modifier = Modifier.animateItem(),
                        icon = iconResource,
                        onSelectIcon = onSelectIcon,
                        onUnlockIcon = onUnlockIcon,
                        iconSize = iconSize,
                        isSelected = state.value.icon.matches(iconResource, LocalContext.current),
                        isUnlocked = icon.second,
                        color = state.value.color
                    )
                }
            }
        }
    }
}

@Composable
fun IconItem(
    modifier: Modifier = Modifier,
    icon: IconResource,
    onSelectIcon: (IconResource) -> Unit = { _ -> },
    onUnlockIcon: (IconResource) -> Unit = { _ -> },
    iconSize: Dp = 32.dp,
    isSelected: Boolean = false,
    isUnlocked: Boolean = false,
    color: Color = MaterialTheme.colorScheme.secondary,
) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val background = if (isSelected) color else
            if (isUnlocked) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            Icon(
                imageVector = icon.vector(context),
                contentDescription = icon.name(context.resources),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable { if (isUnlocked) onSelectIcon(icon) else onUnlockIcon(icon) }
                    .size(iconSize)
                    .background(background)
                    .padding(6.dp),
                tint = Color.White
            )
            if (!isUnlocked) Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.lock_small),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(12.dp)
                    .padding(2.dp)
            )
        }
    }
}

@Composable
fun IconGroupLabel(
    title: String,
    iconPadding: Dp = 8.dp
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = iconPadding),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
fun ColorItem(
    onSelectColor: (Color) -> Unit,
    color: Color,
    iconSize: Dp,
    isSelected: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onSelectColor(color) }
                .size(iconSize)
                .background(color = color, shape = CircleShape)
                .border(
                    2.dp,
                    if (isSelected) color.borderColor(MaterialTheme.colorScheme.onBackground)
                    else Color.Transparent,
                    CircleShape
                )
        )
    }
}

@Composable
@Preview(showBackground = true, locale = "uk")
fun SelectIconScreenPreview(@PreviewParameter(IconLockedProvider::class) icons: List<IconsLockedGroup>) {
    HabitMateTheme {
        val state = remember { mutableStateOf(IconState()) }
        SelectIcon(
            state = state,
            icons = icons,
            onSelectColor = { state.value = state.value.copy(color = it) },
            onSelectIcon = { state.value = state.value.copy(icon = it) },
            logIn = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkSelectIconScreenPreview(@PreviewParameter(IconLockedProvider::class) icons: List<IconsLockedGroup>) {
    HabitMateTheme(darkTheme = true) {
        val state = remember { mutableStateOf(IconState()) }
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SelectIcon(
                state = state,
                icons = icons,
                onSelectColor = { state.value = state.value.copy(color = it) },
                onSelectIcon = { state.value = state.value.copy(icon = it) },
                logIn = {}
            )
        }
    }
}