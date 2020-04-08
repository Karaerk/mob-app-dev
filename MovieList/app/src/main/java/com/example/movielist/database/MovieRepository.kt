package com.example.movielist.database

import com.example.movielist.BuildConfig
import com.example.movielist.model.MovieApiService

class MovieRepository {
    private val language: String = "en-US"
    private val movieApi: MovieApiService = MovieApi.createApi()

    fun getMostPopularMoviesOfYear(year: Int) =
        movieApi.getMostPopularMoviesOfYear(year, BuildConfig.API_KEY, language)
}