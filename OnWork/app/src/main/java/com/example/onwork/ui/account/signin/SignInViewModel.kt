package com.example.onwork.ui.account.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onwork.database.firebase.EntityRepository
import com.example.onwork.database.room.DateFormatRepository
import com.example.onwork.model.DateFormat
import com.example.onwork.model.DateFormatEnum
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private var auth = Firebase.auth
    private val repository = EntityRepository()
    private val dateFormatRepository =
        DateFormatRepository(application.applicationContext)
    val isSignedIn = MutableLiveData(false)
    val success = MutableLiveData(false)
    val error = MutableLiveData(false)

    init {
        println(auth.currentUser)
        if (auth.currentUser != null) {
            isSignedIn.value = true
        }
    }

    companion object {
        val TAG = SignInViewModel::class.simpleName
    }

    /**
     * Makes an attempt to sign in the user with given data.
     */
    fun attemptSignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "attemptSignIn:success")
                    isSignedIn.value = true
                    success.value = true

                    getDateFormatFromRemote()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "attemptSignIn:failure", task.exception)
                    error.value = true
                    isSignedIn.value = false
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
                if(dateFormat != null) {
                    val newDateFormat = DateFormat(
                        DateFormatEnum.values()[dateFormat.value!!.value],
                        dateFormat.value!!.userEmail
                    )
                    dateFormatRepository.updateDateFormat(newDateFormat)
                }
            }
        }
    }
}