package com.example.onwork.ui.account.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInViewModel() : ViewModel() {

    private var auth = Firebase.auth
    val isSignedIn = MutableLiveData<Boolean>(false)
    val success = MutableLiveData<Boolean>(false)
    val error = MutableLiveData<Boolean>(false)

    init {
        println(auth.currentUser)
        if(auth.currentUser != null){
            isSignedIn.value = true
        }
    }

    companion object {
        val TAG = "SignInViewModel"
    }

    /**
     * Makes an attempt to sign in the user with given data.
     */
    fun attemptSignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    isSignedIn.value = true
                    success.value = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    error.value = true
                    isSignedIn.value = false
                }
            }
    }
}