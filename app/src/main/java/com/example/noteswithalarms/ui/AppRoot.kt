package com.example.noteswithalarms.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteswithalarms.data.Note
import com.example.noteswithalarms.ui.state.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppRoot(vm: NotesViewModel = viewModel()) {
    val notes by vm.notes.collectAsState()
    val editing = vm.editingNote.collectAsState().value
    var search by remember { mutableStateOf("") }

    LaunchedEffect(search) { vm.setQuery(search) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Notes with Alarms") },
                actions = {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    TextButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            ExportImport.exportToJson(context)
                        }
                    }) { Text("Export") }
                    // For simplicity, import from the default location previously exported
                    TextButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val file = java.io.File(context.filesDir, "notes_export.json")
                            if (file.exists()) ExportImport.importFromJson(context, file)
                        }
                    }) { Text("Import") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.startEditing(null) }) { Text("+") }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search") }
            )
            Spacer(Modifier.padding(8.dp))
            LazyColumn(Modifier.fillMaxSize()) {
                items(notes) { note ->
                    NoteRow(note = note, onClick = { vm.startEditing(note) }, onDelete = { vm.deleteNote(note) })
                    Divider()
                }
            }
        }
    }

    if (editing != null) {
        EditNoteDialog(
            initial = editing,
            onDismiss = { vm.startEditing(null) },
            onSave = { id, title, desc, category, color, priority, alarmAt, repeat, repeatMs ->
                vm.saveNote(id, title, desc, category, color, priority, alarmAt, repeat, repeatMs)
            }
        )
    }
}

@Composable
private fun NoteRow(note: Note, onClick: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(note.title, style = MaterialTheme.typography.titleMedium)
            if (note.description.isNotBlank()) {
                Text(note.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            }
        }
        TextButton(onClick = onDelete) { Text("Delete") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditNoteDialog(
    initial: Note,
    onDismiss: () -> Unit,
    onSave: (id: Long, title: String, desc: String, category: String?, color: Long?, priority: Int, alarmAt: Long?, repeat: Boolean, repeatMs: Long?) -> Unit
) {
    var title by remember { mutableStateOf(initial.title) }
    var desc by remember { mutableStateOf(initial.description) }
    var category by remember { mutableStateOf(initial.category ?: "") }
    var priority by remember { mutableStateOf(initial.priority.toString()) }
    var alarmAt by remember { mutableStateOf("") }
    var repeat by remember { mutableStateOf(false) }
    var repeatMs by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val alarmMillis = alarmAt.toLongOrNull()
                val p = priority.toIntOrNull() ?: 0
                val repMs = repeatMs.toLongOrNull()
                onSave(initial.id, title, desc, category.ifBlank { null }, null, p, alarmMillis, repeat, repMs)
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text(if (initial.id == 0L) "New Note" else "Edit Note") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") })
                OutlinedTextField(value = priority, onValueChange = { priority = it }, label = { Text("Priority") })
                OutlinedTextField(
                    value = alarmAt,
                    onValueChange = { alarmAt = it },
                    label = { Text("Alarm time (epoch ms)") }
                )
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked = repeat, onCheckedChange = { repeat = it })
                    Text("Repeat")
                }
                if (repeat) {
                    OutlinedTextField(
                        value = repeatMs,
                        onValueChange = { repeatMs = it },
                        label = { Text("Repeat interval (ms)") }
                    )
                }
                Text("Tip: leave alarm blank to not schedule.")
            }
        }
    )
}

