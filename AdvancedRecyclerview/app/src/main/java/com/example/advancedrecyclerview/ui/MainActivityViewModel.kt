package com.example.advancedrecyclerview.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.advancedrecyclerview.database.ColorRepository
import com.example.advancedrecyclerview.model.ColorItem

class MainActivityViewModel : ViewModel() {
    private val colorRepository = ColorRepository()

    val colorItems = MutableLiveData<List<ColorItem>>().apply {
        value = colorRepository.getColorItems()
    }
}