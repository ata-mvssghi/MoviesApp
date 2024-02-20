package com.example.moviesapp.api_responses.credt

data class CreditResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)