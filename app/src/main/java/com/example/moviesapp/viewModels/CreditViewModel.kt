package com.example.moviesapp.viewModels

import android.provider.MediaStore.Video
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.ApiService
import com.example.moviesapp.RetrofitInstance
import com.example.moviesapp.api_responses.credt.Cast
import com.example.moviesapp.api_responses.credt.CreditResponse
import com.example.moviesapp.api_responses.photos.PhotosResponse
import com.example.moviesapp.api_responses.similar_movies.SimilarMoviesResponse
import com.example.moviesapp.api_responses.similar_movies.toMovie
import com.example.moviesapp.model.Movie
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit

class CreditViewModel(): ViewModel() {
     var similarMovies : MutableList <Movie> ? = null
     var images : PhotosResponse? = null
     var actors : MutableList<Cast>?=null
     var  videoKey :String? = null
    private val retrofit : Retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService  = retrofit.create(ApiService::class.java)
    private val _stateFlow = MutableSharedFlow<String>()
    val stateFlow: MutableSharedFlow<String> get() = _stateFlow
    fun getActorsList(movieId :Int,isMovie: Boolean) {
        viewModelScope.launch {
            try {
                val response : Response<CreditResponse>
                if(isMovie)
                    response = apiService.getMovieCredits(movieId)
                else
                    response = apiService.getSerialCredits(movieId)
                if (response.isSuccessful) {
                    val newData =     response.body()?.cast as MutableList<Cast>
                    if(actors==null)
                        actors = newData
                    else
                        actors?.addAll(newData)
                    emitEvent("actors list fetched")
                } else {
                    Log.e("imdb",response.errorBody().toString())
                    Log.e("imdb",response.code().toString())
                }
            }
            catch (e:Exception){
                Log.e("imdb",e.message.toString())
            }
        }
    }
    fun getVideoKey(movieId: Int , isMovie : Boolean){
        try {
            viewModelScope.launch {
                val response  :Response<com.example.moviesapp.api_responses.video.Video>
                if(isMovie)
                    response = apiService.getMovieVideo(movieId)
                else
                    response = apiService.getSerialVideo(movieId)
                if(response.isSuccessful){
                    for(video in response.body()?.results!!){
                        if(video.type.equals("Trailer") && video.official ){
                            videoKey = video.key
                        }
                    }
                    emitEvent("video fetched")
                }
                else{
                    Log.e("imdb",response.errorBody().toString())
                    Log.e("imdb",response.code().toString())
                }
            }
        }
        catch (e:Exception){
            Log.e("imdb",e.message.toString())
        }
    }
    fun getSimilarMoviesList(movieId: Int,isMovie: Boolean){
        try {
            viewModelScope.launch {
                val response :Response<SimilarMoviesResponse>
                if(isMovie)
                     response = apiService.getSimilarMovies(movieId)
                else
                    response = apiService.getSimilarSeries(movieId)
                if(response.isSuccessful){
                    val list = response.body()?.results as MutableList
                    similarMovies = list.map { it.toMovie() }.toMutableList()
                    emitEvent("similar movies fetched")
                }
                else{
                    Log.e("imdb",response.errorBody().toString())
                    Log.e("imdb",response.code().toString())
                }
            }
        }
        catch (e:Exception){
            Log.e("imdb",e.message.toString())
        }
    }
    fun getImages(movieId: Int,isMovie: Boolean){
        try {
            viewModelScope.launch {
                val response :Response<PhotosResponse>
                if(isMovie)
                    response = apiService.getMoviePictures(movieId)
                else
                    response = apiService.getSerialPictures(movieId)
                if(response.isSuccessful){
                    images = response.body()
                    emitEvent("images fetched")
                }
                else{
                    Log.e("imdb",response.errorBody().toString())
                    Log.e("imdb",response.code().toString())
                }
            }
        }
        catch (e:Exception){
            Log.e("imdb",e.message.toString())
        }
    }
    private fun emitEvent(event: String) {
        viewModelScope.launch {
            _stateFlow.emit(event)
            Log.i("imdb", "Emitting: $event")
        }
    }
}