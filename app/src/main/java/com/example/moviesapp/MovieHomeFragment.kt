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
        viewModel.getTopRatedMovies(1,true)
        viewModel.getPopular(1,true)
        viewModel.getUpcoming(1,true)
        viewModel.getNowPlaying(1,true)
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
        binding.button.setOnClickListener {
            val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMoviesFragment(false)
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
        binding.topRatedHomeRecycler.layoutManager = layoutManager1
        binding.topRatedHomeRecycler.adapter = topRatedAdapter
        val layoutManager2 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.popularHomeRecycler.layoutManager = layoutManager2
        binding.popularHomeRecycler.adapter = popularAdapter
        val layoutManager3 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.upcomingHomeRecycler.layoutManager = layoutManager3
        binding.upcomingHomeRecycler.adapter = upComingAdapter
        val layoutManager4 = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.nowPlayingHomeRecycler.layoutManager = layoutManager4
        binding.nowPlayingHomeRecycler.adapter = nowPlayingAdapter
    }
    fun initiateAdapter(){
        topRatedAdapter = HorizontalAdapter(this)
        upComingAdapter = HorizontalAdapter(this)
        nowPlayingAdapter = HorizontalAdapter(this)
        popularAdapter = HorizontalAdapter(this)
    }

    override fun onPhotoCLickListener(position: Int) {
        //We have nothing to do with this function here
    }

    override fun onItemClick(movie: Movie, isMovie: Boolean) {
        val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMovieDetailFragment2(movie,isMovie)
        findNavController().navigate(action)
    }
}