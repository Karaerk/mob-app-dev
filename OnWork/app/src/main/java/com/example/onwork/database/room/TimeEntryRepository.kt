package com.example.onwork.database.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.onwork.model.TimeEntry

class TimeEntryRepository(context: Context) {

    private val timeEntryDao: TimeEntryDao

    init {
        val database =
            TimeEntryRoomDatabase.getDatabase(
                context
            )
        timeEntryDao = database!!.timeEntryDao()
    }

    fun getAllTimeEntries(userEmail: String): LiveData<List<TimeEntry>> =
        timeEntryDao.getAllTimeEntries(userEmail)

    suspend fun insertTimeEntry(timeEntry: TimeEntry): Long =
        timeEntryDao.insertTimeEntry(timeEntry)

    suspend fun updateTimeEntry(timeEntry: TimeEntry) = timeEntryDao.updateTimeEntry(timeEntry)

    suspend fun deleteTimeEntry(timeEntry: TimeEntry) = timeEntryDao.deleteTimeEntry(timeEntry)

    suspend fun deleteAllTimeEntries(userEmail: String) =
        timeEntryDao.deleteAllTimeEntries(userEmail)

    suspend fun getTimeEntryById(id: Long, userEmail: String): TimeEntry? =
        timeEntryDao.getTimeEntryById(id, userEmail)

    suspend fun getOnGoingTimeEntry(userEmail: String): TimeEntry? =
        timeEntryDao.getOnGoingTimeEntry(userEmail)
}