package com.example.onwork.ui.helper

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private var sharedPreferences: SharedPreferences

    fun saveString(name: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(name, value)
        editor.apply()
    }

    companion object {
        const val myPreference = "mypref"
    }

    init {
        sharedPreferences = context.getSharedPreferences(
            myPreference,
            Context.MODE_PRIVATE
        )
    }
}