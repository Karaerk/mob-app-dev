package com.example.rockpaperscissors.model.helper

import androidx.room.TypeConverter
import com.example.rockpaperscissors.model.Action
import com.example.rockpaperscissors.model.GameResult
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromIndex(value: Int?): Action? {
        return value?.let {
            when (it) {
                1 -> Action.PAPER
                2 -> Action.SCISSORS
                else -> Action.ROCK
            }
        }
    }

    @TypeConverter
    fun actionToInt(action: Action?): Int? {
        return action?.let {
            when (it) {
                Action.PAPER -> 1
                Action.SCISSORS -> 2
                else -> 0
            }
        }
    }

    @TypeConverter
    fun fromResultInt(value: Int?): GameResult? {
        return value?.let {
            when (it) {
                1 -> GameResult.PLAYER_WON
                -1 -> GameResult.COMPUTER_WON
                else -> GameResult.DRAW
            }
        }
    }

    @TypeConverter
    fun resultToResultInt(action: GameResult?): Int? {
        return action?.let {
            when (it) {
                GameResult.PLAYER_WON -> 1
                GameResult.COMPUTER_WON -> -1
                else -> 0
            }
        }
    }
}