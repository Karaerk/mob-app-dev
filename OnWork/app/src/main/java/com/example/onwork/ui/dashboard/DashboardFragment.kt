package com.example.onwork.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onwork.R
import com.example.onwork.model.TimeEntry
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var activityContext: AppCompatActivity
    private val timeEntryAdapter =
        TimeEntryAdapter(arrayListOf(TimeEntry("test"),TimeEntry("test"))) { timeEntry: TimeEntry ->
            timeEntryClicked(
                timeEntry
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
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
        rvTimeEntries.layoutManager = LinearLayoutManager(activityContext, RecyclerView.VERTICAL, false)
        rvTimeEntries.adapter = timeEntryAdapter

        fabEntry.setOnClickListener {
            fabEntry.setImageDrawable(activityContext.getDrawable(R.drawable.ic_stop_white_24dp))
        }
    }

    /**
     * Opens up a pop-up with details included about the clicked time entry.
     */
    private fun timeEntryClicked(timeEntry: TimeEntry) {

    }
}
