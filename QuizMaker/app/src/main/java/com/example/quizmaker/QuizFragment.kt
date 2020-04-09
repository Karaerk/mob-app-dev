package com.example.quizmaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_quiz.*

/**
 * A simple [Fragment] subclass.
 */
class QuizFragment : Fragment() {

    private lateinit var viewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(activity as AppCompatActivity).get(QuizViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvQuestion.text = viewModel.question

        btnConfirmAnswer.setOnClickListener {
            if (viewModel.isAnswerCorrect(etAnswer.text.toString())) {
                Toast.makeText(context, "Your answer is correct!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(
                    context,
                    "Your answer is incorrect! The correct answer was: ${viewModel.answer}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}
