package davi.android.alarmapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import davi.android.alarmapp.domain.vibration.ManagerVibrationAndSound
import davi.android.alarmapp.presentation.navigation.AlarmAppNavigation
import davi.android.alarmapp.presentation.theme.AlarmAppTheme
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import davi.android.alarmapp.presentation.viewmodel.AlarmsViewModel
import org.koin.android.ext.android.inject

class MainActivity : FragmentActivity() {

    private val managerVibrationAndSound: ManagerVibrationAndSound by inject()
    private val addAlarmViewModel: AddAlarmViewModel by inject()
    private val alarmsViewModel: AlarmsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AlarmAppTheme {
                AlarmAppNavigation(addAlarmViewModel, alarmsViewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        managerVibrationAndSound.stopRingTone()
        managerVibrationAndSound.cancelVibrate()
    }
}