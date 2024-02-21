package com.example.moviesapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.ApiService
import com.example.moviesapp.RetrofitInstance
import com.example.moviesapp.api_responses.credt.Cast
import com.example.moviesapp.api_responses.people.PeopleResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class PersonDetailViewModel : ViewModel() {
    lateinit var personDetail : PeopleResponse
    private val retrofit : Retrofit = RetrofitInstance.getRetrofitInstance()
    private val apiService  = retrofit.create(ApiService::class.java)
    private val _stateFlow = MutableSharedFlow<String>()
    val stateFlow: MutableSharedFlow<String> get() = _stateFlow
    fun getPersonDetail(person_id :Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getPersonDetails(person_id)
                if (response.isSuccessful) {
                    val newData =  response.body() as PeopleResponse
                        personDetail = newData
                    emitEvent("person detail info fetched")
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