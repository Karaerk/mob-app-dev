package com.example.movielist.ui.main

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.movielist.database.MovieRepository
import com.example.movielist.model.Movie
import com.example.movielist.ui.detail.DetailActivity
import com.example.movielist.ui.detail.MOVIE_DETAIL
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val movieRepository = MovieRepository()
    val movies = MutableLiveData<List<Movie>>()
    val error = MutableLiveData<String>()
    private var context = application.applicationContext

    /**
     * Get a list of most popular movies of a specific year.
     * onResponse if the response is successful populate the [movies] object.
     * onFailure if the call encountered an error populate the [error] object.
     */
    fun getMostPopularMoviesOfYear(input: String) {
        if(isInputValidYear(input)) {
            val year = input.toInt()
            movieRepository.getMostPopularMoviesOfYear(year).enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    error.value = t.message
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val test: JsonObject? = response.body()
                        val test2: JsonElement = test!!["results"]
                        movies.value = Gson().fromJson(test2, Array<Movie>::class.java).toList()
                    }
                }
            })
        } else {
            error.value = "The given year is invalid."
        }
    }

    fun isInputValidYear(input: String): Boolean {
        val regex = "\\d{4}".toRegex()
        return regex.matches(input)
    }

    fun getMovieDetail(movie: Movie) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(MOVIE_DETAIL, movie)
        context.startActivity(intent)
    }
}