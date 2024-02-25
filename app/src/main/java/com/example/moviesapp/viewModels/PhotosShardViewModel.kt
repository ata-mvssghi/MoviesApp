package com.example.moviesapp.viewModels

import androidx.lifecycle.ViewModel
import com.example.moviesapp.api_responses.photos.PhotosResponse
import com.example.moviesapp.model.PhotoDataClass

class PhotosShardViewModel : ViewModel() {
    lateinit var photoList: List<PhotoDataClass>

    var chosenPhotoPosition: Int = 0
}