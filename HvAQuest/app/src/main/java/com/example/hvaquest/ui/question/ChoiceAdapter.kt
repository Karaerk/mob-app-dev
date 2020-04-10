package com.example.hvaquest.ui.question

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.hvaquest.R
import kotlinx.android.synthetic.main.item_choice.view.*

class ChoiceAdapter(
    private val choices: Array<String>,
    private val onClick: (RadioButton) -> Unit
) : RecyclerView.Adapter<ChoiceAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_choice, parent, false)
        )
    }

    override fun getItemCount(): Int = choices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(choices[position])

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.rbAnswer.setOnClickListener {
                onClick(itemView.rbAnswer)
            }
        }

        fun bind(choice: String) {
            itemView.rbAnswer.text = choice
        }
    }

}