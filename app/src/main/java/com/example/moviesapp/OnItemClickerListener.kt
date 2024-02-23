package com.example.moviesapp

import com.example.moviesapp.model.Movie

interface OnItemClickerListener {
    fun onItemClick(movie: Movie ,isMovie: Boolean)
}