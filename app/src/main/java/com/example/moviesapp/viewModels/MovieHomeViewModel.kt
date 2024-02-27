package com.example.moviesapp.viewModels

import android.util.Log
import androidx.core.graphics.convertTo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.ApiService
import com.example.moviesapp.RetrofitInstance
import com.example.moviesapp.api_responses.Movie_Api
import com.example.moviesapp.api_responses.TopRatedResponse
import com.example.moviesapp.api_responses.fromMovieToMovie
import com.example.moviesapp.model.Movie
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit

class MovieHomeViewModel : ViewModel() {
    lateinit var topRatedMovies  : List<Movie>
    lateinit var upComing  : List<Movie>
    lateinit var nowPlaying  : List<Movie>
    lateinit var popularMovies  : List<Movie>
    private val _stateFlow = MutableSharedFlow<String>()
    val stateFlow: MutableSharedFlow<String> get() = _stateFlow
    private val retrofit : Retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService  = retrofit.create(ApiService::class.java)


    fun getTopRatedMovies(page:Int, isMovie :Boolean){
        viewModelScope.launch {
            try {
                Log.i("imdb","top rated movie api called with page = $page")
                val result  : Response<TopRatedResponse>
                if(isMovie)
                     result = apiService.getTopRatedMovies(1) ?: throw Exception("something went wrong ")
                else
                    result = apiService.getTopRatedSerials(1) ?:throw Exception("something went wrong")
                val newData =  result.body()?.results
                val suitableList :MutableList<Movie> = mutableListOf()
                if (newData != null) {
                    for(movie in newData)
                        suitableList.add(movie.fromMovieToMovie())
                    topRatedMovies = suitableList
                }
                if(isMovie)
                    emitEvent("top rated movies fetched successfully")
                else
                    emitEvent("top rated series fetched successfully")
            }
            catch (e:Exception){
                Log.e("imdb","top rated movies fetching failed")
                Log.e("imdb", e.message.toString())
            }

        }
    }
    fun getUpcoming(page:Int, isMovie :Boolean){
        viewModelScope.launch {
            try {
                Log.i("imdb","upcoing movie api called with page = $page")
                val result : Response<TopRatedResponse>
                if(isMovie)
                    result = apiService.getUpComing(1) ?: throw Exception("something went wrong ")
                else
                    result = apiService.getOnTheAirSerials(1) ?: throw Exception("something went wrong ")
                val newData =  result.body()?.results
                val suitableList :MutableList<Movie> = mutableListOf()
                if (newData != null) {
                    for(movie in newData)
                        suitableList.add(movie.fromMovieToMovie())
                    upComing = suitableList
                }
                if(isMovie)
                 emitEvent("upcoming movies fetched successfully")
                else
                    emitEvent("upcoming series fetched successfully")
            }
            catch (e:Exception){
                Log.e("imdb","upcoming movies fetching failed")
                Log.e("imdb", e.message.toString())
            }

        }
    }
    fun getPopular(page:Int, isMovie :Boolean){
        viewModelScope.launch {
            try {
                Log.i("imdb","popular movie api called with page = $page")
                val result  : Response<TopRatedResponse>
                if(isMovie)
                     result = apiService.getPopular(1) ?: throw Exception("something went wrong ")
                else
                    result = apiService.getPopularSerial(1) ?: throw Exception("something went wrong ")
                val newData =  result.body()?.results
                val suitableList :MutableList<Movie> = mutableListOf()
                if (newData != null) {
                    for(movie in newData)
                        suitableList.add(movie.fromMovieToMovie())
                    popularMovies = suitableList
                }
                if(isMovie)
                    emitEvent("popular movies fetched successfully")
                else
                    emitEvent("popular series fetched successfully")
            }
            catch (e:Exception){
                Log.e("imdb","popular movies fetching failed")
                Log.e("imdb", e.message.toString())
            }

        }
    }
    fun getNowPlaying(page:Int, isMovie :Boolean){
        viewModelScope.launch {
            try {
                Log.i("imdb","now playing movie api called with page = $page")
                val result = apiService.getNowPaying(1) ?: throw Exception("something went wrong ")
                val newData =  result.body()?.results
                val suitableList :MutableList<Movie> = mutableListOf()
                if (newData != null) {
                    for(movie in newData)
                        suitableList.add(movie.fromMovieToMovie())
                    nowPlaying = suitableList
                }
                emitEvent("now playing movies fetched successfully")
            }
            catch (e:Exception){
                Log.e("imdb","now playing movies fetching failed")
                Log.e("imdb", e.message.toString())
            }

        }
    }
    private fun emitEvent(event: String) {
        viewModelScope.launch {
            _stateFlow.emit(event)
            Log.i("imdb", "Emitting: $event")
        }
    }
}