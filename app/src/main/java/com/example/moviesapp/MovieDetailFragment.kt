package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviesapp.adapters.ActorsAdapter
import com.example.moviesapp.adapters.HorizontalAdapter
import com.example.moviesapp.adapters.PhotosAdapter
import com.example.moviesapp.api_responses.credt.Cast
import com.example.moviesapp.api_responses.photos.toPhoto
import com.example.moviesapp.databinding.FragmentMovieDetailBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.PhotoDataClass
import com.example.moviesapp.viewModels.CreditViewModel
import com.example.moviesapp.viewModels.PhotosShardViewModel
import kotlinx.coroutines.launch

class MovieDetailFragment : Fragment()  , OnItemClickerListener{
    var isMovie : Boolean = true
    lateinit var binding : FragmentMovieDetailBinding
    lateinit var webView : WebView
    lateinit var viewModel : CreditViewModel
    private val photoViewModel : PhotosShardViewModel by activityViewModels()
    lateinit var actorsAdapter : ActorsAdapter
    lateinit var photosAdapter : PhotosAdapter
    lateinit var similarMoviesAdapter : HorizontalAdapter
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
        viewModel.getVideoKey(passedArgument.id,isMovie)
        setUpPrimaryViews(isMovie)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isMovie = arguments?.getBoolean("isMovie") ?: true
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateFlow.collect { value ->
                handleEvent(value)
                Log.i("imdb", "Received: $value")
            }
        }
        binding.morePhotosMovieDetail.setOnClickListener {
            photoViewModel.chosenMoviePhotoPosition = 0
            onPhotoCLickListener(0)
        }
        actorsAdapter = ActorsAdapter(findNavController())
        similarMoviesAdapter = HorizontalAdapter(this)
        photosAdapter = PhotosAdapter(this)
        val layoutManager1 = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL,false)
        binding.simirlarRecycler.layoutManager = layoutManager1
        binding.simirlarRecycler.adapter =similarMoviesAdapter
        val layoutManager2 = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL,false)
        binding.photoesRecycler.layoutManager = layoutManager2
        binding.photoesRecycler.adapter =photosAdapter
        val layoutManager3 = GridLayoutManager(requireContext(),2)
        binding.castRecycler.layoutManager = layoutManager3
        binding.castRecycler.adapter = actorsAdapter
        viewModel.getActorsList(passedArgument.id,isMovie)
        viewModel.getSimilarMoviesList(passedArgument.id,isMovie)
        viewModel.getImages(passedArgument.id,isMovie)

    }
    fun handleEvent(event:String){
        when(event){
            "actors list fetched" ->{
                val filteredList :MutableList<Cast> = mutableListOf()
                val fullList = viewModel.actors?.take(18)
                if (fullList != null) {
                    for(cast in fullList){
                        if(cast.known_for_department.equals("Acting"))
                            filteredList.add(cast)
                    }
                }
                actorsAdapter.differ.submitList(filteredList)
                binding.actorsProg.visibility = View.GONE
                Log.i("imdb",viewModel.actors?.size.toString())
                actorsAdapter.notifyDataSetChanged()
            }
            "video fetched"->{
                val key = viewModel.videoKey
                setUpWebView(key)
            }
            "similar movies fetched"->{
                similarMoviesAdapter.differ.submitList(viewModel.similarMovies)
                similarMoviesAdapter.notifyDataSetChanged()
                binding.SimilarProgress.visibility = View.GONE
            }
            "images fetched"->{
                val prevList  = viewModel.images!!
                val photos : MutableList<PhotoDataClass> = mutableListOf()
                val posters = prevList.posters.sortedBy { it.vote_average }.take(10).reversed()
                photos.addAll( posters.map { it.toPhoto() })
                val allPhotos : MutableList<PhotoDataClass> = mutableListOf()
                allPhotos.addAll(photos)
                allPhotos.addAll(prevList.backdrops.map { it.toPhoto() }.sortedBy { it.vote_average }.reversed())
                photosAdapter.differ.submitList(photos)
                photosAdapter.notifyDataSetChanged()
                photoViewModel.photoList = allPhotos
                binding.photesProgress.visibility = View.GONE
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
    fun setUpPrimaryViews(isMovie: Boolean){
        if(isMovie) {
            binding.nameDetail.text = passedArgument.title
            binding.review.text = passedArgument.overview
            binding.releaseDetail.text = passedArgument.release_date
            binding.languaeDetail.text = passedArgument.original_language
        }
        else{
            binding.nameDetail.text = passedArgument.title
            binding.review.text = passedArgument.overview
            binding.releaseDetail.text = passedArgument.release_date
            binding.languaeDetail.text = passedArgument.original_language
        }
        val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
        val imageSize = "w300"
        val backdropPath = passedArgument.poster_path
        val imageUrl = "$tmdbBaseUrl$imageSize$backdropPath"
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(binding.posterDetail)
    }

    override fun onPhotoCLickListener(position: Int) {
        photoViewModel.chosenMoviePhotoPosition = position
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToFullScreenFragement("moviePhoto")
        findNavController().navigate(action)
    }


    override fun onItemClick(movie: Movie , notImportantBool: Boolean) {
        val action =  MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(movie,isMovie)
        findNavController().navigate(action)
    }
}

