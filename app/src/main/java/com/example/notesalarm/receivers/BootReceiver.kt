package com.example.notesalarm.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.notesalarm.alarms.AlarmScheduler
import com.example.notesalarm.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
		CoroutineScope(Dispatchers.IO).launch {
			val dao = AppDatabase.get(context).noteDao()
			// naive reschedule: fetch all with future scheduled time and reschedule
			val now = System.currentTimeMillis()
			// simple query via observeAll first emission is okay for small datasets
			dao.observeAll().collect { notes ->
				notes.forEach { note ->
					note.scheduledAtEpochMillis?.let { whenTime ->
						if (whenTime >= now) {
							AlarmScheduler.scheduleExact(context, note.id, whenTime)
						}
					}
				}
				// stop after first emission
				return@collect
			}
		}
	}
}

