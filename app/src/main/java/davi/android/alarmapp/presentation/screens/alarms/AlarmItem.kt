package davi.android.alarmapp.presentation.screens.alarms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.SplitSwitchButton
import androidx.wear.compose.material3.SplitSwitchButtonColors
import androidx.wear.compose.material3.SwipeToReveal
import androidx.wear.compose.material3.SwipeToRevealDefaults
import androidx.wear.compose.material3.Text
import davi.android.alarmapp.R
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.presentation.navigation.RouteEditAlarm
import davi.android.alarmapp.presentation.screens.detailsAlarm.DayOfWeekIcon
import davi.android.alarmapp.presentation.viewmodel.AlarmsViewModel

@Composable
fun AlarmItem(
    alarmsViewModel: AlarmsViewModel,
    backStack: MutableList<Any>,
    alarm: Alarm,
    customSplitSwitchColors: SplitSwitchButtonColors
) {
    val state = remember { mutableStateOf(false) }

    LaunchedEffect(alarm.disabled) {
        state.value = alarm.disabled
    }

    SwipeToReveal(
        primaryAction = {
            PrimaryActionButton(
                onClick = { alarmsViewModel.deleteAlarm(alarm) },
                icon = {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.exclude)
                    )
                },
                text = {
                    Text(
                        stringResource(R.string.exclude),
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                modifier = Modifier.height(SwipeToRevealDefaults.LargeActionButtonHeight)
            )
        },
        onSwipePrimaryAction = {
        },
        undoPrimaryAction = {
            UndoActionButton(
                onClick = { },
                text = {
                    Text(
                        stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
            )
        },
    ) {
        SplitSwitchButton(
            label = {
                Column {
                    Text(
                        stringResource(R.string.alarm_time),
                        fontSize = 12.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        alarm.formattedTime(),
                        fontSize = 16.sp,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            },
            secondaryLabel = {
                if (alarm.repeatDays.isNotBlank())
                    Column(modifier = Modifier.padding(vertical = 5.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.Center, // Space between day icons
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            alarmsViewModel.listDays.subList(0, 4).forEachIndexed { index, initial ->
                                DayOfWeekIcon(
                                    dayInitial = initial.substring(0, 1),
                                    isSelected = alarm.repeatDays.split(",").contains(initial),
                                    modifier = Modifier.padding(horizontal = 1.2.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(3.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center, // Space between day icons
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            alarmsViewModel.listDays.subList(4, 7).forEachIndexed { index, initial ->
                                DayOfWeekIcon(
                                    dayInitial = initial.substring(0, 1),
                                    isSelected = alarm.repeatDays.split(",").contains(initial),
                                    modifier = Modifier.padding(horizontal = 1.2.dp)
                                )
                            }
                        }
                    }
            },
            checked = state.value,
            onCheckedChange = {
                if (it) {
                    alarmsViewModel.activateAlarm(alarm)
                } else {
                    alarmsViewModel.disable(alarm)
                }
            },
            colors = customSplitSwitchColors,
            toggleContentDescription = stringResource(R.string.description_switch_button_activate_alarm),
            onContainerClick = {
                backStack.add(RouteEditAlarm(alarm))
            },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        )
    }
}