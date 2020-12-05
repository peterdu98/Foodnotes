package com.example.android.foodnotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.example.android.foodnotes.R
import com.example.android.foodnotes.model.category.CategoryViewModel
import com.example.android.foodnotes.model.note.Note

class CategoryAdapter(private val categoryViewModel: CategoryViewModel, private val listener: (String) -> Unit) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.layout_category_button, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount() = categoryViewModel.categories.size

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val item = categoryViewModel.categories[position]
        holder.bind(item)
    }

    internal fun updateCategory(category: String?) {
        categoryViewModel.selectedCategory.value = category
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        // Initialise views for the view holder
        private val button: ToggleButton = v.findViewById(R.id.category_button)

        // Binding data into appropriate views
        fun bind(item: String) {
            button.text = item
            button.textOn = item
            button.textOff = item
            button.alpha = if (item != categoryViewModel.selectedCategory.value) 0.6f else 1.0f
            button.setOnClickListener { listener(item) }
        }
    }
}