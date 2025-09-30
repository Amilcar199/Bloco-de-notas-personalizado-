package com.example.notesalarm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
	@Query("SELECT * FROM notes ORDER BY scheduledAtEpochMillis IS NULL, scheduledAtEpochMillis ASC, priority DESC, updatedAtEpochMillis DESC")
	fun observeAll(): Flow<List<Note>>

	@Query("SELECT * FROM notes WHERE id = :id")
	fun observeById(id: Long): Flow<Note?>

	@Query("SELECT * FROM notes WHERE id = :id")
	suspend fun getById(id: Long): Note?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(note: Note): Long

	@Update
	suspend fun update(note: Note)

	@Delete
	suspend fun delete(note: Note)

	@Query("DELETE FROM notes WHERE id = :id")
	suspend fun deleteById(id: Long)

	@Query("SELECT * FROM notes WHERE title LIKE :q OR description LIKE :q ORDER BY updatedAtEpochMillis DESC")
	fun search(q: String): Flow<List<Note>>
}

