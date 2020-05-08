package com.example.onwork.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onwork.R
import com.wearetriple.tripleonboarding.extension.observeNonNull
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private lateinit var activityContext: AppCompatActivity
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activityContext = (activity as AppCompatActivity)
        activityContext.supportActionBar?.show()

        initViewModel()
        initViews()
    }

    /**
     * Prepares the data needed for this fragment.
     */
    private fun initViewModel() {
        settingsViewModel.deleteAccount.observeNonNull(viewLifecycleOwner, this::initDeleteAccount)
        settingsViewModel.signOut.observeNonNull(viewLifecycleOwner, this::initSignOut)
        settingsViewModel.errorDelete.observeNonNull(viewLifecycleOwner, this::initErrorDelete)
    }

    /**
     * Prepares all the views inside this fragment.
     */
    private fun initViews() {
        val COUNTRIES =
            arrayOf("Item 1", "Item 2", "Item 3", "Item 4")

        val adapter = ArrayAdapter(
            activityContext,
            R.layout.item_dropdown_date,
            COUNTRIES
        )

        val editTextFilledExposedDropdown: AutoCompleteTextView =
            etDateFormat
        editTextFilledExposedDropdown.setAdapter(adapter)

        btnSignOut.setOnClickListener {
            settingsViewModel.signOut()
        }

        btnDeleteAccount.setOnClickListener {
            settingsViewModel.deleteAccount()
        }
    }

    /**
     * Prepares to send the deleted user to the sign in view.
     */
    private fun initDeleteAccount(deleted: Boolean) {
        if (deleted) {
            findNavController().navigate(R.id.action_settingsFragment_to_signInFragment)

            Toast.makeText(
                activityContext,
                activityContext.getString(R.string.success_delete_account),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Prepares to send the just signed out user to the sign in view.
     */
    private fun initSignOut(signedOut: Boolean) {
        if (signedOut)
            findNavController().navigate(R.id.action_settingsFragment_to_signInFragment)
    }

    /**
     * Prepares to show an error message to the user.
     */
    private fun initErrorDelete(hasError: Boolean) {
        if (hasError)
            Toast.makeText(
                activityContext,
                activityContext.getString(R.string.error_delete_account),
                Toast.LENGTH_SHORT
            ).show()
    }
}
