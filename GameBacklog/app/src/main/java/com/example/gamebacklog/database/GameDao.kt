package com.example.gamebacklog.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gamebacklog.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM game ORDER BY date")
    fun getAllGames(): LiveData<List<Game>>

    @Insert
    suspend fun insertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("DELETE FROM game")
    suspend fun deleteAllGames()
}