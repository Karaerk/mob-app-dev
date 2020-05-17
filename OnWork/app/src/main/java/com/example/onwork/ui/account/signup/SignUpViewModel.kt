package com.example.onwork.ui.account.signup

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onwork.database.firebase.EntityRepository
import com.example.onwork.database.room.DateFormatRepository
import com.example.onwork.database.room.TimeEntryRepository
import com.example.onwork.model.DateFormat
import com.example.onwork.model.DateFormatEnum
import com.example.onwork.model.TimeEntry
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private var auth = Firebase.auth
    private val repository = EntityRepository()
    private val dateFormatRepository =
        DateFormatRepository(application.applicationContext)
    private val timeEntryRepository =
        TimeEntryRepository(application.applicationContext)
    val isSignedIn = MutableLiveData(false)
    val success = MutableLiveData(false)
    val error = MutableLiveData(false)

    init {
        if (auth.currentUser != null) {
            isSignedIn.value = true
        }
    }

    companion object {
        val TAG = SignUpViewModel::class.simpleName
    }

    /**
     * Makes an attempt to sign up the user with given data.
     */
    fun attemptSignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "attemptSignUp:success")
                    isSignedIn.value = true
                    success.value = true

                    insertDefaultDateFormat()
                    getDateFormatFromRemote()
                    getTimeEntriesFromRemote()
                } else {
                    Log.w(TAG, "attemptSignUp:failure", task.exception)
                    error.value = true
                    isSignedIn.value = false
                }
            }
    }

    /**
     * Uses the default date format for the newly registered user.
     */
    private fun insertDefaultDateFormat() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dateFormatRepository.insertDateFormat(DateFormat(userEmail = auth.currentUser!!.email!!))
            }
        }
    }

    /**
     * Gets the user's date format saved remotely to store it locally.
     */
    private fun getDateFormatFromRemote() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val dateFormat = repository.getItemFromDateFormat(auth.currentUser!!.email!!)

                // Save remotely stored data locally
                if (dateFormat != null) {
                    val newDateFormat = DateFormat(
                        DateFormatEnum.values()[dateFormat.value!!.value],
                        dateFormat.value!!.userEmail
                    )
                    dateFormatRepository.updateDateFormat(newDateFormat)
                }
            }
        }
    }

    /**
     * Gets the user's time entries saved remotely to store it locally.
     */
    private fun getTimeEntriesFromRemote() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val timeEntries = repository.getAllTimeEntries(auth.currentUser!!.email!!)

                // Save remotely stored data locally
                if (timeEntries.isNotEmpty()) {
                    timeEntryRepository.deleteAllTimeEntries(auth.currentUser!!.email!!)

                    timeEntries.forEach {
                        val timeEntry = TimeEntry(
                            it.title,
                            it.userEmail,
                            Date(it.startTime),
                            Date(it.endTime)
                        )
                        timeEntryRepository.insertTimeEntry(timeEntry)
                    }
                }
            }
        }
    }
}