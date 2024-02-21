package com.example.moviesapp.api_responses

import com.example.moviesapp.model.Movie

data class Movie_Api(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)
fun Movie_Api.toMovie(): Movie {
    return Movie(
        id = id, poster_path = poster_path, title = title, vote_average = vote_average,
        vote_count = vote_count, original_language = original_language,
        release_date = release_date, overview = overview,
        original_picture =  backdrop_path
    )
}