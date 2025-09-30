package com.example.notesalarm.data

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dao: NoteDao) {
	fun observeAll(): Flow<List<Note>> = dao.observeAll()
	fun observeById(id: Long): Flow<Note?> = dao.observeById(id)
	fun search(query: String): Flow<List<Note>> = dao.search("%$query%")

	suspend fun upsert(note: Note): Long = dao.upsert(note)
	suspend fun update(note: Note) = dao.update(note)
	suspend fun delete(note: Note) = dao.delete(note)
	suspend fun deleteById(id: Long) = dao.deleteById(id)
}

