package com.example.moviesapp.repository

import android.content.Context
import android.util.Log
import com.example.moviesapp.ApiService
import com.example.moviesapp.RetrofitInstance
import com.example.moviesapp.api_responses.Movie_Api
import com.example.moviesapp.api_responses.TopRatedResponse
import com.example.moviesapp.api_responses.fromMovieToMovie
import com.example.moviesapp.api_responses.fromSeriesToMovie
import com.example.moviesapp.model.Movie
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception

class TopRatedMoviesRepo(
    context : Context
) {
    private val retrofit : Retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService  = retrofit.create(ApiService::class.java)
    suspend fun getTopRatedMovies(page :Int,isMovie: Boolean) : List<Movie>?
    {
        try {
            val apiResponse :Response<TopRatedResponse>
            if(isMovie)
                apiResponse = apiService.getTopRatedMovies(page)
            else
                apiResponse =apiService.getTopRatedSeries(page)
            if (apiResponse.isSuccessful) {
                Log.i("imdb", "result size = "+apiResponse.body()?.results?.size.toString())
                if(isMovie)
                    return  apiResponse.body()?.results?.map { it.fromMovieToMovie() }
                else
                    return  apiResponse.body()?.results?.map { it.fromSeriesToMovie() }
            }
            else {
                val errorCode = apiResponse.code()
                exposeError(errorCode,apiResponse.message())
                Log.i("imdb", "top rated movie api call failed");
                Log.i("imdb", apiResponse.errorBody().toString())
            }
        }
        catch (e:Exception){
            Log.e("imdb",e.message.toString())
        }
        return null
    }
    private fun exposeError(code:Int,message:String) {
        when (code) {
            400 -> {
                // Bad Request
                Log.e("imdb", "Bad Request: $message")
            }
            401 -> {
                // Unauthorized
                Log.e("imdb", "Unauthorized: $message")
            }
            403 -> {
                // Forbidden
                Log.e("imdb", "Forbidden: $message")
            }
            404 -> {
                // Not Found
                Log.e("imdb", "Not Found: $message")
            }
            500 -> {
                // Internal Server Error
                Log.e("imdb", "Internal Server Error: $message")
            }
            else -> {
                Log.e("imdb", "Unexpected Error: $message")
            }
        }
    }
}