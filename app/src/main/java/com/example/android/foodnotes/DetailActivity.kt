package com.example.android.foodnotes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import com.example.android.foodnotes.model.note.Note
import com.example.android.foodnotes.model.note.NoteViewModel
import com.example.android.foodnotes.model.utils.ImageConverter
import kotlinx.android.synthetic.main.activity_detail.*

private const val TAG = "DetailActivity"
private const val NOTE = "noteData"

class DetailActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel

    // Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_detail)

        // Get a new or existing ViewModel from the ViewModelProvider.
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Get data from Intent
        val note = intent.getParcelableExtra<Note>(NOTE)

        note?.let {
            setupUI(it)
        }
    }

    // Setup UI elements
    private fun setupUI(note: Note) {
        // Optional binding for image element
        note.imageSrc?.let {
            note_img.setImageURI(Uri.parse(it))
        }

        // Assign text into TextView elements
        note_heading.text = note.heading
        note_rate.text = note.rate.toString()
        note_minutes.text = "${note.minutes} mins"
        note_category.text = note.category

        // Optional binding for edit text elements
        note_summary.editText?.let {
            it.text = SpannableStringBuilder(note.summary)
            it.isEnabled = false
        }

        note_description.editText?.let {
            it.text = SpannableStringBuilder(note.description)
            it.isEnabled = false
        }

        // Bookmark button
        bookmark_btn.isChecked = note.bookmarked
        bookmark_btn.setOnClickListener { onBookmarkButtonPressed(bookmark_btn, note) }

        // Edit button
        edit_btn.setOnClickListener { onEditButtonPressed(edit_btn, note) }
    }

    // Handle toggle button
    private fun onBookmarkButtonPressed (button: ToggleButton, note: Note) {
        note.bookmarked = button.isChecked
        noteViewModel.update(note)
    }

    // Handle edit button
    private fun onEditButtonPressed(button: ToggleButton, note: Note) {
        var toastMsg = ""
        note_summary.editText?.isEnabled = button.isChecked
        note_description.editText?.isEnabled = button.isChecked

        if (button.isChecked) {
            toastMsg = "You're in the editable mode"
        } else {
            toastMsg = "Saved note"
            note.summary = note_summary.editText?.text.toString()
            note.description = note_description.editText?.text.toString()
            noteViewModel.update(note)
        }

        Toast.makeText(applicationContext, toastMsg, Toast.LENGTH_SHORT).show()
    }

    // Companion elements
    companion object {
        // Put data into an activity
        fun newIntent(packageContext: Activity, note: Note) : Intent {
            return Intent(packageContext, DetailActivity::class.java).apply {
                putExtra(NOTE, note)
            }
        }
    }
}