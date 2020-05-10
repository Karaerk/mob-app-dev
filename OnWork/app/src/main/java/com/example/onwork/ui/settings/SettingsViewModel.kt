package com.example.onwork.ui.settings

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onwork.database.DateFormatRepository
import com.example.onwork.model.DateFormat
import com.example.onwork.model.DateFormatEnum
import com.example.onwork.ui.account.signin.SignInViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private var auth = Firebase.auth
    private val dateFormats = DateFormatEnum.values()
    private val dateFormatRepository = DateFormatRepository(application.applicationContext)
    val dateFormatStrings = dateFormats.map { it.format }
    var dateFormat = dateFormatRepository.getDateFormat(auth.currentUser!!.email!!)
    val signOut = MutableLiveData(false)
    val deleteAccount = MutableLiveData(false)
    val errorDelete = MutableLiveData(false)
    val errorDateFormat = MutableLiveData(false)

    companion object {
        val TAG = "SettingsViewModel"
    }

    init {
        if(dateFormat.value == null)
            insertDateFormat()
    }

    /**
     * Signs out the currently signed in user.
     */
    fun signOut() {
        auth.signOut()
        println(auth.currentUser)
        signOut.value = true
    }

    /**
     * Deletes the currently signed in user from the database.
     */
    fun deleteAccount() {
        auth.currentUser!!.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(SignInViewModel.TAG, "deleteAccount:success")
                    deleteAccount.value = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(SignInViewModel.TAG, "deleteAccount:failure", task.exception)
                    errorDelete.value = true
                }
            }
    }

    /**
     * Updates the date format used by the user.
     */
    fun updateDateFormat(index: Int) {
        val updatedDateFormat = dateFormat.value
        updatedDateFormat!!.value = dateFormats.getOrElse(index) {
            errorDateFormat.value = true
            dateFormats[0]
        }
        updatedDateFormat.lastUpdated = Date()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dateFormatRepository.updateDateFormat(updatedDateFormat)
            }
        }
    }

    /**
     * Insert a date format used by the user.
     */
    fun insertDateFormat() {
        val newDateFormat = DateFormat(
            DateFormatEnum.values()[0],
            Date(),
            auth.currentUser!!.email!!
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dateFormatRepository.insertDateFormat(newDateFormat)
            }
        }
    }
}