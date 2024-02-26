package com.example.moviesapp.api_responses.personImages

import com.example.moviesapp.Constants
import com.example.moviesapp.model.PhotoDataClass

data class Profile(
    val aspect_ratio: Double,
    val file_path: String,
    val height: Int,
    val iso_639_1: Any,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
)
fun Profile.toPhotoDataClass() : PhotoDataClass =
    PhotoDataClass(aspect_ratio = aspect_ratio,file_path = file_path , height = height, iso_639_1 = null ,vote_average = vote_average,vote_count =vote_count,width =width, type = Constants.ImageType.profile)