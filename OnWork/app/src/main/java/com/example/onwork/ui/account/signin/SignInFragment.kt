package com.example.onwork.ui.account.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onwork.R
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_SignInFragment_to_SignUpFragment)
        }

        btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_dashboardFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }
}
