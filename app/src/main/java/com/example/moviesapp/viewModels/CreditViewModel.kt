package com.example.moviesapp.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.ApiService
import com.example.moviesapp.RetrofitInstance
import com.example.moviesapp.api_responses.credt.Cast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.lang.Exception

class CreditViewModel(): ViewModel() {
     var actors : MutableList<Cast>?=null
    private val retrofit : Retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService  = retrofit.create(ApiService::class.java)
    private val _stateFlow = MutableSharedFlow<String>()
    val stateFlow: MutableSharedFlow<String> get() = _stateFlow
    fun getActorsList(movieId :Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getMovieCredits(movieId)
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
    private fun emitEvent(event: String) {
        viewModelScope.launch {
            _stateFlow.emit(event)
            Log.i("imdb", "Emitting: $event")
        }
    }
}