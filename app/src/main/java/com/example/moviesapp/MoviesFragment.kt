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
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.adapters.TopRatedAdapter
import com.example.moviesapp.databinding.FragmentMoviesBinding
import com.example.moviesapp.repository.TopRatedMoviesRepo
import com.example.moviesapp.viewModels.TopRatedMoviesViewModelFactory
import com.example.moviesapp.viewModels.TopRatedViewModel
import kotlinx.coroutines.launch
import kotlin.math.log

class MoviesFragment : Fragment() {
    var isMovie : Boolean = true
    var page = 1
    lateinit var binding : FragmentMoviesBinding
    lateinit var viewModel  : TopRatedViewModel
    lateinit var moviesAdapter : TopRatedAdapter
    lateinit var movieType : Constants.MovieType



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoviesBinding.inflate(inflater)
        val movieRepo = this.context?.let { TopRatedMoviesRepo(it) }
        viewModel = ViewModelProvider(
            this,
            TopRatedMoviesViewModelFactory(movieRepo!!)
        ).get(TopRatedViewModel::class.java)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.topRatedMoivesList.layoutManager = layoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("imdb", "on view created called")
        isMovie = arguments?.getBoolean("isMovie") ?: true
        Log.i("imdb","is movie in movie fragment is =$isMovie")
        movieType = arguments?.get("movieType") as Constants.MovieType
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateFlow.collect { value ->
                handleEvent(value)
                Log.i("imdb", "Received: $value")
            }
        }

        moviesAdapter = TopRatedAdapter(findNavController(),isMovie)
        binding.topRatedMoivesList.adapter = moviesAdapter
        var isScrolling = false
        val layoutManager = binding.topRatedMoivesList.layoutManager as LinearLayoutManager
        if(viewModel.topRatedMovies == null) {
            viewModel.getTopRatedMovies(1,isMovie,movieType)
            Log.i("imdb", "view model list was null")
        }
        else{
            page = viewModel.topRatedMovies!!.size/20
            Log.i("imdb","view model list was not null and the page now is = $page")
            moviesAdapter.differ.currentList.clear()
            moviesAdapter.differ.submitList(viewModel.topRatedMovies)
            moviesAdapter.notifyDataSetChanged()
        }

        binding.topRatedMoivesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(isScrolling) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = moviesAdapter.itemCount
                    val pastVisibleItemCount =
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    //if user scrolls till second last row then new data will be fetched
                    if (visibleItemCount + pastVisibleItemCount >= totalItemCount-2) {
                        // Load more data
                        viewModel.getTopRatedMovies(++page,isMovie,movieType)
                        Log.i("imdb","fetching movies with page  = $page")
                        //binding.loadMoreProgress.visibility = View.VISIBLE
                        isScrolling = false
                    }
                }
            }
        })

    }
    fun handleEvent(event:String){
        when(event){
            "top rated movies fetched successfully" ->{
                moviesAdapter.differ.submitList(viewModel.topRatedMovies)
                binding.progressBar.visibility = View.GONE
                Log.i("imdb","new list size  = ${viewModel.topRatedMovies?.size}")
                moviesAdapter.notifyDataSetChanged()
            }
        }
    }

}