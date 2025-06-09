package davi.android.alarmapp.presentation.screens.addAlarm

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.TimePicker
import androidx.wear.compose.material3.TimePickerDefaults
import davi.android.alarmapp.presentation.navigation.RouteDetailsAlarm
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import java.time.LocalTime

@Composable
fun AddAlarm(addAlarmViewModel: AddAlarmViewModel, backStack: SnapshotStateList<Any>) {

    var timePickerTime by remember { mutableStateOf(LocalTime.now()) }

    TimePicker(
        onTimePicked = {
            timePickerTime = it
            addAlarmViewModel.hour.intValue = it.hour
            addAlarmViewModel.minute.intValue = it.minute
            backStack.add(RouteDetailsAlarm)
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