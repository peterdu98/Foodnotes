package com.example.android.foodnotes.model.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "CategoryViewModel"

class CategoryViewModel(applcation: Application) : AndroidViewModel(applcation) {
    var categories: ArrayList<String> = ArrayList()
    var selectedCategory: MutableLiveData<String> = MutableLiveData()

    init {
        initData()
    }

    private fun initData() {
        categories.add("Breakfast")
        categories.add("Lunch")
        categories.add("Snack")
        categories.add("Dinner")
    }
}