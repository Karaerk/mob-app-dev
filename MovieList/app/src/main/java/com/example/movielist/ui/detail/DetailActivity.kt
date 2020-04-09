package com.example.movielist.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movielist.R
import com.example.movielist.model.Movie

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

const val MOVIE_DETAIL = "MOVIE_DETAIL"

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        initViewModel()
    }

    private fun initViews(movie: Movie) {
        val baseUrl = "https://image.tmdb.org/t/p/w500"

        // Make sure to only load a backdrop image if a movie has one
        if(movie.backdropImage != null) {
            val backdropImage = baseUrl + movie.backdropImage
            Glide.with(this).load(backdropImage).into(ivBackdropImage)
        } else {
            Glide.with(this).load("https://i.imgur.com/APWeHz1.jpg").into(ivBackdropImage)
        }

        val posterImage = baseUrl + movie.posterImage
        Glide.with(this).load(posterImage).into(ivPosterImage)

        tvTitle.text = movie.title
        tvReleaseDate.text = getString(R.string.release_date, movie.releaseDate)
        tvRate.text = movie.rating.toString()
        tvOverview.text = movie.overview
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(DetailActivityViewModel::class.java)
        viewModel.movie.apply { value = intent.getParcelableExtra(MOVIE_DETAIL) }
        viewModel.movie.observe(this, Observer {
            initViews(it)
        })
    }

}
