package com.example.onwork.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onwork.R
import com.example.onwork.model.TimeEntry
import com.example.onwork.ui.helper.AbstractAdapter
import kotlinx.android.synthetic.main.item_time_entry.view.*

class TimeEntryAdapter (
override var items: ArrayList<TimeEntry>,
override val clickListener: (TimeEntry) -> Unit
) : AbstractAdapter<TimeEntry>(items, clickListener) {

    /**
     * Prepares the view before passing it to the RecyclerView.
     */
    inner class ViewHolder(itemView: View) : AbstractAdapter<TimeEntry>.ViewHolder(itemView) {

        override fun bind(item: TimeEntry) {
            itemView.tvTitle.text = item.title
            itemView.setOnClickListener { clickListener(item) }
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