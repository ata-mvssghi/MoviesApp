package com.example.moviesapp.converter

import com.example.moviesapp.api_responses.Movie_Api
import com.example.moviesapp.model.Movie

class Converter {
    fun Movie_Api.toMovie(): Movie {
       return Movie(
            id = id, poster_path = poster_path, title = title, vote_average = vote_average,
            vote_count = vote_count, original_language = original_language,
            release_date = release_date, overview = overview
        )
    }
}