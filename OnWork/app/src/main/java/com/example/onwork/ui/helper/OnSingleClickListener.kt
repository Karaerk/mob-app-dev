package com.example.onwork.ui.helper

import android.view.View

/**
 * Handles only a single click on a specific view.
 * This is needed to prevent unknown destination at sign in view.
 */
class OnSingleClickListener : View.OnClickListener {

    private val onClickListener: View.OnClickListener

    constructor(listener: View.OnClickListener) {
        onClickListener = listener
    }

    constructor(listener: (View) -> Unit) {
        onClickListener = View.OnClickListener { listener.invoke(it) }
    }

    override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis >= previousClickTimeMillis + DELAY_MILLIS) {
            previousClickTimeMillis = currentTimeMillis
            onClickListener.onClick(v)
        }
    }

    companion object {
        // Tweak this value as you see fit. In my personal testing this
        // seems to be good, but you may want to try on some different
        // devices and make sure you can't produce any crashes.
        private const val DELAY_MILLIS = 200L

        private var previousClickTimeMillis = 0L
    }

}