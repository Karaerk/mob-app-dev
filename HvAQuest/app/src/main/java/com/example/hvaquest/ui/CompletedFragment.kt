package com.example.hvaquest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.hvaquest.R
import com.example.hvaquest.ui.question.QuestionViewModel
import kotlinx.android.synthetic.main.fragment_completed.*

/**
 * A simple [Fragment] subclass.
 */
class CompletedFragment : Fragment() {

    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(activity as AppCompatActivity).get(QuestionViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initView()
    }

    private fun initView() {
        btnFinish.setOnClickListener {
            findNavController().navigate(R.id.action_completedFragment_to_homeFragment)
        }
    }

    private fun initViewModel() {
        viewModel.gameOver.apply {
            value = false
        }
        viewModel.resetGame()
    }

}
