package com.example.moviesapp.api_responses.similar_movies

data class SimilarMoviesResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)