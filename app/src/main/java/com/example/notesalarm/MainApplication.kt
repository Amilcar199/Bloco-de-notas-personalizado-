package com.example.notesalarm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MainApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		createNotificationChannel()
	}

	private fun createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				NotificationConstants.CHANNEL_ID,
				"Notes Reminders",
				NotificationManager.IMPORTANCE_HIGH
			).apply {
				description = "Notifications for scheduled note reminders"
			}
			val manager = getSystemService(NotificationManager::class.java)
			manager.createNotificationChannel(channel)
		}
	}
}

object NotificationConstants {
	const val CHANNEL_ID = "notes_alarm_channel"
	const val NOTIFICATION_ID_BASE = 1000
}

