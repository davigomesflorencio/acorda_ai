package davi.android.alarmapp.presentation.screens.editAlarm

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.wear.compose.material3.TimePicker
import androidx.wear.compose.material3.TimePickerDefaults
import davi.android.alarmapp.R
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.presentation.navigation.RouteEditDetailsAlarm
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import java.time.LocalTime

@Composable
fun EditAlarm(
    addAlarmViewModel: AddAlarmViewModel,
    backStack: SnapshotStateList<Any>,
    alarm: Alarm
) {
    val context = LocalContext.current
    var timePickerTime by remember {
        mutableStateOf(
            LocalTime.of(alarm.hour, alarm.min)
        )
    }

    val sunString = stringResource(R.string.sun)
    val monString = stringResource(R.string.mon)
    val tueString = stringResource(R.string.tue)
    val wedString = stringResource(R.string.wed)
    val thuString = stringResource(R.string.thu)
    val friString = stringResource(R.string.fri)
    val satString = stringResource(R.string.sat)


    TimePicker(
        onTimePicked = {
            timePickerTime = it
            addAlarmViewModel.hour.intValue = it.hour
            addAlarmViewModel.minute.intValue = it.minute
            addAlarmViewModel.days.clear()

            val listDays = alarm.repeatDays.split(",")
            listDays.forEach { day ->
                addAlarmViewModel.addToSelectedDays(day)
            }
            addAlarmViewModel.dayDomingo.value = listDays.contains(sunString)
            addAlarmViewModel.daySegunda.value = listDays.contains(monString)
            addAlarmViewModel.dayTerca.value = listDays.contains(tueString)
            addAlarmViewModel.dayQuarta.value = listDays.contains(wedString)
            addAlarmViewModel.dayQuinta.value = listDays.contains(thuString)
            addAlarmViewModel.daySexta.value = listDays.contains(friString)
            addAlarmViewModel.daySabado.value = listDays.contains(satString)

            addAlarmViewModel.addVibration.value = alarm.vibration
            addAlarmViewModel.addSound.value = alarm.soundActive
            addAlarmViewModel.addSnooze.value = alarm.snoozeActive

            addAlarmViewModel.ringtoneTitle.value = addAlarmViewModel.getRingtoneTitle(context, alarm.ringtone.toUri())
            backStack.add(RouteEditDetailsAlarm(alarm))
        },
        initialTime = timePickerTime,
        colors = TimePickerDefaults.timePickerColors(
            pickerLabelColor = Color.White,
            unselectedPickerContentColor = MaterialTheme.colorScheme.primaryContainer,
            confirmButtonContainerColor = MaterialTheme.colorScheme.primaryContainer,
            confirmButtonContentColor = Color.White
        )
    )
}