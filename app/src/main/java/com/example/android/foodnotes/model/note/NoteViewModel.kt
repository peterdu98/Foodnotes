package com.example.android.foodnotes.model.note

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.foodnotes.adapter.NoteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "NoteViewModel"

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    var allNotes: LiveData<List<Note>>

    init {
        val noteDAO = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDAO)

        allNotes = repository.allNotes
    }

    fun resetAllNotes() {
        allNotes = repository.allNotes
    }

    fun findNoteWithBookmark() {
        allNotes = repository.findNoteWithBookmark()
    }

    fun findNoteWithQuery(query: String, category: String?)  {
        allNotes = if (category != null) {
            repository.findNoteWithQueryAndCategory("%$query%", category)
        } else {
            repository.findNoteWithQuery("%$query%")
        }
    }

    fun findNoteWithQueryAndBookmark(query: String) {
        allNotes = repository.findNoteWithQueryAndBookmark("%$query%")
    }

    fun findNoteWithCategory(category: String) {
        allNotes = repository.findNoteWithCategory(category)
    }

    fun update(note: Note) = viewModelScope.launch (Dispatchers.IO) {
        repository.update(note)
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun deleteNoteWithId(noteId: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNoteWithId(noteId)
    }

    fun deleteAllNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun observeNotes(owner: LifecycleOwner, adapter: NoteAdapter, changeView: (List<Note>) -> Unit) {
        allNotes.observe(owner, Observer {
            it?.let { notes ->
                adapter.setNotes(notes)
                changeView(notes)
            }
        })
    }
}