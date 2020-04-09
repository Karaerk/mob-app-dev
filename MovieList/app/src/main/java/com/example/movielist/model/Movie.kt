package com.example.movielist.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Movie(
    @SerializedName("backdrop_path") val backdropImage: String?,
    @SerializedName("poster_path") val posterImage: String,
    val title: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val rating: Double,
    val overview: String
): Parcelable