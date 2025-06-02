package davi.android.alarmapp.receivers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import davi.android.alarmapp.core.Constants
import davi.android.alarmapp.domain.notification.AlarmNotification
import java.util.Date
import java.util.Locale


class AlarmReceiver : BroadcastReceiver() {


    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Constants.NOTIFICATION_INTENT_ACTION_START_ALARM) {
            val alarmId = intent.getIntExtra(Constants.EXTRA_ALARM_ID, -1) // Get ID if needed
            val hour = intent.getIntExtra(Constants.EXTRA_ALARM_TIME_HOUR, -1)
            val minute = intent.getIntExtra(Constants.EXTRA_ALARM_TIME_MINUTE, -1)
            val isSoundEnabled = intent.getBooleanExtra(Constants.EXTRA_ALARM_SOUND_ENABLED, true) // Default to true if not found
            val isVibrationEnabled = intent.getBooleanExtra(Constants.EXTRA_ALARM_VIBRATION_ENABLED, true) // Default to true if not found
            val alarmLabel = intent.getStringExtra(Constants.EXTRA_ALARM_LABEL) ?: "Alarm"

            val formattedTime = if (hour != -1 && minute != -1) {
                String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
            } else {
                "Alarme"
            }

            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrator = vibratorManager.defaultVibrator
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            taskRingtone = RingtoneManager.getRingtone(context, alert)
            if (taskRingtone != null) {
                if (isSoundEnabled)
                    taskRingtone!!.play()
                if (isVibrationEnabled)
                    vibrateDevice()
            }

            sendNotification(context, "Alarme")

        } else if (intent.action == Constants.NOTIFICATION_INTENT_ACTION_STOP_ALARM) {
            val alarmId = intent.getIntExtra(Constants.EXTRA_ALARM_ID, -1) // Get ID if needed
            val hour = intent.getIntExtra(Constants.EXTRA_ALARM_TIME_HOUR, -1)
            val minute = intent.getIntExtra(Constants.EXTRA_ALARM_TIME_MINUTE, -1)
            val isSoundEnabled = intent.getBooleanExtra(Constants.EXTRA_ALARM_SOUND_ENABLED, true) // Default to true if not found
            val isVibrationEnabled = intent.getBooleanExtra(Constants.EXTRA_ALARM_VIBRATION_ENABLED, true) // Default to true if not found
            val alarmLabel = intent.getStringExtra(Constants.EXTRA_ALARM_LABEL) ?: "Alarm"

            if (taskRingtone!!.isPlaying) {
                if (isSoundEnabled)
                    taskRingtone!!.stop()
                if (isVibrationEnabled)
                    vibrator!!.cancel()
            }
        }
    }


    private fun sendNotification(context: Context, body: String) {
        val alarmNotification = AlarmNotification(context)
        val notification = alarmNotification.getNotificationBuilder(body).build()
        alarmNotification.getManager().notify(getID(), notification)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun vibrateDevice() {
        if (vibrator?.hasVibrator() == true) { // Check if the device has a vibrator
            val vibrationEffect =
                VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator?.vibrate(vibrationEffect)
        }
    }

    companion object AlarmReceiver {
        var taskRingtone: Ringtone? = null
        var alert: Uri? = null
        var vibrator: Vibrator? = null

        fun getID(): Int {
            return (Date().time / 1000L % Int.MAX_VALUE).toInt()
        }
    }
}

