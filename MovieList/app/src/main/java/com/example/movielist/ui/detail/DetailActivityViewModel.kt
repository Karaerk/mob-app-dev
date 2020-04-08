package com.example.movielist.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.movielist.model.Movie

class DetailActivityViewModel(application: Application) : AndroidViewModel(application) {

    val movie = MutableLiveData<Movie>()
}