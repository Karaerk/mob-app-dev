package com.example.onwork.ui.settings

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
import com.example.onwork.model.DateFormatFirebase
import com.example.onwork.model.DateFormatSnapshot
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private var auth = Firebase.auth
    private val activityContext = application
    private val repository = EntityRepository()
    private val dateFormats = DateFormatEnum.values()
    private val dateFormatRepository =
        DateFormatRepository(application.applicationContext)
    private val timeEntryRepository =
        TimeEntryRepository(application.applicationContext)
    val dateFormatStrings = dateFormats.map { it.label }
    var dateFormat = dateFormatRepository.getDateFormat(auth.currentUser!!.email!!)
    val signOut = MutableLiveData(false)
    val deleteAccount = MutableLiveData(false)
    val errorDelete = MutableLiveData(false)
    val errorDateFormat = MutableLiveData(false)

    companion object {
        private val TAG = SettingsViewModel::class.simpleName
    }

    /**
     * Signs out the currently signed in user.
     */
    fun signOut() {
        auth.signOut()
        signOut.value = true
    }

    /**
     * Deletes the currently signed in user from the database.
     */
    fun deleteAccount() {
        val email = auth.currentUser!!.email!!

        auth.currentUser!!.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "deleteAccount:success")

                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            dateFormatRepository.deleteAllDateFormats(email)
                            timeEntryRepository.deleteAllTimeEntries(email)
                            repository.deleteAllFromDateFormat(email)
                            repository.deleteAllFromTimeEntry(email)
                        }
                    }
                    deleteAccount.postValue(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "deleteAccount:failure", task.exception)
                    errorDelete.value = true
                }
            }
    }

    /**
     * Updates the date format used by the user.
     */
    fun updateDateFormat(index: Int) {
        var updatedDateFormat: DateFormat? = null
        if (dateFormat.value != null) {
            updatedDateFormat = dateFormat.value
            updatedDateFormat!!.value = dateFormats.getOrElse(index) {
                errorDateFormat.value = true
                dateFormats[0]
            }
        } else {
            updatedDateFormat = DateFormat(
                dateFormats[0],
                auth.currentUser!!.email!!
            )
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dateFormatRepository.updateDateFormat(updatedDateFormat)
                attemptToUpdateRemoteData(updatedDateFormat)
            }
        }
    }

    /**
     * Attempts to collect remotely stored data from the user in order to let the user use the app
     * while being offline.
     */
    private suspend fun attemptToUpdateRemoteData(updatedDateFormat: DateFormat) =
        withContext(Dispatchers.IO) {
            val dateFormatFirebase = DateFormatFirebase(
                updatedDateFormat.userEmail,
                updatedDateFormat.value.ordinal
            )
            val item = DateFormatSnapshot(DateFormat.getDatabaseName(), dateFormatFirebase)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    repository.updateItemFromDateFormat(auth.currentUser!!.email!!, item)
                }
            }
        }
}