package com.example.notesalarm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notesalarm.data.Note

@Composable
fun NotesListScreen(vm: NotesViewModel, onCreate: () -> Unit, onEdit: (Long) -> Unit) {
	var query by remember { mutableStateOf("") }
	val notes by vm.notes.collectAsState()
	Scaffold(
		floatingActionButton = {
			FloatingActionButton(onClick = onCreate) {
				Icon(Icons.Default.Add, contentDescription = "Add")
			}
		}
	) { paddingValues ->
		Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
			OutlinedTextField(
				value = query,
				onValueChange = {
					query = it
					vm.setQuery(it)
				},
				label = { Text("Pesquisar") },
				modifier = Modifier.fillMaxWidth()
			)
			Spacer(Modifier.height(12.dp))
			LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				items(notes) { note ->
					NoteRow(note = note, onClick = { onEdit(note.id) })
				}
			}
		}
	}
}

@Composable
private fun NoteRow(note: Note, onClick: () -> Unit) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(Color(note.colorArgb))
			.clickable { onClick() }
			.padding(12.dp)
	) {
		Text(text = note.title.ifBlank { "(sem título)" }, style = MaterialTheme.typography.titleMedium)
		Spacer(Modifier.height(4.dp))
		Text(text = note.description, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
		Spacer(Modifier.height(4.dp))
		Row { Text(text = note.tagsCsv) }
	}
}

