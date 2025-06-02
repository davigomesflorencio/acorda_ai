package davi.android.alarmapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import davi.android.alarmapp.presentation.screens.alarms.TimerPicker
import davi.android.alarmapp.presentation.screens.detailsAlarm.DetailsAlarmScreen
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import davi.android.alarmapp.presentation.viewmodel.AlarmsViewModel

@Composable
fun AlarmAppNavigation(addAlarmViewModel: AddAlarmViewModel, alarmsViewModel: AlarmsViewModel) {

    val backStack = remember { mutableStateListOf<Any>(Alarms) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    is Alarms -> NavEntry(key) {
                        TimerPicker(alarmsViewModel, addAlarmViewModel, backStack)
                    }

                    is DetailsAlarm -> NavEntry(key) {
                        DetailsAlarmScreen(addAlarmViewModel,backStack)
                    }

                    else -> {
                        error("Unknown route: $key")
                    }
                }
            })
    }
}
