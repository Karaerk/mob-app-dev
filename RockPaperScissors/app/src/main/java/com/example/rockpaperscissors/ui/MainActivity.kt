package com.example.rockpaperscissors.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.database.GameRepository
import com.example.rockpaperscissors.model.Action
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.model.GameResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val DELETE_HISTORY_REQUEST_CODE = 100

class MainActivity : AppCompatActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameRepository = GameRepository(this)
        initViews()
    }

    /**
     * Gets called when a result is retrieved from another Activity.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                DELETE_HISTORY_REQUEST_CODE -> {
                    getStatistics()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initViews() {

        getStatistics()

        ibRock.setOnClickListener {
            startGame(Action.ROCK)
        }
        ibPaper.setOnClickListener {
            startGame(Action.PAPER)
        }
        ibScissors.setOnClickListener {
            startGame(Action.SCISSORS)
        }
    }

    private fun getStatistics() {
        mainScope.launch {
            val countWin = withContext(Dispatchers.IO) {
                gameRepository.countPlayerWin()
            }
            val countDraw = withContext(Dispatchers.IO) {
                gameRepository.countDraw()
            }
            val countLose = withContext(Dispatchers.IO) {
                gameRepository.countPlayerLose()
            }

            tvStatsData.text = getString(R.string.data_stats, countWin, countDraw, countLose)
        }
    }

    /**
     * Starts a game between the computer and the player.
     */
    private fun startGame(playerAction: Action) {
        val computerAction = randomAction()
        val computerActionRes = when (computerAction) {
            Action.PAPER -> R.drawable.paper
            Action.SCISSORS -> R.drawable.scissors
            else -> R.drawable.rock
        }
        ivComputer.setImageResource(computerActionRes)

        val playerActionRes = when (playerAction) {
            Action.PAPER -> R.drawable.paper
            Action.SCISSORS -> R.drawable.scissors
            else -> R.drawable.rock
        }
        ivPlayer.setImageResource(playerActionRes)

        gameResult(computerAction, playerAction)
    }

    /**
     * Shows the result of the game to the user.
     */
    private fun gameResult(computer: Action, player: Action) {
        var result = GameResult.DRAW
        var resultText = getString(R.string.game_draw)

        if (player.winsFrom == computer.toString()) {
            result = GameResult.PLAYER_WON
            resultText = getString(R.string.game_win)
        } else if (computer.winsFrom == player.toString()) {
            result = GameResult.COMPUTER_WON
            resultText = getString(R.string.game_lose)
        }

        tvResult.text = resultText

        mainScope.launch {
            val game = Game(
                date = Date(),
                computerAction = computer,
                playerAction = player,
                gameResult = result
            )

            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
        }

        getStatistics()
    }

    /**
     * Lets the computer give its action for currently ongoing game.
     */
    private fun randomAction(): Action {
        val actions = Action.values()
        val randomIndex = (actions.indices).random()

        return actions[randomIndex]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_go_to_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivityForResult(intent, DELETE_HISTORY_REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
