package com.example.onwork.ui.settings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onwork.ui.account.signin.SignInViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsViewModel() : ViewModel() {

    private var auth = Firebase.auth
    val signOut = MutableLiveData<Boolean>(false)
    val deleteAccount = MutableLiveData<Boolean>(false)
    val errorDelete = MutableLiveData<Boolean>(false)

    companion object {
        val TAG = "SettingsViewModel"
    }

    /**
     * Signs out the currently signed in user.
     */
    fun signOut() {
        auth.signOut()
        signOut.value = true
        //TODO: Fix issue where user goes sometimes to dashboard instead of sign in (probably viewmodel related?)
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
}