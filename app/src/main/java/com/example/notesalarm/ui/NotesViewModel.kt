package com.example.notesalarm.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesalarm.alarms.AlarmScheduler
import com.example.notesalarm.data.Note
import com.example.notesalarm.di.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(app: Application) : AndroidViewModel(app) {
	private val repository = ServiceLocator.provideRepository(app)

	private val queryFlow = MutableStateFlow("")
	val notes: StateFlow<List<Note>> = queryFlow.flatMapLatest { q ->
		if (q.isBlank()) repository.observeAll() else repository.search(q)
	}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

	fun setQuery(q: String) {
		queryFlow.value = q
	}

	fun saveNote(
		id: Long?,
		title: String,
		description: String,
		scheduledAt: Long?,
		priority: Int,
		tagsCsv: String,
		colorArgb: Long
	) {
		viewModelScope.launch {
			val now = System.currentTimeMillis()
			val note = Note(
				id = id ?: 0,
				title = title,
				description = description,
				scheduledAtEpochMillis = scheduledAt,
				priority = priority,
				tagsCsv = tagsCsv,
				colorArgb = colorArgb,
				createdAtEpochMillis = now,
				updatedAtEpochMillis = now
			)
			val newId = repository.upsert(note)
			val finalId = if (id == null || id == 0L) newId else id
			// Schedule or cancel
			if (scheduledAt != null) {
				AlarmScheduler.scheduleExact(getApplication(), finalId, scheduledAt)
			} else {
				AlarmScheduler.cancel(getApplication(), finalId)
			}
		}
	}

	fun deleteNote(note: Note) {
		viewModelScope.launch {
			repository.delete(note)
			AlarmScheduler.cancel(getApplication(), note.id)
		}
	}
}

