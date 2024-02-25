package com.example.moviesapp.api_responses.photos

import com.example.moviesapp.model.PhotoDataClass

data class Poster(
    val aspect_ratio: Double?,
    val file_path: String,
    val height: Int,
    val iso_639_1: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val width: Int
)
fun Poster.toPhoto() : PhotoDataClass =
    PhotoDataClass(aspect_ratio, file_path, height, iso_639_1, vote_average, vote_count, width,true)