package com.example.moviesapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.ApiService
import com.example.moviesapp.RetrofitInstance
import com.example.moviesapp.api_responses.credt.Cast
import com.example.moviesapp.api_responses.horizontal_list_movies.HorizontalListMoviesResponse
import com.example.moviesapp.api_responses.horizontal_list_movies.toMovie
import com.example.moviesapp.api_responses.people.PeopleResponse
import com.example.moviesapp.api_responses.personImages.PeopleProfiles
import com.example.moviesapp.api_responses.personImages.toPhotoDataClass
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.PhotoDataClass
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class PersonDetailViewModel : ViewModel() {
    lateinit var creditList : MutableList<Movie>
    lateinit var personDetail : PeopleResponse
    lateinit var profilePics : List<PhotoDataClass>
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
                    Log.i("imdb","person id = ${response.body()!!.id}")
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
    fun getCreditList(person_id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getPersonCredits(person_id)
                if (response.isSuccessful) {
                    val newData =  response.body() as HorizontalListMoviesResponse
                    val list = newData.cast
                    val newList : MutableList<Movie> = mutableListOf()
                    for(oldItem in list){
                        newList.add(oldItem.toMovie())
                    }
                    creditList = newList
                    emitEvent("credit fetched")
                } else {
                    Log.e("imdb",response.message().toString())
                    response.errorBody()?.string()?.let { Log.e("imdb", it) }
                    Log.e("imdb",response.code().toString())
                }
            }
            catch (e:Exception){
                Log.e("imdb",e.message.toString())
            }
        }
    }
    fun getPersonProfile(person_id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getPersonProfiles(person_id)
                if (response.isSuccessful) {
                    val newData =  response.body() as PeopleProfiles
                    val list = newData.profiles
                    val newList : MutableList<PhotoDataClass> = mutableListOf()
                    newList.addAll(list.map { it.toPhotoDataClass() })
                    profilePics = newList
                    emitEvent("profiles fetched")
                } else {
                    Log.e("imdb",response.message().toString())
                    response.errorBody()?.string()?.let { Log.e("imdb", it) }
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