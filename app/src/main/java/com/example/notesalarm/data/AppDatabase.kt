package com.example.notesalarm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
	entities = [Note::class],
	version = 1,
	exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun noteDao(): NoteDao

	companion object {
		@Volatile private var INSTANCE: AppDatabase? = null

		fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
			INSTANCE ?: Room.databaseBuilder(
				context.applicationContext,
				AppDatabase::class.java,
				"notes_alarm.db"
			).fallbackToDestructiveMigration().build().also { INSTANCE = it }
		}
	}
}

