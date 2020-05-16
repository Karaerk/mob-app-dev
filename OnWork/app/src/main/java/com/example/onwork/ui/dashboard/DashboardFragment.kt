package com.example.onwork.ui.dashboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onwork.R
import com.example.onwork.extension.observeNonNull
import com.example.onwork.model.DateFormat
import com.example.onwork.model.TimeEntry
import com.example.onwork.ui.helper.DateTime
import com.google.android.material.snackbar.Snackbar
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
        this.timeEntries.sort()
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
        val etStartTime = viewInflated.findViewById<EditText>(R.id.etStartTime)
        val etEndTime = viewInflated.findViewById<EditText>(R.id.etEndTime)
        etStartTime.setText(timeEntryResult.startTime)
        etEndTime.setText(timeEntryResult.endTime)

        val builder: AlertDialog.Builder = AlertDialog.Builder(activityContext)
        builder.setTitle(timeEntry.title)
            .setView(viewInflated)
            .setPositiveButton(
                R.string.action_save
            ) { _, _ -> }
            .setNegativeButton(
                R.string.action_cancel
            ) { dialog, _ -> dialog.cancel() }
            .create()
        val dialog = builder.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (isTimeEntryUpdated(etStartTime, etEndTime, timeEntry))
                dialog.dismiss()
        }

        etStartTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                if (isValidTimeInput(p0)) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    etStartTime.error = getString(R.string.error_input_start_time)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        etEndTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                if (isValidTimeInput(p0)) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    etEndTime.error = getString(R.string.error_input_end_time)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    /**
     * Validates the user input for a time.
     */
    private fun isValidTimeInput(input: Editable): Boolean {
        val timeDelimiter = ":"
        val minTimeLength = 3
        val maxTimeLength = 5
        val delimiterSides = 2
        val maxHour = 23
        val minHour = 0
        val maxMinute = 59
        val minMinute = 0

        if (input.isBlank() || input.length < minTimeLength || !input.contains(timeDelimiter)) {
            return false
        } else if (input.contains(timeDelimiter)) {
            val timeSplitted = input.split(timeDelimiter)

            if (timeSplitted.size != delimiterSides || timeSplitted[0].isEmpty() || timeSplitted[1].isEmpty())
                return false

            val hour = timeSplitted[0].toInt()
            val minute = timeSplitted[1].toInt()

            if (hour < minHour || hour > maxHour)
                return false

            if (minute < minMinute || minute > maxMinute)
                return false

            if (input.length > maxTimeLength)
                return false
        }

        return true
    }

    /**
     * Attempts to update the time entry's starting and ending time.
     */
    private fun isTimeEntryUpdated(
        etStartTime: EditText,
        etEndTime: EditText,
        timeEntry: TimeEntry
    ): Boolean {
        val timeDelimiter = ":"

        val startTimeSplitted = etStartTime.text.split(timeDelimiter)
        val startTime = Calendar.getInstance()
        startTime.time = timeEntry.startTime
        startTime[Calendar.HOUR_OF_DAY] = startTimeSplitted[0].toInt()
        startTime[Calendar.MINUTE] = startTimeSplitted[1].toInt()

        val endTimeSplitted = etEndTime.text.split(timeDelimiter)
        val endTime = Calendar.getInstance()
        endTime.time = timeEntry.endTime!!
        endTime[Calendar.HOUR_OF_DAY] = endTimeSplitted[0].toInt()
        endTime[Calendar.MINUTE] = endTimeSplitted[1].toInt()

        if (!startTime.time.before(endTime.time)) {
            Toast.makeText(
                activityContext,
                getString(R.string.error_input_times_not_logic),
                Toast.LENGTH_LONG
            ).show()
            etStartTime.error = getString(R.string.error_input_start_time)
            etEndTime.error = getString(R.string.error_input_end_time)
            return false
        } else {
            val position = timeEntries.indexOf(timeEntry)
            val temp = timeEntry.copy()

            timeEntries[position].startTime = startTime.time
            timeEntries[position].endTime = endTime.time
            timeEntryAdapter.notifyDataSetChanged()

            val snackbar = Snackbar.make(
                rvTimeEntries,
                getString(R.string.success_update_time_entry, timeEntry.title),
                Snackbar.LENGTH_LONG
            )
                .setAction(getString(R.string.action_undo)) {
                    timeEntries[position].startTime = temp.startTime
                    timeEntries[position].endTime = temp.endTime
                    timeEntryAdapter.notifyDataSetChanged()
                }
            snackbar.show()
            snackbar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (event == DISMISS_EVENT_TIMEOUT) {
                        dashboardViewModel.updateTimeEntry(
                            etStartTime.text.toString(),
                            etEndTime.text.toString(),
                            timeEntry
                        )
                    }
                }
            })
            return true
        }
    }

    /**
     * Deletes the whole history of the user.
     */
    private fun deleteHistory() {
        val temp = timeEntries.toList()
        timeEntryAdapter.items.clear()
        timeEntryAdapter.notifyDataSetChanged()

        val snackbar = Snackbar.make(
            rvTimeEntries,
            getString(R.string.success_delete_history),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.action_undo)) { _ ->
                timeEntryAdapter.items.addAll(temp)
                timeEntryAdapter.notifyDataSetChanged()
            }
        snackbar.show()
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (event == DISMISS_EVENT_TIMEOUT) {
                    dashboardViewModel.deleteAllTimeEntries()
                }
            }
        })


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

                deleteTimeEntry(timeEntry)
            }
        }
        return ItemTouchHelper(callback)
    }

    /**
     * Deletes a time entry.
     */
    private fun deleteTimeEntry(timeEntry: TimeEntry) {
        timeEntries.remove(timeEntry)
        timeEntryAdapter.notifyDataSetChanged()

        val snackbar = Snackbar.make(
            rvTimeEntries,
            getString(R.string.success_delete_time_entry, timeEntry.title),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.action_undo)) {
                timeEntries.add(timeEntry)
                timeEntryAdapter.notifyDataSetChanged()
            }
        snackbar.show()
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (event == DISMISS_EVENT_TIMEOUT) {
                    dashboardViewModel.deleteTimeEntry(timeEntry)
                }
            }
        })
    }
}