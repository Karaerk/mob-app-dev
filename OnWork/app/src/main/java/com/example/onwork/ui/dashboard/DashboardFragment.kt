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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onwork.R
import com.example.onwork.model.DateFormat
import com.example.onwork.model.TimeEntry
import com.example.onwork.ui.helper.DateTime
import com.example.onwork.extension.observeNonNull
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_time_entry.*
import java.util.*


class DashboardFragment : Fragment() {

    private lateinit var activityContext: AppCompatActivity
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var timeEntryAdapter: TimeEntryAdapter
    private val timeEntries = arrayListOf<TimeEntry>()

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
            timeEntries,
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
        createItemTouchHelper().attachToRecyclerView(rvTimeEntries)

        tvDate.text = getString(R.string.title_on_going)

        fabEntry.setOnClickListener {
            if (etNewEntry.text!!.isBlank() && etNewEntry.isEnabled)
                etNewEntry.error = getString(R.string.error_input_new_entry)
            else {
                actionButtonTriggered()
            }
        }
    }

    /**
     * Prepares the data needed for this fragment.
     */
    private fun initViewModel() {
        dashboardViewModel.dateFormat.observeNonNull(viewLifecycleOwner, this::initDateFormat)

        dashboardViewModel.timeEntries.observeNonNull(
            viewLifecycleOwner,
            this::initTimeEntries
        )

        dashboardViewModel.updateOnGoingTimeEntry.observeNonNull(
            viewLifecycleOwner,
            this::initOnGoingTimeEntry
        )
    }

    /**
     * Prepares the recylerview full of time entries.
     */
    private fun initTimeEntries(timeEntries: List<TimeEntry>) {
        this.timeEntries.clear()
        this.timeEntries.addAll(timeEntries)
        timeEntryAdapter.notifyDataSetChanged()
    }

    /**
     * Initializes the on going block's time entry.
     */
    private fun initOnGoingTimeEntry(updated: Boolean) {
        if (updated) {
            val timePattern = "HH:mm"
            val timeEntry = dashboardViewModel.onGoingTimeEntry.value
            tvTime.text = getString(
                R.string.label_start_time,
                DateTime.getDateFormatted(timeEntry!!.startTime, timePattern)
            )
            tvTitle.text = timeEntry.title

            fabEntry.setImageDrawable(activityContext.getDrawable(R.drawable.ic_stop_white_24dp))
            iOnGoing.visibility = View.VISIBLE
            etNewEntry.isEnabled = false
        } else {
            fabEntry.setImageDrawable(activityContext.getDrawable(R.drawable.ic_play_arrow_white_24dp))
            iOnGoing.visibility = View.GONE
            etNewEntry.isEnabled = true
        }
    }

    /**
     * Triggers a new on going time entry event.
     */
    private fun actionButtonTriggered() {
        if (dashboardViewModel.isOnGoingPresent()) {
            dashboardViewModel.updateOnGoingTimeEntry(etNewEntry.text.toString())
        } else {
            dashboardViewModel.endOnGoingTimeEntry()
            dashboardViewModel.updateOnGoingTimeEntry.value = false
            dashboardViewModel.onGoingTimeEntry.value = null
        }
        etNewEntry.text?.clear()
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

    /**
     * Deletes the whole history of the user.
     */
    private fun deleteHistory() {
        dashboardViewModel.deleteAllTimeEntries()
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
            R.id.action_delete_history -> {
                deleteHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val timeEntry = timeEntries[position]

                //TODO: Display snackbar to undo deletion
//                Snackbar.make(toolbar, getString(R.string.text_deleted_game), Snackbar.LENGTH_LONG)
//                    .setAction("Undo", UndoListener()).show()

                dashboardViewModel.deleteTimeEntry(timeEntry)
            }
        }
        return ItemTouchHelper(callback)
    }
}