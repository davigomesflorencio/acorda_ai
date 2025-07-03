package davi.android.alarmapp.presentation.screens.editDetailsAlarm

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.EdgeButtonSize
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SplitSwitchButton
import androidx.wear.compose.material3.SwitchButtonDefaults
import androidx.wear.compose.material3.Text
import davi.android.alarmapp.R
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.presentation.navigation.AlarmDaysWeekScreen
import davi.android.alarmapp.presentation.navigation.RouteRingtone
import davi.android.alarmapp.presentation.screens.detailsAlarm.DayOfWeekIcon
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import kotlinx.coroutines.launch

@Composable
fun EditDetailsAlarm(addAlarmViewModel: AddAlarmViewModel, backStack: SnapshotStateList<Any>, alarm: Alarm) {
    val scope = rememberCoroutineScope()
    val state = rememberScalingLazyListState()
    val horizontalPadding = LocalConfiguration.current.screenWidthDp.dp * 0.052f
    val verticalPadding = LocalConfiguration.current.screenHeightDp.dp * 0.16f

    var soundActive by remember { mutableStateOf(alarm.soundActive) }
    var alarmVibrate by remember { mutableStateOf(alarm.vibration) }
    var snoozeActive by remember { mutableStateOf(alarm.snoozeActive) }

    var ringtoneTitle by addAlarmViewModel.ringtoneTitle

    val customSplitSwitchColors = SwitchButtonDefaults.splitSwitchButtonColors(
        checkedContainerColor = MaterialTheme.colorScheme.secondary,
        uncheckedContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f),
        checkedContentColor = Color.Black,
        uncheckedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        checkedSplitContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        uncheckedSplitContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.2f),
        checkedThumbIconColor = Color.White,
        checkedThumbColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        checkedSecondaryContentColor = Color.Black,
        uncheckedSecondaryContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        checkedTrackColor = MaterialTheme.colorScheme.onSecondaryContainer,
        checkedTrackBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )

    LaunchedEffect(Unit) {
        val listDays = alarm.repeatDays.split(",")
        addAlarmViewModel.dayDomingo.value = listDays.contains("DOM")
        addAlarmViewModel.daySegunda.value = listDays.contains("SEG")
        addAlarmViewModel.dayTerca.value = listDays.contains("TER")
        addAlarmViewModel.dayQuarta.value = listDays.contains("QUA")
        addAlarmViewModel.dayQuinta.value = listDays.contains("QUI")
        addAlarmViewModel.daySexta.value = listDays.contains("SEX")
        addAlarmViewModel.daySabado.value = listDays.contains("SAB")
    }

    ScreenScaffold(
        scrollState = state,
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding),
        edgeButton = {
            EdgeButton(
                modifier = Modifier.scrollable(
                    state,
                    orientation = Orientation.Vertical,
                    reverseDirection = true,
                    overscrollEffect = rememberOverscrollEffect()
                ),
                onClick = {
                    addAlarmViewModel.addVibration.value = alarmVibrate
                    addAlarmViewModel.addSound.value = soundActive
                    addAlarmViewModel.addSnooze.value = snoozeActive
                    scope.launch {
                        addAlarmViewModel.updateAlarm(alarm)
                    }
                    backStack.remove(
                        davi.android.alarmapp.presentation.navigation.RouteEditDetailsAlarm(alarm)
                    )
                    backStack.removeLastOrNull()
                },
                buttonSize = EdgeButtonSize.Small,
                colors = ButtonDefaults.filledVariantButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                enabled = true
            ) {
                Text(
                    stringResource(R.string.save),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        })
    { contentPadding ->
        ScalingLazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup(),
            autoCentering = null,
            contentPadding = contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    stringResource(R.string.customize_alarm),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Text(
                    "${addAlarmViewModel.hour.intValue.toString().padStart(2, '0')}:${addAlarmViewModel.minute.intValue.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item {
                Card(modifier = Modifier.fillMaxSize()) {
                    Chip(
                        onClick = {
                            backStack.add(AlarmDaysWeekScreen)
                        },
                        label = {
                            Text(
                                stringResource(R.string.repeat),
                                color = Color.Black,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        secondaryLabel = {
                            if (addAlarmViewModel.days.isEmpty())
                                Text(
                                    stringResource(R.string.off),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center, // Space between day icons
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        addAlarmViewModel.listDays.subList(0, 4).forEachIndexed { index, initial ->
                                            DayOfWeekIcon(
                                                dayInitial = initial.substring(0, 1),
                                                isSelected = addAlarmViewModel.days.contains(initial),
                                                modifier = Modifier.padding(horizontal = 6.dp)
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(3.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.Center, // Space between day icons
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        addAlarmViewModel.listDays.subList(4, 7).forEachIndexed { index, initial ->
                                            DayOfWeekIcon(
                                                dayInitial = initial.substring(0, 1),
                                                isSelected = addAlarmViewModel.days.contains(initial),
                                                modifier = Modifier.padding(horizontal = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }

                        },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    HorizontalDivider()
                    SplitSwitchButton(
                        label = {
                            Text(
                                stringResource(R.string.sound),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        checked = soundActive,
                        onCheckedChange = {
                            soundActive = it
                        },
                        toggleContentDescription = stringResource(R.string.description_switch_button_activate_alarm),
                        onContainerClick = {
                        },
                        enabled = true,
                        colors = customSplitSwitchColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    HorizontalDivider()
                    Chip(
                        onClick = {
                            backStack.add(RouteRingtone)
                        },
                        label = {
                            Text(
                                stringResource(R.string.select_alarm_sound),
                                color = Color.Black,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        secondaryLabel = {
                            Text(
                                ringtoneTitle ?: stringResource(R.string.silent_or_none),
                                color = Color.Black,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    HorizontalDivider()
                    SplitSwitchButton(
                        label = {
                            Text(
                                stringResource(R.string.vibration),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        checked = alarmVibrate,
                        onCheckedChange = {
                            alarmVibrate = it
                        },
                        toggleContentDescription = stringResource(R.string.description_switch_button_activate_alarm),
                        onContainerClick = {
                        },
                        enabled = true,
                        colors = customSplitSwitchColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    HorizontalDivider()
                    SplitSwitchButton(
                        label = {
                            Text(
                                stringResource(R.string.snooze),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        secondaryLabel = {
                            Text(
                                stringResource(R.string.minutes_3_times),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        checked = snoozeActive,
                        onCheckedChange = {
                            snoozeActive = it
                        },
                        toggleContentDescription = stringResource(R.string.description_switch_button_activate_alarm),
                        onContainerClick = {
                        },
                        enabled = true,
                        colors = customSplitSwitchColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}