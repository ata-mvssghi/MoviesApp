package com.example.moviesapp.model

import com.example.moviesapp.Constants

data class PhotoDataClass (
    val aspect_ratio: Double?,
    val file_path: String,
    val height: Int,
    val iso_639_1: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val width: Int,
    val type: Constants.ImageType
        )