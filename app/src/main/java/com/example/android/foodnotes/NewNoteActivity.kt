package com.example.android.foodnotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.room.PrimaryKey
import com.example.android.foodnotes.model.note.Note
import com.example.android.foodnotes.model.note.NoteViewModel
import com.example.android.foodnotes.model.utils.ImageConverter
import kotlinx.android.synthetic.main.activity_new_note.*
import java.util.*
import kotlin.math.log

private const val TAG = "NewNoteActivity"
private const val PICK_IMAGE_CODE = 0

class NewNoteActivity : AppCompatActivity() {
    // Properties
    private val radioButtons by lazy {
        arrayOf<RadioButton>(radio_breakfast, radio_lunch, radio_snack, radio_dinner)
    }
    private lateinit var noteViewModel: NoteViewModel
    private var isSaved: Boolean = false
    private var heading = ""
    private var summary = ""
    private var minutes = "??"
    private var description = ""
    private var rating: Float = 0.0f
    private  var selectedCategory: String = "Unknown"
    private  var imgSrc: String? = null

    // Life cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        // Get a new or existing ViewModel from the ViewModelProvider.
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Setup view elements
        setupDeleteButton()
        setupRadioButtons()
        setupToolbar()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.data?.let { imgUri ->
                    imgSrc = imgUri.toString()
                    note_img.setImageURI(imgUri)
                    note_img.layoutParams.height = -2
                    note_img_btn.isEnabled = true
                    note_img_btn.visibility = View.VISIBLE
                }
            }
        }
    }

    // A Custom Toolbar
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_new_note, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_tick -> {
                saveNote()
                if (isSaved) {
                  finish()
                }
            }
            R.id.ic_image -> {
                pickImageIntent()
            }
        }
        return false
    }

    // Handle saved note
    private fun saveNote() {
        // Validate heading of the note
        heading_tf.editText?.let { headingET ->
            headingET.text?.let {
                if (it.isEmpty()) {
                    Toast.makeText(this, "Heading text should not be empty!", Toast.LENGTH_LONG).show()
                    return
                } else {
                    heading = it.toString()
                }
            }
        }
        // Get summary text, rating value, minutes text and description
        summary = summary_tf.editText?.text?.toString() ?: "No summary yet"
        rating = rating_bar.rating
        minutes = minutes_tf.editText?.text?.toString() ?: "??"
        description = description_tf.editText?.text?.toString() ?: "No description yet"

        // Create a new note
        val uuid = UUID.randomUUID().toString()
        val note = Note(uuid, heading, minutes, description, summary, imageSrc = imgSrc, rating,  selectedCategory, bookmarked = false)
        noteViewModel.insert(note)

        isSaved = true
    }

    // Handle image picker
    private fun pickImageIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_CODE)
    }
    
    // A Custom Radio Group
    private fun setupRadioButtons() {
        radio_breakfast.setOnClickListener { onRadioButtonPressed(radio_breakfast) }
        radio_lunch.setOnClickListener { onRadioButtonPressed(radio_lunch) }
        radio_snack.setOnClickListener { onRadioButtonPressed(radio_snack) }
        radio_dinner.setOnClickListener { onRadioButtonPressed(radio_dinner) }
    }

    private fun onRadioButtonPressed(radioButton: RadioButton) {
        // Uncheck the current button by selecting other buttons

        radioButtons.forEach { button ->
            button.isChecked = (button == radioButton)
            if (button == radioButton) {
                selectedCategory = button.text.toString()
            }
        }
    }

    // A custom delete button for an image
    private fun setupDeleteButton() {
        note_img_btn.isEnabled = false
        note_img_btn.visibility = View.GONE
        note_img_btn.setOnClickListener { onDeleteButtonPressed() }
    }

    private fun onDeleteButtonPressed() {
        note_img.setImageBitmap(null)
        note_img.layoutParams.height = 0
        note_img_btn.isEnabled = false
        note_img_btn.visibility = View.GONE
        imgSrc = null
    }
}