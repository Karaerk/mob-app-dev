package com.example.rockpaperscissors.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.model.Action
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.model.GameResult
import kotlinx.android.synthetic.main.item_history.view.*

class GameAdapter(private val games: List<Game>) :
    RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {
            val result = when (game.gameResult) {
                GameResult.COMPUTER_WON -> "Computer wins!"
                GameResult.PLAYER_WON -> "You win!"
                else -> "Draw"
            }

            val computerActionRes = when (game.computerAction) {
                Action.PAPER -> R.drawable.paper
                Action.SCISSORS -> R.drawable.scissors
                else -> R.drawable.rock
            }
            itemView.ivComputer.setImageResource(computerActionRes)
            val playerActionRes = when (game.playerAction) {
                Action.PAPER -> R.drawable.paper
                Action.SCISSORS -> R.drawable.scissors
                else -> R.drawable.rock
            }
            itemView.ivPlayer.setImageResource(playerActionRes)

            itemView.tvResult.text = result
            itemView.tvDate.text = game.date.toString()
        }
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called item_history.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    /**
     * Returns the size of the list.
     */
    override fun getItemCount(): Int {
        return games.size
    }

    /**
     * Called by RecyclerView to display to data at the specific position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }

}
