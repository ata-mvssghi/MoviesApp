package com.example.moviesapp.model

data class Movie (
    val id: Int,
    val poster_path: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    val original_language: String,
    val release_date: String,
    val overview: String,
        )