package com.example.onwork.database.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.onwork.model.DateFormat

class DateFormatRepository(context: Context) {

    private val dateFormatDao: DateFormatDao

    init {
        val database =
            DateFormatRoomDatabase.getDatabase(
                context
            )
        dateFormatDao = database!!.dateFormatDao()
    }

    suspend fun insertDateFormat(dateFormat: DateFormat) =
        dateFormatDao.insertDateFormat(dateFormat)

    fun getDateFormat(userEmail: String): LiveData<DateFormat> =
        dateFormatDao.getDateFormat(userEmail)

    suspend fun updateDateFormat(dateFormat: DateFormat) =
        dateFormatDao.updateDateFormat(dateFormat)
}