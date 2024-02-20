package com.example.moviesapp

import com.example.moviesapp.api_responses.TopRatedResponse
import com.example.moviesapp.api_responses.credt.CreditResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/top_rated?language=en-US")
    @Headers("accept: application/json", "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYTVjZmVhZDQ1ZTE5NmNhOGE0MTYzMDM0ZmZkY2UwOCIsInN1YiI6IjY1MjY0NzU1ZWE4NGM3MDBjYTBlN2M2ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zMJIxrqfxB_-GewKu_lX6kGKoJTICw6AHtpF7_EfFRg")
    suspend fun getTopRatedMovies(@Query("page") page: Int) : Response<TopRatedResponse>

    @GET("movie/{movie_id}/credits")
    @Headers("accept: application/json", "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYTVjZmVhZDQ1ZTE5NmNhOGE0MTYzMDM0ZmZkY2UwOCIsInN1YiI6IjY1MjY0NzU1ZWE4NGM3MDBjYTBlN2M2ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zMJIxrqfxB_-GewKu_lX6kGKoJTICw6AHtpF7_EfFRg")
    fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<CreditResponse>
}