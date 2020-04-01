package com.example.reminder.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reminder.model.Reminder

/**
 * The suspended functions can't be called without using Coroutines.
 */
@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminderTable")
    fun getAllReminders(): LiveData<List<Reminder>>

    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Update
    suspend fun updateReminder(reminder: Reminder)
}