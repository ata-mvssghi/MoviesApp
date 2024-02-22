package com.example.moviesapp

import com.example.moviesapp.api_responses.TopRatedResponse
import com.example.moviesapp.api_responses.credt.CreditResponse
import com.example.moviesapp.api_responses.horizontal_list_movies.HorizontalListMoviesResponse
import com.example.moviesapp.api_responses.people.PeopleResponse
import com.example.moviesapp.api_responses.similar_movies.SimilarMoviesResponse
import com.example.moviesapp.api_responses.video.Video
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
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
    ): Response<CreditResponse>
    @GET("movie/{movie_id}/videos")
    @Headers("accept: application/json", "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYTVjZmVhZDQ1ZTE5NmNhOGE0MTYzMDM0ZmZkY2UwOCIsInN1YiI6IjY1MjY0NzU1ZWE4NGM3MDBjYTBlN2M2ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zMJIxrqfxB_-GewKu_lX6kGKoJTICw6AHtpF7_EfFRg")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId: Int,
    ): Response<Video>
    @GET("person/{person_id}")
    @Headers("accept: application/json", "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYTVjZmVhZDQ1ZTE5NmNhOGE0MTYzMDM0ZmZkY2UwOCIsInN1YiI6IjY1MjY0NzU1ZWE4NGM3MDBjYTBlN2M2ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zMJIxrqfxB_-GewKu_lX6kGKoJTICw6AHtpF7_EfFRg")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
    ): Response<PeopleResponse>
    @GET("person/{person_id}/movie_credits")
    @Headers("accept: application/json", "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYTVjZmVhZDQ1ZTE5NmNhOGE0MTYzMDM0ZmZkY2UwOCIsInN1YiI6IjY1MjY0NzU1ZWE4NGM3MDBjYTBlN2M2ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zMJIxrqfxB_-GewKu_lX6kGKoJTICw6AHtpF7_EfFRg")
    suspend fun getPersonCredits(
        @Path("person_id") personId: Int,
    ): Response<HorizontalListMoviesResponse>
    @GET("movie/{movie_id}/similar")
    @Headers("accept: application/json", "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYTVjZmVhZDQ1ZTE5NmNhOGE0MTYzMDM0ZmZkY2UwOCIsInN1YiI6IjY1MjY0NzU1ZWE4NGM3MDBjYTBlN2M2ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zMJIxrqfxB_-GewKu_lX6kGKoJTICw6AHtpF7_EfFRg")
    suspend fun getSimilarMovies(
        @Path("movie_id") personId: Int,
    ): Response<SimilarMoviesResponse>
}