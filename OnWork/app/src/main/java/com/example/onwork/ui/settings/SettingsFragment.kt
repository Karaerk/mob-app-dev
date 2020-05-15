package com.example.onwork.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.onwork.R
import com.example.onwork.model.DateFormat
import com.wearetriple.tripleonboarding.extension.observeNonNull
import com.wearetriple.tripleonboarding.extension.observeNull
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
        settingsViewModel.dateFormat.observeNonNull(viewLifecycleOwner, this::initDateFormat)
        settingsViewModel.errorDelete.observeNonNull(viewLifecycleOwner, this::initErrorDelete)
        settingsViewModel.errorDateFormat.observeNonNull(
            viewLifecycleOwner,
            this::initErrorDateFormat
        )
    }

    /**
     * Prepares all the views inside this fragment.
     */
    private fun initViews() {
        val dateFormatAdapter = ArrayAdapter(
            activityContext,
            R.layout.item_dropdown_date,
            settingsViewModel.dateFormatStrings
        )

        etDateFormat.setAdapter(dateFormatAdapter)
        etDateFormat.setOnItemClickListener { _, _, i, _ ->
            settingsViewModel.updateDateFormat(i)
        }

        btnSignOut.setOnClickListener {
            settingsViewModel.signOut()
        }

        btnDeleteAccount.setOnClickListener {
            prepareDeleteAccount()
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
     * Prepares to have the user's preferred date format used as the selected option.
     */
    private fun initDateFormat(dateFormat: DateFormat?) {
        if (dateFormat != null)
            etDateFormat.setText(dateFormat.value.label, false)
    }

    /**
     * Prepares to show an error message to the user about delete account.
     */
    private fun initErrorDelete(hasError: Boolean) {
        if (hasError)
            Toast.makeText(
                activityContext,
                activityContext.getString(R.string.error_delete_account),
                Toast.LENGTH_SHORT
            ).show()
    }

    /**
     * Prepares to show an error message to the user about updating date format.
     */
    private fun initErrorDateFormat(hasError: Boolean) {
        if (hasError)
            Toast.makeText(
                activityContext,
                activityContext.getString(R.string.error_update_date_format),
                Toast.LENGTH_SHORT
            ).show()
    }

    /**
     * Prepares the app to delete the account by first asking the user if he's sure about it.
     */
    private fun prepareDeleteAccount() {
        val builder = AlertDialog.Builder(activityContext)

        builder.setMessage(getString(R.string.confirm_delete_account))
            .setPositiveButton(getString(R.string.button_delete)) { dialog, _ ->
                settingsViewModel.deleteAccount()
            }
            .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
                dialog.cancel()
            }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
}
