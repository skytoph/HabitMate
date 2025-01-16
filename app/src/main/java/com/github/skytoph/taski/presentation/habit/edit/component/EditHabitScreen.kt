@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.skytoph.taski.presentation.habit.edit.component

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.github.skytoph.taski.R
import com.github.skytoph.taski.presentation.core.component.AppBarAction
import com.github.skytoph.taski.presentation.core.component.NotificationPermissionDialog
import com.github.skytoph.taski.presentation.core.component.SquareButton
import com.github.skytoph.taski.presentation.core.component.TextFieldWithError
import com.github.skytoph.taski.presentation.core.component.TimePickerDialog
import com.github.skytoph.taski.presentation.core.component.getLocale
import com.github.skytoph.taski.presentation.core.state.FieldState
import com.github.skytoph.taski.presentation.core.state.IconResource
import com.github.skytoph.taski.presentation.habit.ReminderUi
import com.github.skytoph.taski.presentation.habit.create.GoalState
import com.github.skytoph.taski.presentation.habit.edit.EditHabitEvent
import com.github.skytoph.taski.presentation.habit.edit.EditHabitState
import com.github.skytoph.taski.presentation.habit.edit.EditHabitViewModel
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyCustomType
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyState
import com.github.skytoph.taski.presentation.habit.edit.frequency.FrequencyUi
import com.github.skytoph.taski.presentation.habit.list.component.DialogItem
import com.github.skytoph.taski.ui.theme.HabitMateTheme


@Composable
fun EditHabitScreen(
    viewModel: EditHabitViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onSelectIconClick: () -> Unit
) {
    val context = LocalContext.current

    val actionColor = MaterialTheme.colorScheme.onSurface
    LaunchedEffect(viewModel.state().value.isLoading) {
        if (viewModel.state().value.isLoading) return@LaunchedEffect
        val actionSave =
            AppBarAction.save.copy(color = actionColor, onClick = { viewModel.validate() })
        viewModel.initAppBar(
            title = R.string.edit_habit,
            menuItems = listOf(actionSave)
        )
    }

    val iconState = remember { viewModel.iconState() }
    LaunchedEffect(iconState.value) {
        viewModel.onEvent(EditHabitEvent.UpdateIcon(iconState.value.icon, iconState.value.color))
    }
    val validated = viewModel.state().value.isValidated
    LaunchedEffect(validated) {
        if (validated) viewModel.saveHabit(navigateUp = navigateUp, context = context)
    }

    EditHabit(
        state = viewModel.state(),
        onSelectIconClick = onSelectIconClick,
        onTypeTitle = { viewModel.onEvent(EditHabitEvent.EditTitle(it)) },
        onTypeDescription = { viewModel.onEvent(EditHabitEvent.EditDescription(it)) },
        onDecreaseGoal = { viewModel.onEvent(EditHabitEvent.DecreaseGoal) },
        onIncreaseGoal = { viewModel.onEvent(EditHabitEvent.IncreaseGoal) },
        expandFrequency = { viewModel.onEvent(EditHabitEvent.ExpandFrequency) },
        increaseTimes = { viewModel.onEvent(EditHabitEvent.IncreaseFrequencyTimes) },
        decreaseTimes = { viewModel.onEvent(EditHabitEvent.DecreaseFrequencyTimes) },
        increaseType = { viewModel.onEvent(EditHabitEvent.IncreaseFrequencyType) },
        decreaseType = { viewModel.onEvent(EditHabitEvent.DecreaseFrequencyType) },
        selectType = { viewModel.onEvent(EditHabitEvent.SelectFrequency(it)) },
        selectDay = { viewModel.onEvent(EditHabitEvent.SelectDay(it)) },
        selectCustomType = { viewModel.onEvent(EditHabitEvent.SelectCustomType(it)) },
        expandType = { viewModel.onEvent(EditHabitEvent.ExpandCustomType) },
        switchOn = { viewModel.onEvent(EditHabitEvent.UpdateReminder(switchOn = it)) },
        showDialog = { viewModel.onEvent(EditHabitEvent.UpdateReminder(showDialog = it)) },
        updateReminder = { hour, minute ->
            viewModel.onEvent(
                EditHabitEvent.UpdateReminder(hour = hour, minute = minute, showDialog = false)
            )
        },
        showPermissionDialog = { viewModel.onEvent(EditHabitEvent.ShowPermissionDialog(it)) },
        isFirstDaySunday = viewModel.settings().collectAsState().value.weekStartsOnSunday.value
    )
}

@Composable
private fun EditHabit(
    state: State<EditHabitState>,
    onSelectIconClick: () -> Unit = {},
    onTypeTitle: (String) -> Unit = {},
    onTypeDescription: (String) -> Unit = {},
    onDecreaseGoal: () -> Unit = {},
    onIncreaseGoal: () -> Unit = {},
    expandFrequency: () -> Unit = {},
    increaseTimes: () -> Unit = {},
    decreaseTimes: () -> Unit = {},
    increaseType: () -> Unit = {},
    decreaseType: () -> Unit = {},
    selectType: (FrequencyUi) -> Unit = {},
    selectDay: (Int) -> Unit = {},
    selectCustomType: (FrequencyCustomType) -> Unit = {},
    expandType: () -> Unit = {},
    switchOn: (Boolean) -> Unit = {},
    showDialog: (Boolean) -> Unit = {},
    updateReminder: (Int, Int) -> Unit = { _, _ -> },
    showPermissionDialog: (DialogItem?) -> Unit = {},
    isFirstDaySunday: Boolean = false,
) {
    EditBaseHabit(
        title = state.value.title,
        description = state.value.description,
        goal = state.value.goal,
        icon = state.value.icon,
        color = state.value.color,
        reminder = state.value.reminder,
        dialog = state.value.dialog,
        onTypeTitle = onTypeTitle,
        onTypeDescription = onTypeDescription,
        onSelectIconClick = onSelectIconClick,
        onDecreaseGoal = onDecreaseGoal,
        onIncreaseGoal = onIncreaseGoal,
        frequency = state.value.frequencyState,
        isFrequencyExpanded = state.value.isFrequencyExpanded,
        expandFrequency = expandFrequency,
        increaseTimes = increaseTimes,
        decreaseTimes = decreaseTimes,
        increaseType = increaseType,
        decreaseType = decreaseType,
        selectType = selectType,
        selectDay = selectDay,
        selectCustomType = selectCustomType,
        expandType = expandType,
        typeExpanded = state.value.isCustomTypeExpanded,
        switchOn = switchOn,
        showTimeDialog = showDialog,
        showPermissionDialog = showPermissionDialog,
        updateReminder = updateReminder,
        isFirstDaySunday = isFirstDaySunday
    )
}

@Composable
fun EditBaseHabit(
    title: FieldState,
    description: FieldState,
    goal: GoalState,
    icon: IconResource,
    color: Color,
    reminder: ReminderUi,
    dialog: DialogItem?,
    onTypeTitle: (String) -> Unit,
    onTypeDescription: (String) -> Unit,
    onSelectIconClick: () -> Unit,
    onDecreaseGoal: () -> Unit,
    onIncreaseGoal: () -> Unit,
    minHeight: Dp = 44.dp,
    frequency: FrequencyState = FrequencyState(),
    isFrequencyExpanded: Boolean = true,
    expandFrequency: () -> Unit = {},
    increaseTimes: () -> Unit,
    decreaseTimes: () -> Unit,
    increaseType: () -> Unit,
    decreaseType: () -> Unit,
    selectType: (FrequencyUi) -> Unit,
    selectDay: (Int) -> Unit = {},
    selectCustomType: (FrequencyCustomType) -> Unit = {},
    expandType: () -> Unit = {},
    typeExpanded: Boolean = true,
    switchOn: (Boolean) -> Unit,
    showTimeDialog: (Boolean) -> Unit,
    showPermissionDialog: (DialogItem?) -> Unit,
    updateReminder: (Int, Int) -> Unit,
    isFirstDaySunday: Boolean,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(R.string.habit_label),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.icon_label),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.widthIn(min = minHeight)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TextFieldWithError(
                modifier = Modifier.weight(1f),
                value = title.field,
                onValueChange = onTypeTitle,
                error = title.error?.getString(context),
                height = minHeight,
                clearFocus = { focusManager.clearFocus() },
                title = "",
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
            IconSelector(
                icon = icon,
                color = color,
                size = minHeight,
                onClick = { focusManager.clearFocus(); onSelectIconClick() }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        TextFieldWithError(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            value = description.field,
            onValueChange = onTypeDescription,
            height = minHeight,
            clearFocus = { focusManager.clearFocus() },
            title = stringResource(R.string.description_label),
            singleLine = false,
            maxLines = 5,
            keyboardOptions = KeyboardOptions.Default,
            keyboardActions = KeyboardActions.Default
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.goal_label),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(horizontal = 16.dp)
                    .height(minHeight)
                    .wrapContentHeight()
                    .weight(1f)
            ) {
                AnimatedContent(
                    targetState = goal.value,
                    transitionSpec = {
                        if (initialState == EditHabitState.goalIsNotInitialized || targetState == EditHabitState.goalIsNotInitialized)
                            fadeIn(tween(durationMillis = 0)) togetherWith fadeOut(tween(durationMillis = 0))
                        else if (targetState > initialState)
                            slideInVertically { -it } togetherWith slideOutVertically { it }
                        else
                            slideInVertically { it } togetherWith slideOutVertically { -it }
                    },
                    label = "goal_counter_anim"
                ) { count ->
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = " " + stringResource(R.string.goal_in_day),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            SquareButton(
                onClick = { focusManager.clearFocus(); onDecreaseGoal() },
                icon = Icons.Default.Remove,
                size = minHeight,
                isEnabled = goal.canBeDecreased
            )
            SquareButton(
                onClick = { focusManager.clearFocus(); onIncreaseGoal() },
                icon = Icons.Default.Add,
                size = minHeight,
                isEnabled = goal.canBeIncreased
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.frequency),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        EditFrequency(
            frequency = frequency,
            expanded = isFrequencyExpanded,
            expand = { focusManager.clearFocus(); expandFrequency() },
            minHeight = minHeight,
            selectType = selectType,
            increaseTimes = increaseTimes,
            decreaseTimes = decreaseTimes,
            increaseType = increaseType,
            decreaseType = decreaseType,
            selectDay = selectDay,
            selectCustomType = selectCustomType,
            expandType = expandType,
            typeExpanded = typeExpanded,
            isFirstDaySunday = isFirstDaySunday
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.reminder),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        EditReminder(
            minHeight = minHeight,
            reminder = reminder,
            switchOn = { focusManager.clearFocus(); switchOn(it) },
            showTimeDialog = { focusManager.clearFocus(); showTimeDialog(it) },
            requestPermissionDialog = { focusManager.clearFocus(); showPermissionDialog(it) }
        )
        Spacer(modifier = Modifier.height(80.dp))
    }
    if (reminder.isDialogShown)
        TimePickerDialog(
            onDismissRequest = { showTimeDialog(false) },
            onConfirm = updateReminder,
            initialHour = reminder.hour,
            initialMinute = reminder.minute,
        )
    dialog?.let {
        NotificationPermissionDialog(dialog)
    }
}

@Composable
private fun EditReminder(
    minHeight: Dp,
    reminder: ReminderUi,
    switchOn: (Boolean) -> Unit,
    showTimeDialog: (Boolean) -> Unit,
    requestPermissionDialog: (DialogItem?) -> Unit,
) {
    RequestNotificationPermission(
        requestPermissionDialog = requestPermissionDialog,
        permissionGranted = switchOn,
        content = { checkPermission ->
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(end = 16.dp)
                    .height(minHeight)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Crossfade(
                    targetState = reminder.switchedOn,
                    label = "reminder_crossfade",
                ) { isReminderOn ->
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.extraSmall)
                            .clickable(
                                enabled = isReminderOn,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { showTimeDialog(true) })
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = reminder.formatted(getLocale(), stringResource(R.string.reminder_none)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isReminderOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
                Switch(
                    colors = SwitchDefaults.colors(
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                        uncheckedThumbColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    checked = reminder.switchedOn,
                    onCheckedChange = {
                        if (!reminder.switchedOn)
                            checkPermission()
                        else
                            switchOn(false)
                    },
                )
            }
        }
    )
}

@Composable
fun RequestNotificationPermission(
    requestPermissionDialog: (DialogItem?) -> Unit,
    permissionGranted: (Boolean) -> Unit = {},
    initialize: Boolean = true,
    content: @Composable (requestPermission: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val lifecycleState: State<Lifecycle.State> =
        LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val lifecycleScope = rememberCoroutineScope()
    var notificationEnabled: Boolean? by remember { mutableStateOf(null) }
    var alarmEnabled: Boolean? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(RequestPermission()) { isEnabled ->
        if (isEnabled) {
            notificationEnabled = true
            alarmEnabled = areAlarmsEnabled(context)
        }
    }
    val notificationLauncher = LaunchNotificationSettingsScreen(
        handleResult = { isEnabled ->
            if (isEnabled) {
                notificationEnabled = true
                requestPermissionDialog(null)
                alarmEnabled = areAlarmsEnabled(context)
            }
        },
        context = context
    )
    val alarmLauncher = LaunchAlarmSettingsScreen(
        handleResult = { isEnabled ->
            if (isEnabled) {
                alarmEnabled = true
                requestPermissionDialog(null)
                notificationEnabled = areNotificationsEnabled(context)
            }
        },
        context = context
    )

    val notificationDialog = remember {
        DialogItem.notification.copy(
            onConfirm = {
                startNotificationSettingsActivity(lifecycleScope, context, notificationLauncher)
            }, onDismiss = { requestPermissionDialog(null) }
        )
    }
    val alarmDialog = remember {
        DialogItem.alarm.copy(
            onConfirm = { startAlarmSettingsActivity(lifecycleScope, context, alarmLauncher) },
            onDismiss = { requestPermissionDialog(null) }
        )
    }
    val askForNotificationPermission = {
        askForNotificationPermission(
            launcher = launcher,
            showDialog = { requestPermissionDialog(notificationDialog) },
            lifecycleState = lifecycleState
        )
    }
    if (initialize) LaunchedEffect(Unit) {
        if (!areNotificationsEnabled(context) || !areAlarmsEnabled(context))
            permissionGranted(false)
    }
    LaunchedEffect(notificationEnabled, alarmEnabled) {
        when {
            notificationEnabled == false -> askForNotificationPermission()
            alarmEnabled == false -> requestPermissionDialog(alarmDialog)
            alarmEnabled == true && notificationEnabled == true -> permissionGranted(true)
        }
    }

    content(requestPermission = {
        checkPermission(
            context = context,
            requestAlarmPermission = { requestPermissionDialog(alarmDialog) },
            requestNotificationPermission = { askForNotificationPermission() },
            onGranted = { permissionGranted(true) })
    })
}

fun isPermissionNeeded(context: Context): Boolean = !areNotificationsEnabled(context) || !areAlarmsEnabled(context)

@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    icon: IconResource,
    color: Color,
    size: Dp,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier.width(size)) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .background(color = color, shape = MaterialTheme.shapes.extraSmall)
                .size(size)
        ) {
            Icon(
                imageVector = icon.vector(context),
                contentDescription = icon.name(context.resources),
                modifier = Modifier.size(36.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
@Preview
private fun DarkHabitScreenPreview() {
    HabitMateTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            EditHabit(state = remember { mutableStateOf(EditHabitState()) })
        }
    }
}