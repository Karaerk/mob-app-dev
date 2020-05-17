package com.example.onwork.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.onwork.model.TimeEntry
import com.example.onwork.model.helper.Converts

@Database(entities = [TimeEntry::class], version = 1, exportSchema = false)
@TypeConverters(Converts::class)
abstract class TimeEntryRoomDatabase : RoomDatabase() {

    abstract fun timeEntryDao(): TimeEntryDao

    companion object {
        private const val DATABASE_NAME = "TIME_ENTRY_DATABASE"

        @Volatile
        private var timeEntryDatabaseInstance: TimeEntryRoomDatabase? = null

        fun getDatabase(context: Context): TimeEntryRoomDatabase? {
            if (timeEntryDatabaseInstance == null) {
                synchronized(TimeEntryRoomDatabase::class.java) {
                    if (timeEntryDatabaseInstance == null) {
                        timeEntryDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            TimeEntryRoomDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return timeEntryDatabaseInstance
        }
    }
}