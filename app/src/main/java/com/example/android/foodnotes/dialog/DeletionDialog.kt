package com.example.android.foodnotes.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Typeface
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.android.foodnotes.R
import com.example.android.foodnotes.model.note.Note
import com.example.android.foodnotes.model.note.NoteViewModel
import kotlinx.android.synthetic.main.deletion_dialog.*

class DeletionDialog(private val activity: Activity) : Dialog(activity) {
    private lateinit var dialog: AlertDialog
    private lateinit var noteViewModel: NoteViewModel

    fun startLoadingDialog(note: Note, noteViewModel: NoteViewModel) {
        // Get a new or existing ViewModel from the ViewModelProvider
        this.noteViewModel = noteViewModel

        // Connect with view
        val builder = AlertDialog.Builder(activity, R.style.DeletionDialog)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.deletion_dialog, null))
        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()
        dialog.window?.setLayout(600, 460)

        // Setup action for "Yes" and "No" buttons
        dialog.yBtn.setOnClickListener { onButtonPressed(dialog.yBtn, note) }
        dialog.nBtn.setOnClickListener { onButtonPressed(dialog.nBtn, note) }
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

    private fun onButtonPressed(button: Button, note: Note) {
        if (button == dialog.yBtn) {
            this.noteViewModel.deleteNoteWithId(note.noteId)

            // Showing a custom toast with success message
            val mToast = Toast.makeText(activity, "Note got deleted", 500)
            val mToastView = mToast.view
            val mToastTV = mToastView.findViewById<TextView>(android.R.id.message)
            mToastView.background = mToastView.resources.getDrawable(R.drawable.rounded_success_background)
            mToastTV.textSize = 16.0f
            mToastTV.setTypeface(null, Typeface.BOLD)
            mToastTV.setTextColor(Color.BLACK)
            mToast.show()
        }
        dismissDialog()
    }
}