package com.example.moviesapp.repository

import android.util.Log
import com.example.moviesapp.ApiService
import com.example.moviesapp.RetrofitInstance
import com.example.moviesapp.api_responses.credt.Cast
import retrofit2.Retrofit

class MovieDetailRepository {
    private val retrofit : Retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService  = retrofit.create(ApiService::class.java)
    suspend fun getCredits(movieId :Int) : List<Cast>?{
        val response = apiService.getMovieCredits(movieId)
        if(response.isSuccessful){
            return response.body()?.cast
        }
        else{
            Log.e("imdb",response.code().toString())
            Log.e("imdb",response.errorBody().toString())
        }
        return  null
    }
}