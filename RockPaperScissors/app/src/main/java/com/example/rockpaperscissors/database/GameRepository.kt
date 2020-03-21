package com.example.rockpaperscissors.database

import android.content.Context
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.model.GameResult

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database =
            GameRoomDatabase.getDatabase(
                context
            )
        gameDao = database!!.gameDao()
    }

    suspend fun getAllGames(): List<Game> = gameDao.getAllGames()

    suspend fun insertGame(game: Game) = gameDao.insertGame(game)

    suspend fun deleteGame(game: Game) = gameDao.deleteGame(game)

    suspend fun deleteAllGames() = gameDao.deleteAllGames()

    suspend fun countPlayerLose(): Int = gameDao.countPlayerLose(GameResult.COMPUTER_WON)

    suspend fun countPlayerWin(): Int = gameDao.countPlayerWin(GameResult.PLAYER_WON)

    suspend fun countDraw(): Int = gameDao.countDraw(GameResult.DRAW)
}