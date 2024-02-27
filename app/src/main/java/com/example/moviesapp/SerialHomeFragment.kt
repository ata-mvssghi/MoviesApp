package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.adapters.HorizontalAdapter
import com.example.moviesapp.databinding.FragmentMovieHomeBinding
import com.example.moviesapp.databinding.FragmentSerialHomeBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.viewModels.MovieHomeViewModel
import kotlinx.coroutines.launch

class SerialHomeFragment : Fragment() ,OnItemClickerListener{
    lateinit var binding : FragmentSerialHomeBinding
    lateinit var viewModel : MovieHomeViewModel
    lateinit var topRatedAdapter : HorizontalAdapter
    lateinit var upComingAdapter : HorizontalAdapter
    lateinit var popularAdapter : HorizontalAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSerialHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(MovieHomeViewModel::class.java)
        initiateAdapter()
        viewModel.getTopRatedMovies(1,false)
        viewModel.getPopular(1,false)
        viewModel.getUpcoming(1,false)
        viewModel.getNowPlaying(1,false)
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
        binding.allPopularSerial.setOnClickListener {
            val action = SerialHomeFragmentDirections.actionSerialHomeFragmentToMoviesFragment(false)
            findNavController().navigate(action)
        }
        binding.allTopSerial.setOnClickListener {
            val action = SerialHomeFragmentDirections.actionSerialHomeFragmentToMoviesFragment(false)
            findNavController().navigate(action)
        }
        binding.allOnAir.setOnClickListener {
            val action = SerialHomeFragmentDirections.actionSerialHomeFragmentToMoviesFragment(false)
            findNavController().navigate(action)
        }

        setUpRecyclers()
    }
    fun handleEvent(event:String){
        when(event){
            "top rated series fetched successfully" ->{
                topRatedAdapter.differ.submitList(viewModel.topRatedMovies)
                topRatedAdapter.notifyDataSetChanged()
            }
            "upcoming series fetched successfully" ->{
                upComingAdapter.differ.submitList(viewModel.upComing)
                upComingAdapter.notifyDataSetChanged()
            }
            "popular series fetched successfully" ->{
                popularAdapter.differ.submitList(viewModel.popularMovies)
                popularAdapter.notifyDataSetChanged()
            }

        }
    }
    fun setUpRecyclers(){
        val layoutManager1 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.topRatedSerialRec.layoutManager = layoutManager1
        binding.topRatedSerialRec.adapter = topRatedAdapter
        val layoutManager2 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.popularSerialRec.layoutManager = layoutManager2
        binding.popularSerialRec.adapter = popularAdapter
        val layoutManager3 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.onTheAirSerialRec.layoutManager = layoutManager3
        binding.onTheAirSerialRec.adapter = upComingAdapter
    }
    fun initiateAdapter(){
        topRatedAdapter = HorizontalAdapter(this)
        upComingAdapter = HorizontalAdapter(this)
        popularAdapter = HorizontalAdapter(this)
    }
    override fun onPhotoCLickListener(position: Int) {
        //We have nothing to do with this function here
    }

    override fun onItemClick(movie: Movie, isMovie: Boolean) {
        val action = SerialHomeFragmentDirections.actionSerialHomeFragmentToMovieDetailFragment(movie,isMovie)
        findNavController().navigate(action)
    }

}