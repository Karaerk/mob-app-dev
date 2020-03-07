package com.example.studentportal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*

const val EXTRA_PORTAL = "EXTRA_PORTAL"

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initViews()
    }

    private fun initViews() {
        supportActionBar?.title = "Create a Portal"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnAddPortal.setOnClickListener {
            onSaveClick()
        }
    }

    private fun onSaveClick() {
        if (etTitle.text.isNullOrBlank()) {
            etTitle.error = "Please fill in a title."
            return
        }

        if (etUrl.text.isNullOrBlank() || etUrl.text.toString() == getString(R.string.url_text)) {
            etUrl.error = "Please fill in an url."
            return
        }

        if (etTitle.text!!.isNotBlank() && etUrl.text!!.isNotBlank()) {
            val portal = Portal(etTitle.text.toString(), etUrl.text.toString())

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_PORTAL, portal)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
