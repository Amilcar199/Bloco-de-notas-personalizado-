package com.example.notesalarm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val title: String,
	val description: String,
	val scheduledAtEpochMillis: Long?,
	val priority: Int,
	val tagsCsv: String,
	val colorArgb: Long,
	val createdAtEpochMillis: Long,
	val updatedAtEpochMillis: Long
)

