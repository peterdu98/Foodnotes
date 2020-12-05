package com.example.android.foodnotes.model.note

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database.
class NoteRepository(private val noteDao: NoteDAO) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    fun findNoteWithQueryAndCategory(query: String, category: String) = noteDao.findNoteWithQueryAndCategory(query, category)
    fun findNoteWithQueryAndBookmark(query: String) = noteDao.findNoteWithQueryAndBookmark(query)
    fun findNoteWithQuery(query: String) = noteDao.findNoteWithQuery(query)
    fun findNoteWithBookmark() = noteDao.findNoteWithBookmark()
    fun findNoteWithCategory(category: String) = noteDao.findNoteWithCategory(category)

    @WorkerThread
    suspend fun update(note: Note) { noteDao.updateNote(note) }

    @WorkerThread
    suspend fun insert(note: Note) { noteDao.insert(note) }

    @WorkerThread
    suspend fun deleteNoteWithId(noteId: String) { noteDao.deleteNoteWithId(noteId) }

    @WorkerThread
    suspend fun deleteAll() { noteDao.deleteAll() }
}