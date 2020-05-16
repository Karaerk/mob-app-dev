package com.example.onwork.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onwork.database.DateFormatRepository
import com.example.onwork.database.TimeEntryRepository
import com.example.onwork.model.DateFormat
import com.example.onwork.model.DateFormatEnum
import com.example.onwork.model.TimeEntry
import com.example.onwork.model.TimeEntryResult
import com.example.onwork.ui.helper.DateTime
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private var auth = Firebase.auth
    private val dateFormats = DateFormatEnum.values()
    private val dateFormatRepository = DateFormatRepository(application.applicationContext)
    private val timeEntryRepository = TimeEntryRepository(application.applicationContext)
    var dateFormat = dateFormatRepository.getDateFormat(auth.currentUser!!.email!!)

    private var onGoingId = MutableLiveData<Long>()
    var timeEntries = timeEntryRepository.getAllTimeEntries(auth.currentUser!!.email!!)
    var onGoingTimeEntry = MutableLiveData<TimeEntry>()
    var updateOnGoingTimeEntry = MutableLiveData(false)

    companion object {
        private val TAG = DashboardViewModel::class.simpleName
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val existingOnGoing =
                    timeEntryRepository.getOnGoingTimeEntry(auth.currentUser!!.email!!)

                if (existingOnGoing != null) {
                    onGoingId.postValue(existingOnGoing.id)
                    onGoingTimeEntry.postValue(existingOnGoing)
                    updateOnGoingTimeEntry.postValue(true)
                } else {
                    updateOnGoingTimeEntry.postValue(false)
                }
            }
        }
    }

    /**
     * Checks if there's current a time entry going on.
     */
    fun isOnGoingPresent() = onGoingTimeEntry.value == null

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

    /**
     * Updates the user's on going time entry.
     */
    fun updateOnGoingTimeEntry(title: String) {
        val timeEntry = TimeEntry(
            title,
            auth.currentUser!!.email!!
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val existingOnGoing =
                    timeEntryRepository.getOnGoingTimeEntry(auth.currentUser!!.email!!)

                if (existingOnGoing != null) {
                    timeEntry.id = existingOnGoing.id
                    timeEntryRepository.updateTimeEntry(timeEntry)
                } else {
                    timeEntry.id = timeEntryRepository.insertTimeEntry(timeEntry)
                }

                onGoingId.postValue(timeEntry.id)
                onGoingTimeEntry.postValue(timeEntry)
                updateOnGoingTimeEntry.postValue(true)
            }
        }
    }

    /**
     * Ends the currently on going time entry.
     */
    fun endOnGoingTimeEntry() {
        val timeEntry = onGoingTimeEntry.value!!
        timeEntry.endTime = Date()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                timeEntryRepository.updateTimeEntry(timeEntry)
            }
        }
    }

    /**
     * Deletes a time entry from the user.
     */
    fun deleteTimeEntry(timeEntry: TimeEntry) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                timeEntryRepository.deleteTimeEntry(timeEntry)
            }
        }
    }

    /**
     * Deletes all time entries from the user.
     */
    fun deleteAllTimeEntries() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                timeEntryRepository.deleteAllTimeEntries()
            }
        }
    }

    /**
     * Updates the given time entry's starting and ending time.
     */
    fun updateTimeEntry(start: String, end: String, timeEntry: TimeEntry) {
        val timeDelimiter = ":"

        val startTimeSplitted = start.split(timeDelimiter)
        val startTime = Calendar.getInstance()
        startTime.time = timeEntry.startTime
        startTime[Calendar.HOUR] = startTimeSplitted[0].toInt()
        startTime[Calendar.MINUTE] = startTimeSplitted[1].toInt()
        timeEntry.startTime = startTime.time

        val endTimeSplitted = end.split(timeDelimiter)
        val endTime = Calendar.getInstance()
        endTime.time = timeEntry.endTime!!
        endTime[Calendar.HOUR] = endTimeSplitted[0].toInt()
        endTime[Calendar.MINUTE] = endTimeSplitted[1].toInt()
        timeEntry.endTime = endTime.time

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                timeEntryRepository.updateTimeEntry(timeEntry)
            }
        }
    }
}