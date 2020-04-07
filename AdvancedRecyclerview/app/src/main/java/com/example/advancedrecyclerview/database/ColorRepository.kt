package com.example.advancedrecyclerview.database

import com.example.advancedrecyclerview.model.ColorItem

class ColorRepository {
    fun getColorItems(): List<ColorItem> {
        return arrayListOf(
            ColorItem("000000", "black"),
            ColorItem("FF0000", "red"),
            ColorItem("0000FF", "blue"),
            ColorItem("FFFF00", "yellow"),
            ColorItem("008000", "green")
        )
    }
}