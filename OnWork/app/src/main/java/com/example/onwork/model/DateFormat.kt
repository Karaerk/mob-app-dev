package com.example.onwork.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "DateFormat")
data class DateFormat(
    @ColumnInfo(name = "value")
    var value: DateFormatEnum,

    @ColumnInfo(name = "lastUpdated")
    var lastUpdated: Date = Date(),

    @ColumnInfo(name = "userEmail")
    val userEmail: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
) : Parcelable