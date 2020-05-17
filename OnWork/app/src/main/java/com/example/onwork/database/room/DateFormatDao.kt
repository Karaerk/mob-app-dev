package com.example.onwork.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.onwork.model.DateFormat

@Dao
interface DateFormatDao {

    @Insert
    suspend fun insertDateFormat(dateFormat: DateFormat)

    @Query("SELECT * FROM DateFormat WHERE userEmail = :userEmail LIMIT 1")
    fun getDateFormat(userEmail: String): LiveData<DateFormat>

    @Query("DELETE FROM DateFormat WHERE userEmail = :userEmail")
    suspend fun deleteAllTimeEntries(userEmail: String)

    @Update
    suspend fun updateDateFormat(dateFormat: DateFormat)
}