package com.example.moviesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.Constants
import com.example.moviesapp.MoviesFragmentDirections
import com.example.moviesapp.R
import com.example.moviesapp.SearchFragmentDirections
import com.example.moviesapp.api_responses.search.SearchResponse
import com.example.moviesapp.databinding.MoiveListItemBinding
import com.example.moviesapp.databinding.SearchItemBinding
import com.example.moviesapp.model.Movie
import kotlinx.coroutines.NonDisposableHandle.parent

class SearchAdapter(
    val naveController : NavController
): RecyclerView.Adapter<SearchAdapter.ResultViewHolder>(){
    inner class ResultViewHolder(val binding : SearchItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: SearchResponse) {
            if(result.media_type.equals("movie")){
                hidePeople(binding)
                    binding.movieTitleSearch.text = result.title
                    binding.scoreSearch.text = result.vote_average.toString()
                    binding.productionSearch.text = result.release_date
                    val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
                    var imageSize : String? = "w500"
                    val imageUrl = "$tmdbBaseUrl$imageSize${result.poster_path}"
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.moviePoster)

            }
            else if(result.media_type.equals("tv")){
                hidePeople(binding)
                binding.movieTitleSearch.text = result.name
                binding.scoreSearch.text = result.vote_average.toString()
                binding.productionSearch.text = result.first_air_date
                val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
                var imageSize : String? = "w500"
                val imageUrl = "$tmdbBaseUrl$imageSize${result.poster_path}"
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.moviePoster)
            }
            else {
                hideMovie(binding)
                binding.acrtorName.text = result.name
                val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
                var imageSize : String? = "w185"
                val imageUrl = "$tmdbBaseUrl$imageSize${result.profile_path}"
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.profile)
            }

        }
    }
    private val differCallback = object : DiffUtil.ItemCallback<SearchResponse>(){
        override fun areItemsTheSame(oldItem: SearchResponse, newItem: SearchResponse): Boolean {
            return oldItem.id== newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchResponse, newItem: SearchResponse): Boolean {
            return oldItem==newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ResultViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ResultViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.ResultViewHolder, position: Int) {
        differ.currentList[position]?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            val currentItem = differ.currentList.get(position)
            if(currentItem.media_type.equals("movie")){
                val movie  = Movie(currentItem.id!!,currentItem.original_language,currentItem.poster_path,currentItem.name,currentItem.vote_average!!,currentItem.vote_count!!
                    ,currentItem.original_language!!,currentItem.release_date,currentItem.popularity!!,currentItem.overview!!)
                val action = SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(movie,true)
                naveController.navigate(action)
            }
            else if(currentItem.media_type.equals("tv")){
                val movie  = Movie(currentItem.id!!,currentItem.original_language,currentItem.poster_path,currentItem.title,currentItem.vote_average!!,currentItem.vote_count!!
                    ,currentItem.original_language!!,currentItem.release_date,currentItem.popularity!!,currentItem.overview!!)
                val action = SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(movie,false)
                naveController.navigate(action)
            }
            else{
                val action = SearchFragmentDirections.actionSearchFragmentToPeopleDetailFragment(currentItem.id!!)
                naveController.navigate(action)
            }
        }
    }
    fun hideMovie(binding: SearchItemBinding){
        binding.personSearchCardView.visibility = View.VISIBLE
        binding.acrtorName.visibility = View.VISIBLE
        binding.movieTitleSearch.visibility = View.GONE
        binding.scoreSearch.visibility = View.GONE
        binding.productionSearch.visibility = View.GONE
        binding.moviePoster.visibility = View.GONE
    }
    fun hidePeople(binding: SearchItemBinding){
        binding.personSearchCardView.visibility = View.GONE
        binding.acrtorName.visibility = View.GONE
        binding.movieTitleSearch.visibility = View.VISIBLE
        binding.scoreSearch.visibility = View.VISIBLE
        binding.productionSearch.visibility = View.VISIBLE
        binding.moviePoster.visibility = View.VISIBLE
    }


}