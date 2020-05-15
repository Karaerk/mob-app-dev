package com.example.onwork.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.onwork.database.DateFormatRepository
import com.example.onwork.model.DateFormat
import com.example.onwork.model.DateFormatEnum
import com.example.onwork.model.TimeEntry
import com.example.onwork.model.TimeEntryResult
import com.example.onwork.ui.helper.DateTime
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private var auth = Firebase.auth
    private val dateFormats = DateFormatEnum.values()
    private val dateFormatRepository = DateFormatRepository(application.applicationContext)
    var dateFormat = dateFormatRepository.getDateFormat(auth.currentUser!!.email!!)

    companion object {
        private val TAG = DashboardViewModel::class.simpleName
    }

    /**
     * Returns the default date format.
     */
    fun getDateFormat(date: Date = Date()): DateFormat {
        return if (dateFormat.value == null) {
            DateFormat(
                dateFormats[0],
                date,
                auth.currentUser!!.email!!
            )
        } else {
            dateFormat.value!!
        }
    }

    /**
     * Returns a result of given time entry to the view.
     */
    fun getTimeEntryResult(timeEntry: TimeEntry): TimeEntryResult {
        val timePattern = "HH:mm"

        val date = DateTime.getDateFormatted(timeEntry.startTime, dateFormat.value!!.value.format)
        val startTime = DateTime.getDateFormatted(timeEntry.startTime, timePattern)
        val endTime = DateTime.getDateFormatted(timeEntry.endTime!!, timePattern)

        val duration = DateTime.calculateDifference(timeEntry.startTime, timeEntry.endTime!!)

        return TimeEntryResult(
            timeEntry.title,
            timeEntry.userEmail,
            date,
            duration,
            startTime,
            endTime
        )
    }
}