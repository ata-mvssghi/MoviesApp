package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.adapters.HorizontalAdapter
import com.example.moviesapp.adapters.TopRatedAdapter
import com.example.moviesapp.databinding.FragmentMovieHomeBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.viewModels.MovieHomeViewModel
import kotlinx.coroutines.launch

class MovieHomeFragment : Fragment() , OnItemClickerListener{
    lateinit var binding : FragmentMovieHomeBinding
    lateinit var viewModel : MovieHomeViewModel
    lateinit var topRatedAdapter : HorizontalAdapter
    lateinit var upComingAdapter : HorizontalAdapter
    lateinit var popularAdapter : HorizontalAdapter
    lateinit var nowPlayingAdapter : HorizontalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(MovieHomeViewModel::class.java)
        initiateAdapter()
        getTheData()
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
        binding.allPopularrSerial.setOnClickListener {
            val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMoviesFragment(true,Constants.MovieType.popular)
            findNavController().navigate(action)
        }
        binding.allNowPlayingSerial.setOnClickListener {
            val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMoviesFragment(true,Constants.MovieType.nowPlaying)
            findNavController().navigate(action)
        }
        binding.allTopRatedSerial.setOnClickListener {
            val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMoviesFragment(true,Constants.MovieType.topRated)
            findNavController().navigate(action)
        }
        binding.allUpComingSerial.setOnClickListener {
            val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMoviesFragment(true , Constants.MovieType.upComing)
            findNavController().navigate(action)
        }
        setUpRecyclers()

    }
    fun handleEvent(event:String){
        when(event){
            "top rated movies fetched successfully" ->{
                topRatedAdapter.differ.submitList(viewModel.topRatedMovies)
                topRatedAdapter.notifyDataSetChanged()
            }
            "upcoming movies fetched successfully" ->{
                upComingAdapter.differ.submitList(viewModel.upComing)
                upComingAdapter.notifyDataSetChanged()
            }
            "popular movies fetched successfully" ->{
                popularAdapter.differ.submitList(viewModel.popularMovies)
                popularAdapter.notifyDataSetChanged()
            }
            "now playing movies fetched successfully" ->{
                nowPlayingAdapter.differ.submitList(viewModel.nowPlaying)
                nowPlayingAdapter.notifyDataSetChanged()
            }



        }
    }
    fun setUpRecyclers(){
        val layoutManager1 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.topRatedMovies.layoutManager = layoutManager1
        binding.topRatedMovies.adapter = topRatedAdapter
        val layoutManager2 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.popularMovies.layoutManager = layoutManager2
        binding.popularMovies.adapter = popularAdapter
        val layoutManager3 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.upComingMovies.layoutManager = layoutManager3
        binding.upComingMovies.adapter = upComingAdapter
        val layoutManager4 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.nowPlayingMovies.layoutManager = layoutManager4
        binding.nowPlayingMovies.adapter = nowPlayingAdapter
    }
    fun initiateAdapter(){
        topRatedAdapter = HorizontalAdapter(this,true)
        upComingAdapter = HorizontalAdapter(this,true)
        nowPlayingAdapter = HorizontalAdapter(this,true)
        popularAdapter = HorizontalAdapter(this,true)
    }

    override fun onPhotoCLickListener(position: Int) {
        //We have nothing to do with this function here
    }

    override fun onItemClick(movie: Movie, isMovie: Boolean) {
        val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMovieDetailFragment2(movie,isMovie)
        findNavController().navigate(action)
    }
    fun getTheData(){
        if(viewModel.topRatedMovies == null)
            viewModel.getTopRatedMovies(1,true)

        else{
            topRatedAdapter.differ.submitList(viewModel.topRatedMovies)
            topRatedAdapter.notifyDataSetChanged()
        }
        if(viewModel.popularMovies == null)
            viewModel.getPopular(1,true)
        else {
            popularAdapter.differ.submitList(viewModel.popularMovies)
            popularAdapter.notifyDataSetChanged()
        }
        if(viewModel.upComing == null)
            viewModel.getUpcoming(1,true)
        else {
            upComingAdapter.differ.submitList(viewModel.popularMovies)
            upComingAdapter.notifyDataSetChanged()
        }
        if(viewModel.nowPlaying == null)
            viewModel.getNowPlaying(1,true)
        else{
            nowPlayingAdapter.differ.submitList(viewModel.nowPlaying)
            nowPlayingAdapter.notifyDataSetChanged()
        }
    }
}