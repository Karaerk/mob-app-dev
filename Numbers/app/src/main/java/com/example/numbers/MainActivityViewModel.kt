package com.example.numbers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val numbersRepository = NumbersRepository()
    val trivia = MutableLiveData<Trivia>()
    val error = MutableLiveData<String>()

    /**
     * Get a random mnumber trivia from the repository using Retrofit.
     * onResponse if the response is successful populate the [trivia] object.
     * If the call encountered an error then populate the [error] object.
     */
    fun getRandomTrivia() {
        numbersRepository.getRandomNumberTrivia().enqueue(object : Callback<Trivia> {
            override fun onResponse(call: Call<Trivia>, response: Response<Trivia>) {
                if(response.isSuccessful) trivia.value = response.body()
                else error.value = "An error occurred: ${response.errorBody().toString()}"
            }
            override fun onFailure(call: Call<Trivia>, t: Throwable) {
                error.value = t.message
            }
        })
    }
}