package davi.android.alarmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import davi.android.alarmapp.presentation.navigation.AlarmAppNavigation
import davi.android.alarmapp.presentation.theme.AlarmAppTheme
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import davi.android.alarmapp.presentation.viewmodel.AlarmsViewModel
import davi.android.alarmapp.receivers.AlarmReceiver
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val addAlarmViewModel: AddAlarmViewModel by inject()
    private val alarmsViewModel: AlarmsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlarmAppTheme {
                AlarmAppNavigation(addAlarmViewModel, alarmsViewModel)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if (AlarmReceiver.taskRingtone != null) {
            if (AlarmReceiver.taskRingtone!!.isPlaying) {
                AlarmReceiver.taskRingtone!!.stop()
                AlarmReceiver.vibrator!!.cancel()
            }
        }
    }
}