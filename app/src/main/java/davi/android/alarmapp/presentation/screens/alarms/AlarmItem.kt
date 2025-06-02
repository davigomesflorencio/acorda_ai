package davi.android.alarmapp.presentation.screens.alarms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.SplitSwitchButton
import androidx.wear.compose.material3.SplitSwitchButtonColors
import androidx.wear.compose.material3.Text
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.presentation.viewmodel.AlarmsViewModel

@Composable
fun AlarmItem(
    alarmsViewModel: AlarmsViewModel,
    alarm: Alarm,
    showTimePicker: MutableState<Boolean>,
    customSplitSwitchColors: SplitSwitchButtonColors
) {
    val state = remember { mutableStateOf(false) }

    LaunchedEffect(alarm) {
        state.value = alarm.disabled
    }

    SplitSwitchButton(
        label = {
            Column {
                Text("Horario do alarme", fontSize = 10.sp)
                Text(alarm.formattedTime(), fontSize = 10.sp)
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
        toggleContentDescription = "Split Switch Button Sample",
        onContainerClick = {
            if (alarm.disabled) {
                alarmsViewModel.activateAlarm(alarm)
            } else {
                alarmsViewModel.disable(alarm)
            }
//            showTimePicker.value = true
        },
        enabled = true,
        modifier = Modifier.fillMaxWidth()
    )
}