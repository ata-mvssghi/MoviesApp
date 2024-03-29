package com.example.moviesapp

import com.example.moviesapp.Constants.ApiKey
import com.example.moviesapp.api_responses.TopRatedResponse
import com.example.moviesapp.api_responses.credt.CreditResponse
import com.example.moviesapp.api_responses.horizontal_list_movies.HorizontalListMoviesResponse
import com.example.moviesapp.api_responses.people.PeopleResponse
import com.example.moviesapp.api_responses.personImages.PeopleProfiles
import com.example.moviesapp.api_responses.photos.PhotosResponse
import com.example.moviesapp.api_responses.search.Multi
import com.example.moviesapp.api_responses.similar_movies.SimilarMoviesResponse
import com.example.moviesapp.api_responses.video.Video
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/top_rated?language=en-US")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getTopRatedMovies(@Query("page") page: Int) : Response<TopRatedResponse>
    @GET("tv/on_the_air?language=en-US")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getOnTheAirSerials(@Query("page") page: Int) : Response<TopRatedResponse>
    @GET("tv/top_rated?language=en-US")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getTopRatedSerials(@Query("page") page: Int) : Response<TopRatedResponse>
    @GET("movie/upcoming")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getUpComing(@Query("page") page: Int) : Response<TopRatedResponse>
    @GET("tv/popular")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getPopularSerial(@Query("page") page: Int) : Response<TopRatedResponse>
    @GET("movie/now_playing")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getNowPaying(@Query("page") page: Int) : Response<TopRatedResponse>
    @GET("movie/popular")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getPopular(@Query("page") page: Int) : Response<TopRatedResponse>
    @GET("tv/top_rated?language=en-US")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getTopRatedSeries(@Query("page") page: Int) : Response<TopRatedResponse>


    @GET("movie/{movie_id}/credits")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
    ): Response<CreditResponse>
    @GET("tv/{series_id}/credits")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getSerialCredits(
        @Path("series_id") serialId: Int,
    ): Response<CreditResponse>
    @GET("movie/{movie_id}/videos")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getMovieVideo(
        @Path("movie_id") movieId: Int,
    ): Response<Video>
    @GET("tv/{series_id}/videos")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getSerialVideo(
        @Path("series_id") movieId: Int,
    ): Response<Video>
    @GET("person/{person_id}")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
    ): Response<PeopleResponse>
    @GET("person/{person_id}/movie_credits")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getPersonCredits(
        @Path("person_id") personId: Int,
    ): Response<HorizontalListMoviesResponse>
    @GET("movie/{series_id}/similar")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getSimilarMovies(
        @Path("series_id") personId: Int,
    ): Response<SimilarMoviesResponse>
    @GET("tv/{series_id}/similar")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getSimilarSeries(
        @Path("series_id") personId: Int,
    ): Response<SimilarMoviesResponse>
    @GET("movie/{movie_id}/images")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getMoviePictures(
        @Path("movie_id") movieId: Int,
    ): Response<PhotosResponse>
    @GET("tv/{series_id}/images")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getSerialPictures(
        @Path("series_id") serialId: Int,
    ): Response<PhotosResponse>
    @GET("person/{person_id}/images")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun getPersonProfiles(
        @Path("person_id") personId: Int,
    ): Response<PeopleProfiles>
    @GET("search/multi")
    @Headers("accept: application/json", "Authorization: Bearer $ApiKey")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<Multi>
}