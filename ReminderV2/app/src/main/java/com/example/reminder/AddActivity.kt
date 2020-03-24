package com.example.reminder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*

const val EXTRA_REMINDER = "EXTRA_REMINDER"

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
    }

    private fun initView() {
        fab.setOnClickListener { onSaveClick() }
    }

    private fun onSaveClick() {
        if (etAddReminder.text.isNullOrBlank()) {
            // Allows the app to show an error message right at the edit text view
            // etAddReminder.setError("Please fill in text before saving a reminder.")

            Toast.makeText(this, "The reminder cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            val reminder = Reminder(etAddReminder.text.toString())

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_REMINDER, reminder)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
