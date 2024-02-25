package com.example.moviesapp.api_responses.photos

data class PhotosResponse(
    val backdrops: List<Backdrop>,
    val id: Int,
    val logos: List<Logo>,
    val posters: List<Poster>
)