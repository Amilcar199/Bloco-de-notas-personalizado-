package com.example.noteswithalarms.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.noteswithalarms.MainActivity
import com.example.noteswithalarms.NotesApp
import com.example.noteswithalarms.R
import com.example.noteswithalarms.alarms.AlarmScheduler
import com.example.noteswithalarms.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val noteId = intent.getLongExtra(AlarmScheduler.EXTRA_NOTE_ID, -1)
        val alarmId = intent.getLongExtra(AlarmScheduler.EXTRA_ALARM_ID, -1)
        if (noteId <= 0 || alarmId <= 0) return

        val db = AppDatabase.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
            val note = db.noteDao().getById(noteId)
            val title = note?.title ?: context.getString(R.string.app_name)
            val text = note?.description ?: "Reminder"

            val openIntent = Intent(context, MainActivity::class.java)
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            val contentIntent = PendingIntent.getActivity(context, 0, openIntent, flags)

            val notification = NotificationCompat.Builder(context, NotesApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(alarmId.toInt(), notification)

            val alarm = db.alarmDao().getById(alarmId)
            if (alarm != null && alarm.isRepeating && alarm.repeatIntervalMillis != null) {
                var next = alarm.triggerAtEpochMillis + alarm.repeatIntervalMillis
                val now = System.currentTimeMillis()
                while (next <= now) {
                    next += alarm.repeatIntervalMillis
                }
                val updated = alarm.copy(triggerAtEpochMillis = next)
                db.alarmDao().update(updated)
                val scheduler = com.example.noteswithalarms.alarms.AlarmScheduler(context)
                scheduler.scheduleExact(alarm.noteId, alarm.id, next)
            }
        }
    }
}

