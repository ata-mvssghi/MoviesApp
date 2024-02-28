package com.example.moviesapp
interface BottomNavigatorCallback {
    fun showBottomNavigator(show: Boolean)
}
object  Constants {
    enum class ImageType{
        poster,
        backDrop,
        profile
    }
    enum class MovieType {
        popular,
        upComing,
        topRated,
        nowPlaying
    }
    val readtoken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYTVjZmVhZDQ1ZTE5NmNhOGE0MTYzMDM0ZmZkY2UwOCIsInN1YiI6IjY1MjY0NzU1ZWE4NGM3MDBjYTBlN2M2ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zMJIxrqfxB_-GewKu_lX6kGKoJTICw6AHtpF7_EfFRg"
    val baseUrl  = "https://api.themoviedb.org/3/"
}