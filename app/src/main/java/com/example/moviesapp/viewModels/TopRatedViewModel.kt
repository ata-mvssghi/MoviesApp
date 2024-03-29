package com.example.moviesapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.Constants
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.TopRatedMoviesRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.function.BinaryOperator

class TopRatedViewModel(
    private val repository : TopRatedMoviesRepo
): ViewModel() {
    var topRatedMovies : MutableList<Movie>? = null
    private val _stateFlow = MutableSharedFlow<String>()
    val stateFlow: MutableSharedFlow<String> get() = _stateFlow
    fun getTopRatedMovies(page:Int, isMovie :Boolean,movieType : Constants.MovieType){
        viewModelScope.launch {
            try {
                Log.i("imdb","top rated movie api called with page = $page")
                val result = repository.getTopRatedMovies(page,isMovie,movieType) ?: throw Exception("something went wrong ")
                val newData =  result as MutableList<Movie>?
                if(topRatedMovies == null)
                    topRatedMovies = newData
                else
                    newData?.let { topRatedMovies!!.addAll(it) }
                emitEvent("top rated movies fetched successfully")
            }
            catch (e:Exception){
                Log.e("imdb","top rated movies fetching failed")
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