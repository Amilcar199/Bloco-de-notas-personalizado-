package com.example.notesalarm.di

import android.content.Context
import com.example.notesalarm.data.AppDatabase
import com.example.notesalarm.data.NoteRepository

object ServiceLocator {
	@Volatile private var repository: NoteRepository? = null

	fun provideRepository(context: Context): NoteRepository = repository ?: synchronized(this) {
		repository ?: NoteRepository(AppDatabase.get(context).noteDao()).also { repository = it }
	}
}

