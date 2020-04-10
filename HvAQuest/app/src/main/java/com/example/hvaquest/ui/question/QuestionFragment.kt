package com.example.hvaquest.ui.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hvaquest.R
import kotlinx.android.synthetic.main.fragment_question.*

/**
 * A simple [Fragment] subclass.
 */
class QuestionFragment : Fragment() {

    private val args: QuestionFragmentArgs by navArgs()
    private lateinit var viewModel: QuestionViewModel
    private lateinit var choices: Array<String>
    private lateinit var choiceAdapter: ChoiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(activity as AppCompatActivity).get(QuestionViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.prepareQuestion(args.questionNumber)

        val question = viewModel.getQuestionByIndex(args.questionNumber)

        tvQuestion.text = question.question
        choices = question.choices
        choiceAdapter = ChoiceAdapter(choices) { radioButton -> onChoiceClick(radioButton) }
        initViews()

        viewModel.status.observe(viewLifecycleOwner, Observer {
            tvStatus.text = it
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it == true)
                Toast.makeText(activity, "Incorrect answer, try again", Toast.LENGTH_SHORT).show()
        })

        viewModel.showLocationClue.observe(viewLifecycleOwner, Observer {
            if (it == true && viewModel.gameOver.value == false) {
                val action =
                    QuestionFragmentDirections.actionQuestionFragmentToLocationClueFragment(
                        viewModel.getQuestionIndexOfCurrent()
                    )
                findNavController().navigate(action)
            }
        })

        viewModel.gameOver.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(R.id.action_questionFragment_to_completedFragment)
            }
        })
    }

    private fun initViews() {
        rvChoices.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvChoices.adapter = choiceAdapter

        btnConfirm.setOnClickListener {
            onConfirmClick()
        }
    }

    private fun onConfirmClick() {
        viewModel.confirmChoice()
    }

    private fun onChoiceClick(choice: RadioButton) {
        if (viewModel.choice.value != null) {
            val previousChoice: RadioButton = viewModel.choice.value!!
            previousChoice.isChecked = false
        }

        viewModel.choice.value = choice
    }

}
