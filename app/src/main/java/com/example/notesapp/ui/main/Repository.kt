package com.example.notesapp.ui.main

import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteDao

class Repository(private val dao: NoteDao) {

    val notes = dao.getAllNotes()

    suspend fun upsert(note: Note) {
        dao.upsert(note)
    }

    suspend fun delete(note: Note) {
        dao.delete(note)
    }
}