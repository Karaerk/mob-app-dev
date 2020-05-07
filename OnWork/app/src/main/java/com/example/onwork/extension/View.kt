package com.example.onwork.extension

import android.view.View
import com.example.onwork.ui.helper.OnSingleClickListener

/**
 * Allows the user to only listen to a single click event on a specific view.
 */
fun View.setOnSingleClickListener(l: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(l))
}

/**
 * Allows the user to only listen to a single click event on a specific view.
 */
fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}