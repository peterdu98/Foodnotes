package com.example.android.foodnotes.model.note

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDAO {
    @Query("SELECT * FROM note_table")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE bookmarked = 1")
    fun findNoteWithBookmark() : LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE category = :category")
    fun findNoteWithCategory(category: String): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE (heading LIKE :query OR summary LIKE :query OR description LIKE :query) AND category = :category")
    fun findNoteWithQueryAndCategory(query: String, category: String) : LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE (heading LIKE :query OR summary LIKE :query OR description LIKE :query) AND bookmarked = 1")
    fun findNoteWithQueryAndBookmark(query: String): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE heading LIKE :query OR summary LIKE :query OR description LIKE :query")
    fun findNoteWithQuery(query: String) : LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM note_table WHERE noteId = :noteId")
    suspend fun deleteNoteWithId(noteId: String)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()
}