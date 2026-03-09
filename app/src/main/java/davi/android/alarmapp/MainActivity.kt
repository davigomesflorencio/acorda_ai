package davi.android.alarmapp

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

    private var showPermissionRationaleDialog by mutableStateOf(false)

    private val requestExactAlarmPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            showExactAlarmPermissionRationale()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)

        checkAndRequestExactAlarmPermission()

        setContent {
            AlarmAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green),
                ) {
                    AlarmAppNavigation(addAlarmViewModel, alarmsViewModel)

                    if (showPermissionRationaleDialog) {
                        AlertDialog(
                            onDismissRequest = {
                                showPermissionRationaleDialog = false
                            },
                            title = { Text(stringResource(R.string.permission_alarm_exact_necessary)) },
                            text = { Text(stringResource(R.string.text_dialog_permission_alarm_exact)) },
                            confirmButton = {
                                TextButton(onClick = {
                                    showPermissionRationaleDialog = false
                                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                        data = ("package:$packageName").toUri()
                                    }
                                    requestExactAlarmPermissionLauncher.launch(intent)
                                }) {
                                    Text(stringResource(R.string.go_settings))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showPermissionRationaleDialog = false
                                }) {
                                    Text(stringResource(R.string.cancel))
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        managerVibrationAndSound.stopRingTone()
        managerVibrationAndSound.cancelVibrate()
    }

    private fun checkAndRequestExactAlarmPermission() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            showExactAlarmPermissionRationale()
        }
    }

    private fun showExactAlarmPermissionRationale() {
        showPermissionRationaleDialog = true
    }
}
