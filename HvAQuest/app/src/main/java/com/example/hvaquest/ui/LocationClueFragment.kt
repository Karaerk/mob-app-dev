package com.example.hvaquest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hvaquest.R
import com.example.hvaquest.ui.question.QuestionViewModel
import kotlinx.android.synthetic.main.fragment_location_clue.*

/**
 * A simple [Fragment] subclass.
 */
class LocationClueFragment : Fragment() {

    private val args: LocationClueFragmentArgs by navArgs()
    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(activity as AppCompatActivity).get(QuestionViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_clue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initView()
    }

    private fun initView() {
        val question = viewModel.getQuestionByIndex(args.questionNumber)
        ivLocation.setImageResource(question.clue)

        btnNext.setOnClickListener {
            val action =
                LocationClueFragmentDirections.actionLocationClueFragmentToQuestionFragment(
                    viewModel.getQuestionIndexOfCurrent()
                )
            findNavController().navigate(action)
        }
    }

    private fun initViewModel() {
        viewModel.showLocationClue.apply {
            value = false
        }
    }

}
