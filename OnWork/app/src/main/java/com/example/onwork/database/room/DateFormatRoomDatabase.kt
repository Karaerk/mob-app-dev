package com.example.onwork.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.onwork.model.DateFormat
import com.example.onwork.model.DateFormatEnum
import com.example.onwork.model.helper.Converts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [DateFormat::class], version = 1, exportSchema = false)
@TypeConverters(Converts::class)
abstract class DateFormatRoomDatabase : RoomDatabase() {

    abstract fun dateFormatDao(): DateFormatDao

    companion object {
        private const val DATABASE_NAME = "DATE_FORMAT_DATABASE"
        private var auth = Firebase.auth

        @Volatile
        private var INSTANCE: DateFormatRoomDatabase? = null

        fun getDatabase(context: Context): DateFormatRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(DateFormatRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DateFormatRoomDatabase::class.java,
                            DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .addCallback(object : RoomDatabase.Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    INSTANCE?.let { database ->
                                        CoroutineScope(Dispatchers.IO).launch {
                                            database.dateFormatDao().insertDateFormat(
                                                DateFormat(
                                                    DateFormatEnum.values()[0],
                                                    auth.currentUser!!.email!!
                                                )
                                            )
                                        }
                                    }
                                }
                            })
                            .build()
                    }
                }
            }

            return INSTANCE
        }
    }
}