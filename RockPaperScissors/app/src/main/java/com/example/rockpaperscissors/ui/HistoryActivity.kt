package com.example.rockpaperscissors.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.database.GameRepository
import com.example.rockpaperscissors.model.Game
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val DELETE_HISTORY = "DELETE_HISTORY"

class HistoryActivity : AppCompatActivity() {

    private val games = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(games)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        gameRepository = GameRepository(this)
        initViews()
    }

    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_history)

        rvGames.layoutManager =
            LinearLayoutManager(this@HistoryActivity, RecyclerView.VERTICAL, false)
        rvGames.adapter = gameAdapter
        rvGames.addItemDecoration(
            DividerItemDecoration(
                this@HistoryActivity,
                DividerItemDecoration.VERTICAL
            )
        )
        createItemTouchHelper().attachToRecyclerView(rvGames)
        getGamesFromDatabase()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_delete_history -> {
                deleteHistory()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun deleteHistory() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAllGames()
            }
            getGamesFromDatabase()
        }

        setResult(Activity.RESULT_OK, Intent())
    }

    private fun getGamesFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val games = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }
            this@HistoryActivity.games.clear()
            this@HistoryActivity.games.addAll(games)
            this@HistoryActivity.gameAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Recognizes when a user swipes an item from a recycler view.
     * Enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val gameToDelete = games[position]
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        gameRepository.deleteGame(gameToDelete)
                    }
                    getGamesFromDatabase()
                    setResult(Activity.RESULT_OK, Intent())
                }
            }
        }

        return ItemTouchHelper(callback)
    }
}
