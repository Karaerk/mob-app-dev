package com.example.onwork.ui.account.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpViewModel() : ViewModel() {

    private var auth = Firebase.auth
    val isSignedIn = MutableLiveData<Boolean>(false)
    val success = MutableLiveData<Boolean>(false)
    val error = MutableLiveData<Boolean>(false)

    init {
        if(auth.currentUser != null){
            isSignedIn.value = true
        }
    }

    companion object {
        val TAG = "SignUpViewModel"
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
                } else {
                    Log.w(TAG, "attemptSignUp:failure", task.exception)
                    error.value = true
                    isSignedIn.value = false
                }
            }
    }
}