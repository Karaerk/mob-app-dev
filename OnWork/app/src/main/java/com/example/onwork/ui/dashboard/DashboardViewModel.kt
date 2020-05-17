package com.example.onwork.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onwork.database.firebase.EntityRepository
import com.example.onwork.database.room.DateFormatRepository
import com.example.onwork.database.room.TimeEntryRepository
import com.example.onwork.model.*
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
    private val repository = EntityRepository()
    private val dateFormatRepository =
        DateFormatRepository(application.applicationContext)
    private val timeEntryRepository =
        TimeEntryRepository(application.applicationContext)
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
                attemptToInsertRemoteData(timeEntry)
            }
        }
    }

    /**
     * Prepares given (local) time entry for remote database by converting it to a proper object.
     */
    private fun prepareTimeEntryForRemote(timeEntry: TimeEntry): TimeEntryFirebase {
        return TimeEntryFirebase(
            timeEntry.userEmail,
            timeEntry.startTime.time,
            timeEntry.endTime!!.time,
            timeEntry.title
        )
    }

    /**
     * Attempts to send locally generated data to remote database.
     */
    private suspend fun attemptToInsertRemoteData(updatedTimeEntry: TimeEntry) =
        withContext(Dispatchers.IO) {
            val timeEntryFirebase = prepareTimeEntryForRemote(updatedTimeEntry)

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    repository.insertItemFromTimeEntry(timeEntryFirebase)
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
                attemptToDeleteFromRemote(timeEntry)
            }
        }
    }

    /**
     * Attempts to delete given time entry from remote data source.
     */
    private fun attemptToDeleteFromRemote(timeEntry: TimeEntry) {
        val timeEntryFirebase = prepareTimeEntryForRemote(timeEntry)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteItemFromTimeEntry(timeEntryFirebase)
            }
        }
    }

    /**
     * Deletes all time entries from the user.
     */
    fun deleteAllTimeEntries() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                timeEntryRepository.deleteAllTimeEntries(auth.currentUser!!.email!!)
                repository.deleteAllFromTimeEntry(auth.currentUser!!.email!!)
            }
        }
    }

    /**
     * Updates the given time entry's starting and ending time.
     */
    fun updateTimeEntry(start: String, end: String, old: TimeEntry, timeEntry: TimeEntry) {
        val timeDelimiter = ":"
        val current = prepareTimeEntryForRemote(old)

        val startTimeSplitted = start.split(timeDelimiter)
        val startTime = Calendar.getInstance()
        startTime.time = timeEntry.startTime
        startTime[Calendar.HOUR_OF_DAY] = startTimeSplitted[0].toInt()
        startTime[Calendar.MINUTE] = startTimeSplitted[1].toInt()
        timeEntry.startTime = startTime.time

        val endTimeSplitted = end.split(timeDelimiter)
        val endTime = Calendar.getInstance()
        endTime.time = timeEntry.endTime!!
        endTime[Calendar.HOUR_OF_DAY] = endTimeSplitted[0].toInt()
        endTime[Calendar.MINUTE] = endTimeSplitted[1].toInt()
        timeEntry.endTime = endTime.time

        val new = prepareTimeEntryForRemote(timeEntry)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                timeEntryRepository.updateTimeEntry(timeEntry)
                repository.updateItemFromTimeEntry(current, new)
            }
        }
    }
}