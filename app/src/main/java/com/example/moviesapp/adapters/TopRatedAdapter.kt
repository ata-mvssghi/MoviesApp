package com.example.moviesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.databinding.MoiveListItemBinding
import com.example.moviesapp.model.Movie

class TopRatedAdapter(
): RecyclerView.Adapter<TopRatedAdapter.MovieViewHolder>(){
    inner class MovieViewHolder(val binding : MoiveListItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie,index : Int) {
            binding.movieTitle.text = "$index"+".${movie.title}"
            binding.language.text = movie.original_language
            val fullDate = movie.vote_average.toString()
            val trimmedDate = fullDate.substring(0,3)
            val releaseDate = trimmedDate + "(${formatNumber(movie.vote_count)})"
            binding.score.text = releaseDate
            val seprated  = movie.release_date.split("-")
            binding.prodDate.text = seprated[0]
            val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
            val imageSize = "w500"
            val backdropPath = movie.poster_path
            val imageUrl = "$tmdbBaseUrl$imageSize$backdropPath"

            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.poster)
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MoiveListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        differ.currentList[position]?.let { holder.bind(it,position+1) }
        holder.itemView.setOnClickListener {
           // naveController.navigate()
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