package com.example.rockpaperscissors.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.model.GameResult

@Dao
interface GameDao {

    @Query("SELECT * FROM game_table")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("DELETE FROM game_table")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT(result) FROM game_table WHERE result = :gameResult")
    suspend fun countResult(gameResult: GameResult): Int
}