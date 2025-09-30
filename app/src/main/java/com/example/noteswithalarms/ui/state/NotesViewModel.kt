package com.example.noteswithalarms.ui.state

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteswithalarms.data.Note
import com.example.noteswithalarms.repo.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NotesRepository.getInstance(app)

    private val queryFlow = MutableStateFlow("")

    val notes: StateFlow<List<Note>> = queryFlow
        .flatMapLatest { q -> if (q.isBlank()) repo.observeNotes() else repo.searchNotes(q) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _editingNote = MutableStateFlow<Note?>(null)
    val editingNote: StateFlow<Note?> = _editingNote.asStateFlow()

    fun setQuery(q: String) { queryFlow.value = q }

    fun startEditing(note: Note?) { _editingNote.value = note }

    fun saveNote(
        id: Long,
        title: String,
        description: String,
        category: String?,
        colorArgb: Long?,
        priority: Int,
        alarmAtMillis: Long?,
        isRepeating: Boolean,
        repeatIntervalMillis: Long?
    ) {
        viewModelScope.launch {
            val note = Note(
                id = id,
                title = title,
                description = description,
                category = category,
                colorArgb = colorArgb,
                createdAtEpochMillis = System.currentTimeMillis(),
                updatedAtEpochMillis = System.currentTimeMillis(),
                priority = priority
            )
            repo.upsertNoteWithAlarm(
                note,
                alarmAtMillis,
                isRepeating = isRepeating,
                repeatIntervalMillis = repeatIntervalMillis
            )
            _editingNote.value = null
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repo.deleteNote(note)
        }
    }
}

