package com.example.moviesapp.api_responses.horizontal_list_movies

import com.example.moviesapp.model.Movie

data class CastHorizontal(
    val adult: Boolean,
    val backdrop_path: String,
    val character: String,
    val credit_id: String,
    val genre_ids: List<Int>,
    val id: Int,
    val order: Int,
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
fun CastHorizontal.toMovie() : Movie =
    Movie(id = id , original_language = original_language , original_picture =  backdrop_path , poster_path =  poster_path
    , vote_average =  vote_average, vote_count = vote_count, release_date =  release_date, title =  title
    , overview =  overview)