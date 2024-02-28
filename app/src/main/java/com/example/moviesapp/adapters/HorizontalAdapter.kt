package com.example.moviesapp.adapters

import android.telephony.PhoneNumberUtils.formatNumber
import android.text.Layout.Directions
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.MovieDetailFragmentDirections
import com.example.moviesapp.MoviesFragmentDirections
import com.example.moviesapp.OnItemClickerListener
import com.example.moviesapp.PeopleDetailFragmentDirections
import com.example.moviesapp.R
import com.example.moviesapp.databinding.MoiveListItemBinding
import com.example.moviesapp.databinding.MovieListItemSecondBinding
import com.example.moviesapp.model.Movie

class HorizontalAdapter(
    private val itemCLickListener : OnItemClickerListener
    ,val isMovie: Boolean
): RecyclerView.Adapter<HorizontalAdapter.HorizontalMovieViewHolder>(){
    inner class HorizontalMovieViewHolder(val binding : MovieListItemSecondBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieName.text = movie.title
            val fullScore = movie.vote_average.toString()
            val trimmedScore = fullScore.substring(0,3)
            val releaseScore = trimmedScore + "(${formatNumber(movie.vote_count)})"
            binding.scoreList.text = releaseScore
            val seprated  = movie.release_date?.split("-")
            binding.release.text = seprated?.get(0) ?: "no data"
            val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
            val imageSize = "w500"
            val backdropPath = movie.poster_path
            val imageUrl = "$tmdbBaseUrl$imageSize$backdropPath"

            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.poserHorizontal)
        }
    }
    private val differCallback = object : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id== newItem.id &&
                    oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem==newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalMovieViewHolder {
        val binding = MovieListItemSecondBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalMovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: HorizontalMovieViewHolder, position: Int) {
        differ.currentList[position]?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
           itemCLickListener.onItemClick(differ.currentList[position],isMovie)
        }
    }

    fun formatNumber(number: Int): String {
        val absNumber = Math.abs(number.toDouble())

        return when {
            absNumber >= 1e6 -> String.format("%.1fM", number.toDouble() / 1e6)
            absNumber >= 1e3 -> String.format("%.1fK", number.toDouble() / 1e3)
            else -> number.toString()
        }
    }
}