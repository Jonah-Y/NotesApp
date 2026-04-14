package com.example.notesapp

import android.app.Application
import com.example.notesapp.data.NoteDatabase
import com.example.notesapp.ui.main.Repository

class NotesApplication : Application() {
    private val database: NoteDatabase by lazy { NoteDatabase.getDatabase(this) }
    val repository: Repository by lazy { Repository(database.noteDao()) }
}