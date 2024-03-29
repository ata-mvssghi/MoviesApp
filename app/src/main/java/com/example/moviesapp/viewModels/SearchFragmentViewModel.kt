package com.example.moviesapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.ApiService
import com.example.moviesapp.RetrofitInstance
import com.example.moviesapp.api_responses.search.SearchResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.lang.Exception

class SearchFragmentViewModel : ViewModel(){
     var listToBeAdded : List<SearchResponse>? = null
     var result  : MutableList<SearchResponse> = mutableListOf()
    private val _stateFlow = MutableSharedFlow<String>()
    val stateFlow: MutableSharedFlow<String> get() = _stateFlow
    private val retrofit : Retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService  = retrofit.create(ApiService::class.java)
    fun searchQuery(query:String , page : Int){
        viewModelScope.launch {
            try {
                val response = apiService.searchMulti(query,page)
                if(response.isSuccessful){
                    listToBeAdded = response.body()?.results
                    if(page == 1){
                        result = listToBeAdded?.toMutableList()!!
                    }
                    else {
                        listToBeAdded?.toMutableList()?.let { result.addAll(it) }
                    }

                    emitEvent("search result fetched")
                }
                else{
                    Log.e("imdb",response.message())
                    Log.e("imdb",response.errorBody().toString())
                }
            }
            catch (e:Exception){
                Log.e("imdb",e.message.toString())
                Log.e("imdb",e.printStackTrace().toString())
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