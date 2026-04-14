package com.example.notesapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notesapp.data.Note
import com.example.notesapp.NotesApplication
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository) : ViewModel() {
    val notes = repository.notes.asLiveData()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NotesApplication).repository
                NoteViewModel(repository)
            }
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            repository.upsert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}