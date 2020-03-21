package com.example.rockpaperscissors.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.model.helper.Converters

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GameRoomDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        private const val DATABASE_NAME = "GAME_DATABASE"

        @Volatile
        private var gameDatabaseInstance: GameRoomDatabase? = null

        fun getDatabase(context: Context): GameRoomDatabase? {
            if (gameDatabaseInstance == null) {
                synchronized(GameRoomDatabase::class.java) {
                    if (gameDatabaseInstance == null) {
                        gameDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            GameRoomDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return gameDatabaseInstance
        }
    }
}