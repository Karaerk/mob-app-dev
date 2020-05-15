package com.example.onwork.ui.dashboard

import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onwork.R
import com.example.onwork.model.TimeEntry
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_dialog.*
import kotlinx.android.synthetic.main.item_time_entry.*
import kotlinx.android.synthetic.main.item_time_entry.tvDate
import kotlinx.android.synthetic.main.item_time_entry.tvTitle


class DashboardFragment : Fragment() {

    private lateinit var activityContext: AppCompatActivity
    private val timeEntryAdapter =
        TimeEntryAdapter(
            arrayListOf(
                TimeEntry("test", "test@test.com"),
                TimeEntry("test", "test@test.com")
            )
        ) { timeEntry: TimeEntry ->
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

        setHasOptionsMenu(true)
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
     * Opens up a pop-up with details included about the clicked time entry.
     */
    private fun timeEntryClicked(timeEntry: TimeEntry) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activityContext)
        builder.setTitle(timeEntry.title)
        val viewInflated: View = LayoutInflater.from(activityContext)
            .inflate(R.layout.item_dialog, view as ViewGroup?, false)
//        val input = viewInflated.findViewById(R.id.input) as EditText
        builder.setView(viewInflated)
        viewInflated.findViewById<TextView>(R.id.tvItemDate).text = "test 1"
        viewInflated.findViewById<TextView>(R.id.tvItemDuration).text = "test 2"
        viewInflated.findViewById<EditText>(R.id.etStartTime).setText("12:34")
        viewInflated.findViewById<EditText>(R.id.etEndTime).setText("56:78")

        builder.setPositiveButton(
            R.string.action_save
        ) { dialog, _ ->
            dialog.dismiss()

        }
        builder.setNegativeButton(
            R.string.action_cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
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
