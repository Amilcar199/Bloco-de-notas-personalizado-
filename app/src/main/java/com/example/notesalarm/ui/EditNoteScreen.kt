package com.example.notesalarm.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun EditNoteScreen(vm: NotesViewModel, onDone: () -> Unit) {
	val context = LocalContext.current
	val title = remember { mutableStateOf("") }
	val description = remember { mutableStateOf("") }
	val tags = remember { mutableStateOf("") }
	val priority = remember { mutableStateOf(0) }
	val color = remember { mutableStateOf(0xFFEFEFEF) }
	val scheduledAt = remember { mutableStateOf<Long?>(null) }

	fun pickDateTime() {
		val cal = Calendar.getInstance()
		DatePickerDialog(context, { _, y, m, d ->
			TimePickerDialog(context, { _, h, min ->
				cal.set(y, m, d, h, min, 0)
				scheduledAt.value = cal.timeInMillis
			}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
		}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
	}

	Column(Modifier.fillMaxSize().padding(16.dp)) {
		OutlinedTextField(value = title.value, onValueChange = { title.value = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
		Spacer(Modifier.height(8.dp))
		OutlinedTextField(value = description.value, onValueChange = { description.value = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
		Spacer(Modifier.height(8.dp))
		OutlinedTextField(value = tags.value, onValueChange = { tags.value = it }, label = { Text("Tags") }, modifier = Modifier.fillMaxWidth())
		Spacer(Modifier.height(8.dp))
		Button(onClick = { pickDateTime() }) { Text(text = if (scheduledAt.value != null) "Alterar alarme" else "Definir alarme") }
		Spacer(Modifier.height(16.dp))
		Button(onClick = {
			vm.saveNote(
				id = null,
				title = title.value,
				description = description.value,
				scheduledAt = scheduledAt.value,
				priority = priority.value,
				tagsCsv = tags.value,
				colorArgb = color.value.toLong()
			)
			onDone()
		}) { Text("Salvar") }
	}
}

