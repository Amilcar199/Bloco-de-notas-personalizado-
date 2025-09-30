package com.example.noteswithalarms.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String?,
    val colorArgb: Long?,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
    val priority: Int = 0
)

