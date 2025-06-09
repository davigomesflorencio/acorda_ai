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
import davi.android.alarmapp.presentation.screens.addAlarm.AddAlarm
import davi.android.alarmapp.presentation.screens.alarmDayWeeks.AlarmDaysWeekScreen
import davi.android.alarmapp.presentation.screens.alarms.Alarms
import davi.android.alarmapp.presentation.screens.detailsAlarm.DetailsAlarmScreen
import davi.android.alarmapp.presentation.screens.editAlarm.EditAlarm
import davi.android.alarmapp.presentation.screens.editDetailsAlarm.EditDetailsAlarm
import davi.android.alarmapp.presentation.screens.splash.SplashScreen
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import davi.android.alarmapp.presentation.viewmodel.AlarmsViewModel

@Composable
fun AlarmAppNavigation(addAlarmViewModel: AddAlarmViewModel, alarmsViewModel: AlarmsViewModel) {
    val backStack = remember { mutableStateListOf<Any>(RouteSplash) }
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
                    is RouteSplash -> NavEntry(key) {
                        SplashScreen(backStack)
                    }

                    is RouteAlarms -> NavEntry(key) {
                        Alarms(alarmsViewModel, backStack)
                    }

                    is RouteAddAlarm -> NavEntry(key) {
                        AddAlarm(addAlarmViewModel, backStack)
                    }

                    is RouteDetailsAlarm -> NavEntry(key) {
                        DetailsAlarmScreen(addAlarmViewModel, backStack)
                    }

                    is RouteEditAlarm -> NavEntry(key) {
                        EditAlarm(addAlarmViewModel, backStack, key.alarm)
                    }

                    is RouteEditDetailsAlarm -> NavEntry(key) {
                        EditDetailsAlarm(addAlarmViewModel, backStack, key.alarm)
                    }

                    is AlarmDaysWeekScreen -> NavEntry(key) {
                        AlarmDaysWeekScreen(addAlarmViewModel, backStack)
                    }

                    else -> {
                        error("Unknown route: $key")
                    }
                }
            })
    }
}
