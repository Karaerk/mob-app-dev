package com.example.onwork.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.onwork.model.TimeEntry

@Dao
interface TimeEntryDao {

    @Query("SELECT * FROM timeEntry WHERE userEmail = :userEmail AND endTime IS NOT NULL")
    fun getAllTimeEntries(userEmail: String): LiveData<List<TimeEntry>>

    @Insert
    suspend fun insertTimeEntry(timeEntry: TimeEntry): Long

    @Update
    suspend fun updateTimeEntry(timeEntry: TimeEntry)

    @Delete
    suspend fun deleteTimeEntry(timeEntry: TimeEntry)

    @Query("DELETE FROM timeEntry")
    suspend fun deleteAllTimeEntries()

    @Query("SELECT * FROM timeEntry WHERE id = :id")
    suspend fun getTimeEntryById(id: Long): TimeEntry?

    @Query("SELECT * FROM timeEntry WHERE userEmail = :userEmail AND endTime IS NULL ORDER BY id DESC LIMIT 1")
    suspend fun getOnGoingTimeEntry(userEmail: String): TimeEntry?
}