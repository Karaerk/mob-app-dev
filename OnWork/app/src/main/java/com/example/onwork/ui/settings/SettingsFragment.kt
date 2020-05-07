package com.example.onwork.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.onwork.R
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private lateinit var activityContext: AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activityContext = (activity as AppCompatActivity)
        activityContext.supportActionBar?.show()

        initViews()
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
            filled_exposed_dropdown
        editTextFilledExposedDropdown.setAdapter(adapter)
    }
}
