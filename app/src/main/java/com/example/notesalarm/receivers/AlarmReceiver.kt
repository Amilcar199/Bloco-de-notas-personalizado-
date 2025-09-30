package com.example.notesalarm.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.notesalarm.NotificationConstants
import com.example.notesalarm.R
import com.example.notesalarm.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		val noteId = intent.getLongExtra("noteId", -1)
		if (noteId <= 0) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Default values in case fetching the note takes time
        var title = "Lembrete"
        var content = "Alarme para sua nota"

        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.get(context).noteDao()
            val note = dao.getById(noteId)
            if (note != null) {
                title = if (note.title.isNotBlank()) note.title else title
                content = if (note.description.isNotBlank()) note.description else content
            }
            val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(NotificationConstants.NOTIFICATION_ID_BASE + noteId.toInt(), notification)
        }
	}
}

