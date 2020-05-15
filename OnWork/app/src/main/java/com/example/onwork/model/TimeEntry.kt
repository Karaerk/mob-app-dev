package com.example.onwork.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "TimeEntry")
data class TimeEntry(
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "userEmail")
    val userEmail: String,

    @ColumnInfo(name = "startTime")
    var startTime: Date = Date(),

    @ColumnInfo(name = "endTime")
    var endTime: Date? = null,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
) : Parcelable