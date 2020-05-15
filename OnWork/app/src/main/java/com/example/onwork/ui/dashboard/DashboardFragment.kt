package com.example.onwork.ui.dashboard

import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onwork.R
import com.example.onwork.model.DateFormat
import com.example.onwork.model.TimeEntry
import com.wearetriple.tripleonboarding.extension.observeNonNull
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_time_entry.*
import java.time.LocalDate
import java.util.*


class DashboardFragment : Fragment() {

    private lateinit var activityContext: AppCompatActivity
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var timeEntryAdapter: TimeEntryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activityContext = (activity as AppCompatActivity)
        activityContext.supportActionBar?.show()

        initViews()
        initViewModel()
    }

    /**
     * Prepares all the views inside this fragment.
     */
    private fun initViews() {
        //TODO: remove this test data
        val cal = Calendar.getInstance()
        cal[Calendar.MINUTE] = 149
        cal[Calendar.SECOND] = 32
        val endTime = cal.time
        cal[Calendar.MINUTE] = 100
        val endTimeTwo = cal.time
        timeEntryAdapter = TimeEntryAdapter(
            arrayListOf(
                TimeEntry(
                    "test",
                    "test@test.com",
                    Date(),
                    endTime
                ),
                TimeEntry(
                    "test 2",
                    "test@test.com",
                    Date(),
                    endTimeTwo
                )
            ),
            { timeEntry: TimeEntry ->
                timeEntryClicked(
                    timeEntry
                )
            },
            dashboardViewModel.getDateFormat()
        )

        rvTimeEntries.layoutManager =
            LinearLayoutManager(activityContext, RecyclerView.VERTICAL, false)
        rvTimeEntries.adapter = timeEntryAdapter

        tvDate.text = getString(R.string.title_on_going)
        tvTime.text = getString(R.string.label_start_time, "10:45")

        fabEntry.setOnClickListener {
            fabEntry.setImageDrawable(activityContext.getDrawable(R.drawable.ic_stop_white_24dp))
            iOnGoing.visibility = View.VISIBLE
            tvTitle.text = etNewEntry.text
            etNewEntry.text?.clear()
        }
    }

    /**
     * Prepares the data needed for this fragment.
     */
    private fun initViewModel() {
        dashboardViewModel.dateFormat.observeNonNull(viewLifecycleOwner, this::initDateFormat)
    }

    /**
     * Prepares to have the user's preferred date format used as the selected option.
     */
    private fun initDateFormat(dateFormat: DateFormat) {
        timeEntryAdapter.dateFormat = dateFormat
        timeEntryAdapter.notifyDataSetChanged()
    }

    /**
     * Opens up a pop-up with details included about the clicked time entry.
     */
    private fun timeEntryClicked(timeEntry: TimeEntry) {
        val timeEntryResult = dashboardViewModel.getTimeEntryResult(timeEntry)
        val viewInflated: View = LayoutInflater.from(activityContext)
            .inflate(R.layout.item_dialog, view as ViewGroup?, false)
        viewInflated.findViewById<TextView>(R.id.tvItemDate).text = timeEntryResult.date
        viewInflated.findViewById<TextView>(R.id.tvItemDuration).text = timeEntryResult.duration
        viewInflated.findViewById<EditText>(R.id.etStartTime).setText(timeEntryResult.startTime)
        viewInflated.findViewById<EditText>(R.id.etEndTime).setText(timeEntryResult.endTime)

        val builder: AlertDialog.Builder = AlertDialog.Builder(activityContext)
        builder.setTitle(timeEntry.title)
            .setView(viewInflated)
            .setPositiveButton(
                R.string.action_save
            ) { dialog, _ ->
                dialog.dismiss()

            }
            .setNegativeButton(
                R.string.action_cancel
            ) { dialog, _ -> dialog.cancel() }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_history -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
