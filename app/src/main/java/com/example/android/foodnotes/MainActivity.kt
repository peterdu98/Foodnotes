package com.example.android.foodnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.foodnotes.model.note.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    // Properties
    private lateinit var noteViewModel: NoteViewModel

    // Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // Get a new or existing ViewModel from the ViewModelProvider
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Bottom navigation
        bottom_navigation.itemIconTintList = null
        onBottomNavSelected()
        bottom_navigation.selectedItemId = R.id.ic_home

        // Intent to a new note screen
        new_note_btn.setOnClickListener {
            val intent = Intent(this, NewNoteActivity::class.java)
            startActivity(intent)
        }
    }

    // Handle actions
    private fun onBottomNavSelected() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.ic_home -> {
                    createFragment(HomeFragment())
                    true
                }
                R.id.ic_bookmark -> {
                    createFragment(BookmarkFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun createFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.parent_container, fragment)
            .commit()
    }
}