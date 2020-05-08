package com.example.onwork.ui.account.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onwork.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wearetriple.tripleonboarding.extension.observeNonNull
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    private lateinit var activityContext: AppCompatActivity
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activityContext = (activity as AppCompatActivity)
        activityContext.supportActionBar?.hide()

        initViewModel()
        initViews()
    }

    /**
     * Prepares the data needed for this fragment.
     */
    private fun initViewModel() {
        signInViewModel.isSignedIn.observeNonNull(viewLifecycleOwner, this::initAuthorizedUser)
        signInViewModel.success.observeNonNull(viewLifecycleOwner, this::initSuccess)
        signInViewModel.error.observeNonNull(viewLifecycleOwner, this::initErrorAuthentication)
    }

    /**
     * Prepares the views needed for this fragment.
     */
    private fun initViews() {
        btnSignUp.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        btnSignIn.setOnClickListener {
            validateInputs()

            if (isAllInputCorrect())
                signInViewModel.attemptSignIn(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    /**
     * Validates the user's input before attempting to sign in.
     */
    private fun validateInputs() {
        if (etEmail.text.isNullOrBlank()) {
            etEmail.error = activityContext.getString(R.string.error_input_email)
        }
        if (etPassword.text.isNullOrBlank()) {
            etPassword.error = activityContext.getString(R.string.error_input_password)
        }
    }

    /**
     * Prepares to let the user know it has successfully signed in.
     */
    private fun initSuccess(success: Boolean) {
        if (success) {
            Toast.makeText(
                activityContext,
                activityContext.getString(R.string.success_sign_in),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Prepares to send the authorized user to the next view.
     */
    private fun initAuthorizedUser(signedIn: Boolean) {
        if (signedIn) {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToDashboardFragment())
        }
    }

    /**
     * Prepares to show an error message to the user.
     */
    private fun initErrorAuthentication(hasError: Boolean) {
        if (hasError)
            Toast.makeText(
                activityContext,
                activityContext.getString(R.string.error_sign_in),
                Toast.LENGTH_SHORT
            ).show()
    }

    /**
     * Checks if all text fields don't have any error.
     */
    private fun isAllInputCorrect(): Boolean = etEmail.error.isNullOrEmpty()
            && etEmail.text!!.isNotBlank()
            && etPassword.error.isNullOrEmpty()
            && etPassword.text!!.isNotBlank()
}
