package com.example.moviesapp.api_responses.similar_movies

import com.example.moviesapp.api_responses.similar_movies.Result
import com.example.moviesapp.model.Movie

data class Result(
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
fun Result.toMovie() : Movie =
    Movie(id = id , original_language = original_language , original_picture =  original_language,
    poster_path =  poster_path, title =  title , vote_average =  vote_average , vote_count =  vote_count,
    overview =  overview, release_date =  release_date)