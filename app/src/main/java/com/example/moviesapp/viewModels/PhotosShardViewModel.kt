package com.example.moviesapp.viewModels

import androidx.lifecycle.ViewModel
import com.example.moviesapp.model.PhotoDataClass

class PhotosShardViewModel : ViewModel() {
    lateinit var photoList: List<PhotoDataClass>
    lateinit var  profilePictures : List<PhotoDataClass>
    var chosenMoviePhotoPosition: Int = 0
}