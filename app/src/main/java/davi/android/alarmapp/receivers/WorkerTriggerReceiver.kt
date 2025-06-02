package davi.android.alarmapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import davi.android.alarmapp.domain.worker.WorkerFunctions

class WorkerTriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        WorkerFunctions.startAlarmWorker(context, action ?: "")
    }
}