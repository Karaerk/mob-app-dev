package com.example.gamebacklog.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebacklog.R
import com.example.gamebacklog.model.Game
import com.example.gamebacklog.ui.add.AddActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val games = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(games)
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvGames.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rvGames.adapter = gameAdapter
        createItemTouchHelper().attachToRecyclerView(rvGames)

        fab.setOnClickListener {
            startActivity()
        }
    }

    private fun initViewModel() {
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        mainActivityViewModel.games.observe(this, Observer { games ->
            this@MainActivity.games.clear()
            this@MainActivity.games.addAll(games)
            gameAdapter.notifyDataSetChanged()
        })
    }

    private fun startActivity() {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    private fun deleteHistory() {
        mainActivityViewModel.undoGames.value = mainActivityViewModel.games.value

        //TODO: Display snackbar to undo deletion
//        Snackbar
//            .make(toolbar, getString(R.string.text_deleted_backlog), Snackbar.LENGTH_LONG)
//            .setAction("Undo", UndoListener())
//            .addCallback(object : Snackbar.Callback() {
//                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//                    super.onDismissed(transientBottomBar, event)
//
//                    when (event) {
//                        DISMISS_EVENT_TIMEOUT,
//                        DISMISS_EVENT_SWIPE,
//                        DISMISS_EVENT_CONSECUTIVE,
//                        DISMISS_EVENT_MANUAL -> mainActivityViewModel.deleteAllGames()
//                        else -> return
//                    }
//                }
//            })
//            .show()

        mainActivityViewModel.deleteAllGames()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

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
                val game = games[position]

                //TODO: Display snackbar to undo deletion
//                Snackbar.make(toolbar, getString(R.string.text_deleted_game), Snackbar.LENGTH_LONG)
//                    .setAction("Undo", UndoListener()).show()

                mainActivityViewModel.deleteGame(game)
            }
        }
        return ItemTouchHelper(callback)
    }
}
