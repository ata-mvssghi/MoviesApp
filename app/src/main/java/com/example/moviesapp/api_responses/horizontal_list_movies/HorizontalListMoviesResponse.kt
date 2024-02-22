package com.example.moviesapp.api_responses.horizontal_list_movies

data class HorizontalListMoviesResponse(
    val cast: List<CastHorizontal>,
    val crew: List<CrewHorizontal>,
    val id: Int
)