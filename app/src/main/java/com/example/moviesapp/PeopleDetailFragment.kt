package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.moviesapp.api_responses.people.PeopleResponse
import com.example.moviesapp.databinding.FragmentPeopleDetailBinding
import com.example.moviesapp.viewModels.PersonDetailViewModel
import kotlinx.coroutines.launch

class PeopleDetailFragment : Fragment() {
    lateinit var binding : FragmentPeopleDetailBinding
    lateinit var viewModel : PersonDetailViewModel
     var personId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPeopleDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(PersonDetailViewModel::class.java)
        personId = arguments?.getInt("person_id")!!
        viewModel.getPersonDetail(personId)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateFlow.collect { value ->
                handleEvent(value)
                Log.i("imdb", "Received: $value")
            }
        }
    }

    fun handleEvent(event:String){
        when(event){
            "person detail info fetched" ->{
                setUpInfo(viewModel.personDetail)
            }


        }
    }
    fun setUpInfo(personDetail :PeopleResponse){
        binding.namePersonDetail.text = personDetail.name
        binding.career.text = personDetail.known_for_department
        val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
        val imageSize = "original"
        val backdropPath = personDetail.profile_path
        val imageUrl = "$tmdbBaseUrl$imageSize$backdropPath"
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(binding.imageView2)
        //TODO
    }
}