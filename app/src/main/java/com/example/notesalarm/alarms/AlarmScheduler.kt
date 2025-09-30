package com.example.notesalarm.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.notesalarm.receivers.AlarmReceiver

object AlarmScheduler {
	fun scheduleExact(context: Context, noteId: Long, triggerAtMillis: Long) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, AlarmReceiver::class.java).apply {
			putExtra("noteId", noteId)
		}
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			noteId.toInt(),
			intent,
			PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		)
		alarmManager.setExactAndAllowWhileIdle(
			AlarmManager.RTC_WAKEUP,
			triggerAtMillis,
			pendingIntent
		)
	}

	fun cancel(context: Context, noteId: Long) {
		val intent = Intent(context, AlarmReceiver::class.java)
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			noteId.toInt(),
			intent,
			PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
		)
		if (pendingIntent != null) {
			val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
			alarmManager.cancel(pendingIntent)
			pendingIntent.cancel()
		}
	}
}

