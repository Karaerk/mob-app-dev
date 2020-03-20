package com.example.shoppinglist

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "product_table")
class Product(
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
) : Parcelable