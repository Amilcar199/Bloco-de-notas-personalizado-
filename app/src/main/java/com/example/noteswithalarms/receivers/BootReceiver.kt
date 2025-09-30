package com.example.noteswithalarms.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.noteswithalarms.alarms.AlarmScheduler
import com.example.noteswithalarms.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val db = AppDatabase.getInstance(context)
        val scheduler = AlarmScheduler(context)
        CoroutineScope(Dispatchers.IO).launch {
            val alarms = db.alarmDao().getAll()
            alarms.forEach { alarm ->
                val now = System.currentTimeMillis()
                var trigger = alarm.triggerAtEpochMillis
                if (trigger <= now && alarm.isRepeating && alarm.repeatIntervalMillis != null) {
                    while (trigger <= now) trigger += alarm.repeatIntervalMillis
                    db.alarmDao().update(alarm.copy(triggerAtEpochMillis = trigger))
                }
                if (trigger > now) scheduler.scheduleExact(alarm.noteId, alarm.id, trigger)
            }
        }
    }
}

