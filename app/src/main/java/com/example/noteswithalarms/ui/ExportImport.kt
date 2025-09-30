package com.example.noteswithalarms.ui

import android.content.Context
import android.widget.Toast
import com.example.noteswithalarms.data.AppDatabase
import com.example.noteswithalarms.data.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object ExportImport {
    suspend fun exportToJson(context: Context): File = withContext(Dispatchers.IO) {
        val db = AppDatabase.getInstance(context)
        val allNotes = db.noteDao().getAllNow()
        val array = JSONArray()
        allNotes.forEach { n ->
            val obj = JSONObject()
                .put("id", n.id)
                .put("title", n.title)
                .put("description", n.description)
                .put("category", n.category)
                .put("colorArgb", n.colorArgb)
                .put("createdAtEpochMillis", n.createdAtEpochMillis)
                .put("updatedAtEpochMillis", n.updatedAtEpochMillis)
                .put("priority", n.priority)
            array.put(obj)
        }
        val out = JSONObject().put("notes", array).toString(2)
        val file = File(context.filesDir, "notes_export.json")
        file.writeText(out)
        file
    }

    suspend fun importFromJson(context: Context, file: File) = withContext(Dispatchers.IO) {
        val text = file.readText()
        val json = JSONObject(text)
        val db = AppDatabase.getInstance(context)
        val noteDao = db.noteDao()
        val arr = json.optJSONArray("notes") ?: JSONArray()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val note = Note(
                id = 0L,
                title = o.optString("title"),
                description = o.optString("description"),
                category = if (o.isNull("category")) null else o.getString("category"),
                colorArgb = if (o.isNull("colorArgb")) null else o.getLong("colorArgb"),
                createdAtEpochMillis = o.optLong("createdAtEpochMillis", System.currentTimeMillis()),
                updatedAtEpochMillis = System.currentTimeMillis(),
                priority = o.optInt("priority", 0)
            )
            noteDao.insert(note)
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Import complete", Toast.LENGTH_SHORT).show()
        }
    }
}

