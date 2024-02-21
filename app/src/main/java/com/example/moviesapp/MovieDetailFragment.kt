package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviesapp.adapters.ActorsAdapter
import com.example.moviesapp.databinding.FragmentMovieDetailBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.viewModels.CreditViewModel
import kotlinx.coroutines.launch


class MovieDetailFragment : Fragment() {
    lateinit var binding : FragmentMovieDetailBinding
    lateinit var webView : WebView
    lateinit var viewModel : CreditViewModel
    lateinit var actorsAdapter : ActorsAdapter
    lateinit var passedArgument : Movie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(
            this
        ).get(CreditViewModel::class.java)
        webView = binding.webView
        val serializable =arguments?.getSerializable("movie")
        passedArgument = serializable as Movie
        viewModel.getVideoKey(passedArgument.id)
        setUpPrimaryViews()
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
        actorsAdapter = ActorsAdapter(findNavController())
        val layoutManager = GridLayoutManager(requireContext(),2)
        binding.castRecycler.layoutManager = layoutManager
        binding.castRecycler.adapter = actorsAdapter
        viewModel.getActorsList(passedArgument.id)
    }
    fun handleEvent(event:String){
        when(event){
            "actors list fetched" ->{
                val shortedList = viewModel.actors?.take(18)
                actorsAdapter.differ.submitList(shortedList)
                binding.actorsProg.visibility = View.GONE
                Log.i("imdb",viewModel.actors?.size.toString())
                actorsAdapter.notifyDataSetChanged()
            }
            "video fetched"->{
                val key = viewModel.videoKey
                setUpWebView(key)
            }

        }
    }
    fun setUpWebView(movieKey :String?){
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        // Load YouTube video URL
        val videoId = movieKey // Replace with the YouTube video ID
        val youtubeUrl = "https://www.youtube.com/embed/$videoId"
        webView.loadData("<iframe width=\"100%\" height=\"100%\" src=\"$youtubeUrl\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8")

        // Set a WebChromeClient to handle video playback
        webView.webChromeClient = WebChromeClient()
    }
    fun setUpPrimaryViews(){
        binding.nameDetail.text = passedArgument.title
        binding.review.text = passedArgument.overview
        binding.releaseDetail.text = passedArgument.release_date
        binding.languaeDetail.text = passedArgument.original_language
        val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
        val imageSize = "w300"
        val backdropPath = passedArgument.poster_path
        val imageUrl = "$tmdbBaseUrl$imageSize$backdropPath"
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(binding.posterDetail)
    }
}