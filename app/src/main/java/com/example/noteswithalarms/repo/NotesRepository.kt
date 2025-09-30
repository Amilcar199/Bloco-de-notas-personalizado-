package com.example.noteswithalarms.repo

import android.content.Context
import com.example.noteswithalarms.alarms.AlarmScheduler
import com.example.noteswithalarms.data.Alarm
import com.example.noteswithalarms.data.AppDatabase
import com.example.noteswithalarms.data.Note
import kotlinx.coroutines.flow.Flow

class NotesRepository private constructor(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val noteDao = db.noteDao()
    private val alarmDao = db.alarmDao()
    private val alarmScheduler = AlarmScheduler(context)

    fun observeNotes(): Flow<List<Note>> = noteDao.observeAll()

    fun searchNotes(query: String): Flow<List<Note>> = noteDao.search(query)

    suspend fun getNote(id: Long): Note? = noteDao.getById(id)

    suspend fun upsertNoteWithAlarm(
        note: Note,
        alarmEpochMillis: Long?,
        isRepeating: Boolean = false,
        repeatIntervalMillis: Long? = null
    ): Long {
        val now = System.currentTimeMillis()
        val noteForSave = if (note.id == 0L) {
            note.copy(createdAtEpochMillis = now, updatedAtEpochMillis = now)
        } else {
            note.copy(updatedAtEpochMillis = now)
        }
        val noteId = if (noteForSave.id == 0L) noteDao.insert(noteForSave) else {
            noteDao.update(noteForSave)
            noteForSave.id
        }

        alarmDao.deleteByNoteId(noteId)

        if (alarmEpochMillis != null) {
            val alarm = Alarm(
                noteId = noteId,
                triggerAtEpochMillis = alarmEpochMillis,
                isRepeating = isRepeating,
                repeatIntervalMillis = repeatIntervalMillis
            )
            val alarmId = alarmDao.insert(alarm)
            alarmScheduler.scheduleExact(noteId, alarmId, alarmEpochMillis)
        }

        return noteId
    }

    suspend fun deleteNote(note: Note) {
        val alarms = alarmDao.getByNoteId(note.id)
        alarms.forEach { alarm ->
            alarmScheduler.cancel(note.id, alarm.id)
        }
        alarmDao.deleteByNoteId(note.id)
        noteDao.delete(note)
    }

    suspend fun getAllAlarms() = alarmDao.getAll()

    companion object {
        @Volatile private var INSTANCE: NotesRepository? = null
        fun getInstance(context: Context): NotesRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotesRepository(context.applicationContext).also { INSTANCE = it }
            }
    }
}

