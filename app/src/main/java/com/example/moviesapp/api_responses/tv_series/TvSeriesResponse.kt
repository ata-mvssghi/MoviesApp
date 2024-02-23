package com.example.moviesapp.api_responses.tv_series

data class TvSeriesResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)