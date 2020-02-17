package com.example.swipequiz

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val questions = arrayListOf<Question>()
    private val questionAdapter = QuestionAdapter(questions)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        rvQuestions.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvQuestions.adapter = questionAdapter

        rvQuestions.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        for (i in Question.questions.indices) {
            questions.add(Question(Question.questions[i], Question.answers[i]))
        }
        questionAdapter.notifyDataSetChanged()

        createItemTouchHelper().attachToRecyclerView(rvQuestions)
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val answer = when (direction) {
                    ItemTouchHelper.LEFT -> false
                    ItemTouchHelper.RIGHT -> true
                    else -> false
                }

                val position = viewHolder.adapterPosition

                if (isAnswerCorrect(position, answer)) {
                    removeQuestion(position)
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.answer_correct),
                        Toast.LENGTH_LONG
                    ).show()
                    questionAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.answer_incorrect),
                        Toast.LENGTH_LONG
                    ).show()
                    questionAdapter.notifyDataSetChanged()
                }

            }
        }

        return ItemTouchHelper(callback)
    }

    /**
     * Checks if given answer is correct.
     */
    private fun isAnswerCorrect(questionNr: Int, answer: Boolean): Boolean {
        val expectedAnswer = Question.answers[questionNr]

        return expectedAnswer == answer
    }

    /**
     * Removes a question properly so that the application is able to function well after
     * some changes were made.
     */
    private fun removeQuestion(questionNr: Int){
        questions.removeAt(questionNr)

        // Needed as the RecycleView rearranges the position of each element
        Question.questions.removeAt(questionNr)
        Question.answers.removeAt(questionNr)
    }
}
