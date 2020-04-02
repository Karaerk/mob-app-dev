package com.example.gamebacklog.ui.add

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.gamebacklog.R
import com.example.gamebacklog.model.NewGame
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*


class AddActivity : AppCompatActivity() {

    private lateinit var addActivityViewModel: AddActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        fab.setOnClickListener {
            val game = NewGame(
                title = etTitle.text.toString(),
                platform = etPlatform.text.toString(),
                year = etYear.text.toString().toIntOrNull(),
                month = etMonth.text.toString().toIntOrNull(),
                day = etDay.text.toString().toIntOrNull()
            )

            addActivityViewModel.newGame.value = game
            addActivityViewModel.insertGame()
        }
    }

    private fun initViewModel() {
        addActivityViewModel = ViewModelProviders.of(this).get(AddActivityViewModel::class.java)

        addActivityViewModel.error.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
        addActivityViewModel.success.observe(this, Observer { success ->
            if (success) finish()
        })
    }
}
