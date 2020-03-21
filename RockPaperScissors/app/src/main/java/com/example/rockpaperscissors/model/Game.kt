package com.example.rockpaperscissors.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "game_table")
data class Game(
    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "computer_action")
    val computerAction: Action,

    @ColumnInfo(name = "player_action")
    val playerAction: Action,

    @ColumnInfo(name = "result")
    val gameResult: GameResult,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
) : Parcelable