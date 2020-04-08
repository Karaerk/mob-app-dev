package com.example.movielist.model

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("/3/discover/movie")
    fun getMostPopularMoviesOfYear(
        @Query("year") year: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<JsonObject>
}