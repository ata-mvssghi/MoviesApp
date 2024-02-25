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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2.Orientation
import com.bumptech.glide.Glide
import com.example.moviesapp.adapters.ActorsAdapter
import com.example.moviesapp.adapters.HorizontalAdapter
import com.example.moviesapp.api_responses.people.PeopleResponse
import com.example.moviesapp.databinding.FragmentPeopleDetailBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.viewModels.PersonDetailViewModel
import kotlinx.coroutines.launch

class PeopleDetailFragment : Fragment() , OnItemClickerListener {
    lateinit var binding : FragmentPeopleDetailBinding
    lateinit var viewModel : PersonDetailViewModel
    lateinit var creditAdadpter : HorizontalAdapter
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
        creditAdadpter = HorizontalAdapter(this)
        viewModel.getCreditList(personId)
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
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.creditsRecyclerView.layoutManager = layoutManager
        binding.creditsRecyclerView.adapter = creditAdadpter
    }

    fun handleEvent(event:String){
        when(event){
            "person detail info fetched" ->{
                setUpInfo(viewModel.personDetail)
            }
            "credit fetched"->{
                val originalList = viewModel.creditList
                val sortedList = originalList.sortedBy { it.popularity }
                creditAdadpter.differ.submitList(sortedList.reversed())
                creditAdadpter.notifyDataSetChanged()
                binding.perosonCreditProg.visibility = View.GONE
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
        binding.biography.text = personDetail.biography
        binding.Born.text = personDetail.birthday
        if(personDetail.deathday != null) {
            binding.deathDate.text = personDetail.deathday.toString()
            binding.deadOrNot.visibility = View.VISIBLE
            binding.deathDate.visibility = View.VISIBLE
            binding.deadOrNot.text = "BORN"
        }
    }

    override fun onPhotoCLickListener(position: Int) {
        //We have nothing to do with this function here
    }


    override fun onItemClick(movie: Movie, isMovie: Boolean) {
        val action = PeopleDetailFragmentDirections.actionPeopleDetailFragmentToMovieDetailFragment(movie,isMovie)
        findNavController().navigate(action)
    }
}