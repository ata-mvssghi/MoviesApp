package com.example.moviesapp.api_responses

data class TopRatedResponse(
    val page: Int,
    val results: List<Movie_Api>,
    val total_pages: Int,
    val total_results: Int
)