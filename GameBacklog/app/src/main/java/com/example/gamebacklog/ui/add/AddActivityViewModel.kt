package com.example.gamebacklog.ui.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.gamebacklog.database.GameRepository
import com.example.gamebacklog.model.Game
import com.example.gamebacklog.model.NewGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val gameRepository = GameRepository(application.applicationContext)

    val newGame = MutableLiveData<NewGame?>()
    val error = MutableLiveData<String?>()
    val success = MutableLiveData<Boolean>()

    /**
     * Inserts the new game as a game in the database after going through validation.
     */
    fun insertGame() {
        if (isNewGameValid()) {
            val date = Calendar.getInstance()
            date.set(
                newGame.value!!.year!!,
                newGame.value!!.month!! - 1,
                newGame.value!!.day!!
            )

            val game = Game(newGame.value!!.title, newGame.value!!.platform, date.time)

            mainScope.launch {
                withContext(Dispatchers.IO) {
                    gameRepository.insertGame(game)
                }
                success.value = true
            }
        }
    }

    /**
     * Lets the new game go through a thorough validation to make sure the data is correct.
     */
    fun isNewGameValid(): Boolean {
        return when {
            newGame.value == null -> {
                error.value = "Game must not be null"
                false
            }
            isFormBlank() -> {
                false
            }
            !isDateValid() -> {
                false
            }
            else -> true
        }
    }

    /**
     * Checks if any field is blank.
     */
    fun isFormBlank(): Boolean {
        return when {
            newGame.value!!.title.isBlank() -> {
                error.value = "Title must not be empty"
                true
            }
            newGame.value!!.platform.isBlank() -> {
                error.value = "Platform must not be empty"
                true
            }
            newGame.value!!.day == null -> {
                error.value = "Day must not be empty"
                true
            }
            newGame.value!!.month == null -> {
                error.value = "Month must not be empty"
                true
            }
            newGame.value!!.year == null -> {
                error.value = "Year must not be empty"
                true
            }
            else -> false
        }
    }

    /**
     * Checks if the date is after today and if the date's day and month is between its range.
     */
    fun isDateValid(): Boolean {
        val day = newGame.value!!.day!!
        val month = newGame.value!!.month!!
        val year = newGame.value!!.year!!

        return when {
            day < START_DAY_OF_MONTH || day > END_DAY_OF_MONTH -> {
                error.value = "Day must be between $START_DAY_OF_MONTH and $END_DAY_OF_MONTH"
                false
            }
            month < START_MONTH_OF_YEAR || month > END_MONTH_OF_YEAR -> {
                error.value = "Month must be between $START_MONTH_OF_YEAR and $END_MONTH_OF_YEAR"
                false
            }
            else -> {
                val date = Calendar.getInstance()
                date.set(
                    year,
                    month - 1,
                    day,
                    0,
                    0,
                    0
                ) // Make sure the time of the day won't cause unsuspected behaviour

                if (date.time.before(Date())) {
                    error.value = "Date must be after today"
                    return false
                }
                return true
            }
        }
    }

    companion object {
        const val START_DAY_OF_MONTH = 1
        const val END_DAY_OF_MONTH = 31
        const val START_MONTH_OF_YEAR = 1
        const val END_MONTH_OF_YEAR = 12
    }
}