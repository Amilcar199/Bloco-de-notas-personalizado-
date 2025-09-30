package com.example.noteswithalarms.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alarms",
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("noteId")]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val noteId: Long,
    val triggerAtEpochMillis: Long,
    val isRepeating: Boolean = false,
    val repeatIntervalMillis: Long? = null
)

