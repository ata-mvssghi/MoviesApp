package com.example.moviesapp.api_responses.search

data class Multi(
    val page: Int,
    val results: List<SearchResponse>,
    val total_pages: Int,
    val total_results: Int
)