package com.example.moviesapp.adapters

import android.telephony.PhoneNumberUtils.formatNumber
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.MovieDetailFragmentDirections
import com.example.moviesapp.R
import com.example.moviesapp.api_responses.credt.Cast
import com.example.moviesapp.databinding.ActorItemBinding

class ActorsAdapter(
    val navController: NavController
): RecyclerView.Adapter<ActorsAdapter.ActorsViewHolder>() {
    inner class ActorsViewHolder(val binding: ActorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.acrtorName.text = cast.original_name
            binding.character.text = cast.character
            val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
            val imageSize = "w300"
            val backdropPath = cast.profile_path
            val imageUrl = "$tmdbBaseUrl$imageSize$backdropPath"

            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.profile)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.original_name == newItem.original_name
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        val binding = ActorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        differ.currentList[position]?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToPeopleDetailFragment(differ.currentList[position].id)
            navController.navigate(action)
            // naveController.navigate()
        }
    }
}