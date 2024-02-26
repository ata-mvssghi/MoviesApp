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
import com.example.moviesapp.Constants
import com.example.moviesapp.MovieDetailFragmentDirections
import com.example.moviesapp.MoviesFragmentDirections
import com.example.moviesapp.OnItemClickerListener
import com.example.moviesapp.PeopleDetailFragmentDirections
import com.example.moviesapp.R
import com.example.moviesapp.api_responses.photos.Backdrop
import com.example.moviesapp.api_responses.photos.PhotosResponse
import com.example.moviesapp.api_responses.photos.Poster
import com.example.moviesapp.databinding.MoiveListItemBinding
import com.example.moviesapp.databinding.MovieListItemSecondBinding
import com.example.moviesapp.databinding.PhotoItemBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.PhotoDataClass

class PhotosAdapter(
    private val itemCLickListener : OnItemClickerListener,
): RecyclerView.Adapter<PhotosAdapter.HorizontalPhotosViewHolder>(){
    inner class HorizontalPhotosViewHolder(val binding : PhotoItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoDataClass) {
                val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
            var imageSize : String? = "w185"
            if(photo.type == Constants.ImageType.poster)
                 imageSize = "w500"
            else if(photo.type == Constants.ImageType.backDrop)
                imageSize = "w300"
            else if(photo.type == Constants.ImageType.profile)
                imageSize == "w185"

                val imageUrl = "$tmdbBaseUrl$imageSize${photo.file_path}"
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.photoItem)


        }
    }
    private val differCallback = object : DiffUtil.ItemCallback<PhotoDataClass>(){
        override fun areItemsTheSame(oldItem: PhotoDataClass, newItem: PhotoDataClass): Boolean {
            return oldItem.file_path== newItem.file_path
        }

        override fun areContentsTheSame(oldItem: PhotoDataClass, newItem: PhotoDataClass): Boolean {
            return oldItem==newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalPhotosViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalPhotosViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: HorizontalPhotosViewHolder, position: Int) {
        differ.currentList[position]?.let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            itemCLickListener.onPhotoCLickListener(position)
        }
    }

}