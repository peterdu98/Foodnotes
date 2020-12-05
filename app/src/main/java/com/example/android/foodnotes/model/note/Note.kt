package com.example.android.foodnotes.model.note

import android.net.Uri
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "note_table", indices = arrayOf(Index(value = ["heading", "category"])))
class Note (@PrimaryKey val noteId: String, val heading: String, val minutes: String, var description: String, var summary: String, val imageSrc: String?, val rate: Float,
            val category: String, var bookmarked: Boolean) : Parcelable { }