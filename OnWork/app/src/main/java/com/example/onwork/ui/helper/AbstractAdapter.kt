package com.example.onwork.ui.helper

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Used as a template for recyclerview's adapters.
 */
abstract class AbstractAdapter<E>(
    open val items: List<E>,
    open val clickListener: ((E) -> Unit)? = null
) : RecyclerView.Adapter<AbstractAdapter<E>.ViewHolder>() {

    /**
     * Prepares the view before passing it to the RecyclerView.
     */
    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: E)
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout
     */
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}