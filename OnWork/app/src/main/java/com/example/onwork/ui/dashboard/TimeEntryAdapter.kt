package com.example.onwork.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onwork.R
import com.example.onwork.model.DateFormat
import com.example.onwork.model.TimeEntry
import com.example.onwork.ui.helper.AbstractAdapter
import com.example.onwork.ui.helper.DateTime
import kotlinx.android.synthetic.main.item_time_entry.view.*
import java.text.SimpleDateFormat
import java.util.*

class TimeEntryAdapter(
    override var items: ArrayList<TimeEntry>,
    override val clickListener: (TimeEntry) -> Unit,
    var dateFormat: DateFormat
) : AbstractAdapter<TimeEntry>(items, clickListener) {

    lateinit var test: View
    lateinit var testTwo: TimeEntry

    /**
     * Prepares the view before passing it to the RecyclerView.
     */
    inner class ViewHolder(itemView: View) : AbstractAdapter<TimeEntry>.ViewHolder(itemView) {

        override fun bind(item: TimeEntry) {
            itemView.tvTitle.text = item.title
            itemView.tvDate.text =
                DateTime.getDateFormatted(item.startTime, dateFormat.value.format, true)
            itemView.tvTime.text =
                "${getTimeFormatted(item.startTime)} - ${getTimeFormatted(item.endTime!!)}"
            itemView.tvDuration.text = DateTime.calculateDifference(item.startTime, item.endTime!!)
            itemView.setOnClickListener { clickListener(item) }
            itemView.cvEntry.setOnClickListener { clickListener(item) }
        }

        private fun getTimeFormatted(time: Date): String {
            val timePattern = "HH:mm"
            return time.let {
                val simpleDateFormat = SimpleDateFormat(timePattern, Locale.ROOT)
                simpleDateFormat.format(time)
            }
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractAdapter<TimeEntry>.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_time_entry, parent, false)
        )
    }

}