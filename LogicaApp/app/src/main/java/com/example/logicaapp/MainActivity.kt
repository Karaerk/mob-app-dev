package com.example.logicaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val questionAnswers: Map<Int, Char> = mapOf(
        R.id.etQuestionOneAnswer to 'T',
        R.id.etQuestionTwoAnswer to 'F',
        R.id.etQuestionThreeAnswer to 'F',
        R.id.etQuestionFourAnswer to 'F'
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    /**
     * Set the initial (UI) state of the game.
     */
    fun initViews() {
        btnSubmit.setOnClickListener {
            checkAnswers()
        }
    }

    /**
     * Checks all filled in answers and shows the result to the user.
     */
    fun checkAnswers() {
        var etAnswer: EditText? = null
        var countCorrectAnswers: Int = 0

        // Give the user points to filled in answers if they're correct
        for ((key, value) in questionAnswers){
            etAnswer = findViewById(key)

            // Only add points if user entered the right answer
            if(etAnswer.text.length > 0 && etAnswer.text.get(0).toUpperCase() == value){
                countCorrectAnswers++
            }

        }

        Toast.makeText(this, getString(R.string.correct_answers, countCorrectAnswers), Toast.LENGTH_LONG).show()
    }
}
