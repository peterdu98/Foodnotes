package com.example.android.foodnotes.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.foodnotes.R
import com.example.android.foodnotes.model.note.Note
import com.example.android.foodnotes.model.utils.ImageConverter

class NoteAdapter(private val onNotePressedListener: (Note) -> Unit, private val onLongPressedListener: (Note) -> Unit): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var notes = emptyList<Note>() // Cached copy of notes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.layout_note_card, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        // Initialise views for the view holder
        private val foodImg = v.findViewById<ImageView>(R.id.food_img)
        private val heading = v.findViewById<TextView>(R.id.heading)
        private val summary = v.findViewById<TextView>(R.id.summary)
        private val ratingBar = v.findViewById<RatingBar>(R.id.rating_bar)

        // Binding data into appropriate views
        fun bind(note: Note) {
            note.imageSrc?.let {
                foodImg.setImageURI(Uri.parse(it))
            }
            heading.text = note.heading
            summary.text = note.summary
            ratingBar.rating = note.rate

            v.setOnClickListener { onNotePressedListener(note) }
            v.setOnLongClickListener {
                onLongPressedListener(note)
                true
            }
        }
    }
}