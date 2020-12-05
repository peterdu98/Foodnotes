package com.example.android.foodnotes

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.foodnotes.adapter.CategoryAdapter
import com.example.android.foodnotes.adapter.NoteAdapter
import com.example.android.foodnotes.dialog.DeletionDialog
import com.example.android.foodnotes.model.category.CategoryViewModel
import com.example.android.foodnotes.model.note.Note
import com.example.android.foodnotes.model.note.NoteViewModel
import kotlinx.android.synthetic.main.fragment_home.*

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    // Properties
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private val deletionDialog by lazy {
        this.activity?.let { DeletionDialog(it) }
    }
    private val categoryViewModel by lazy {
        ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    // Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate has been established.")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Optional binding for activity
        activity?.let { fragmentActivity ->
            // Category Adapter
            val categoryList = view.findViewById<RecyclerView>(R.id.categoryList)
            val categories = categoryViewModel.categories
            categoryList.layoutManager = LinearLayoutManager(fragmentActivity.applicationContext, LinearLayoutManager.HORIZONTAL, false)
            categoryAdapter = CategoryAdapter(categoryViewModel) { onToggleButtonClick(it) }
            categoryList.adapter = categoryAdapter

            // Note Adapter
            val noteList = view.findViewById<RecyclerView>(R.id.noteList)
            noteList.layoutManager = GridLayoutManager(fragmentActivity.applicationContext, 2)
            noteAdapter = NoteAdapter(onNotePressedListener = {
                onNotePressed(it)
            }, onLongPressedListener = {
                onNoteLongPressed(it)
            })
            noteList.adapter = noteAdapter

            // Get a new or existing ViewModel from the ViewModelProvider
            noteViewModel = ViewModelProvider(fragmentActivity).get(NoteViewModel::class.java)

            // Add an observer on the LiveData returned by getAllNotes
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground
            noteViewModel.resetAllNotes()
            noteViewModel.observeNotes(fragmentActivity, adapter = noteAdapter, changeView = {
                // Update number of notes in the UI
                val noteInfo = view.findViewById<TextView>(R.id.notes_info)
                noteInfo.text = "You created ${it.size} food notes"
            })

            // Handle search bar
            val searchBar = view.findViewById<SearchView>(R.id.searchBar)
            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                // If text is entered, use that for searching
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    p0?.let {
                        noteViewModel.findNoteWithQuery(it, categoryViewModel.selectedCategory.value)
                        noteViewModel.observeNotes(fragmentActivity, adapter = noteAdapter, changeView = {})
                        hideKeyboard(view)
                    }
                    return true
                }

                // If text is clear, fetch all notes
                override fun onQueryTextChange(p0: String?): Boolean {
                    p0?.let {
                        if (it.isEmpty()) {
                            // If category is enabled, only fetch notes on that category
                            if (categoryViewModel.selectedCategory.value == null) {
                                noteViewModel.resetAllNotes()
                            } else {
                                noteViewModel.findNoteWithCategory(categoryViewModel.selectedCategory.value!!)
                            }
                            noteViewModel.observeNotes(fragmentActivity, adapter = noteAdapter, changeView = {})
                            hideKeyboard(view)
                        }
                    }
                    return true
                }
            })
        }

        return view
    }

    // Handle keyboard
    private fun hideKeyboard(v: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    // Handle listeners
    private fun onNotePressed(note: Note) {
        // Create the intent ad put Parcelable object into it
        // Establish the transition between this fragment and DetailActivity
        activity?.let {
            val intent = DetailActivity.newIntent(it, note)
            startActivity(intent)
            searchBar.setQuery(null, false)
            searchBar.clearFocus()
        }
    }

    private fun onNoteLongPressed(note: Note) {
        deletionDialog?.let {
            it.startLoadingDialog(note, noteViewModel)
        }
    }

    private fun onToggleButtonClick(item: String) {
        // Handle double-selection
        categoryViewModel.selectedCategory.let {
            if (it.value == item) {
                categoryAdapter.updateCategory(null)
            } else {
                categoryAdapter.updateCategory(item)
            }
        }

        // If the item is turned off -> Fetch all notes. Otherwise -> Fetch specific notes
        if (categoryViewModel.selectedCategory.value == null) {
            noteViewModel.resetAllNotes()
            noteViewModel.observeNotes(this.activity!!, adapter = noteAdapter, changeView = {})
        } else {
            noteViewModel.findNoteWithCategory(item)
            noteViewModel.observeNotes(this.activity!!, adapter = noteAdapter, changeView = {})
        }
    }
}