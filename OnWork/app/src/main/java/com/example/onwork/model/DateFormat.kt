package com.example.onwork.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "DateFormat")
data class DateFormat(
    @ColumnInfo(name = "value")
    var value: DateFormatEnum = DateFormatEnum.MM_DD_YYYY_FORWARD_SLASH,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "userEmail")
    override val userEmail: String = ""

) : Parcelable, IdentifiableUser {
    companion object : IdentifiableTable {
        override fun getDatabaseName() = "dateFormat"
    }
}