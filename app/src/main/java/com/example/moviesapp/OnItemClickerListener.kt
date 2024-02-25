package com.example.moviesapp

import com.example.moviesapp.api_responses.photos.PhotosResponse
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.PhotoDataClass

interface OnItemClickerListener {
    fun onPhotoCLickListener(position: Int)
    fun onItemClick(movie: Movie ,isMovie: Boolean)
}