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
import com.example.moviesapp.api_responses.photos.Backdrop
import com.example.moviesapp.api_responses.photos.PhotosResponse
import com.example.moviesapp.api_responses.photos.Poster
import com.example.moviesapp.databinding.MoiveListItemBinding
import com.example.moviesapp.databinding.MovieListItemSecondBinding
import com.example.moviesapp.databinding.PhotoItemBinding
import com.example.moviesapp.databinding.PhotoRealSizeItemBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.PhotoDataClass
interface OnPhotoItemClickListener {
    fun onItemClick(position: Int)
}
class FullScreenPhotoAdapter(
   val listener: OnPhotoItemClickListener
): RecyclerView.Adapter<FullScreenPhotoAdapter.HorizontalPhotosFullScreenViewHolder>(){
    inner class HorizontalPhotosFullScreenViewHolder(val binding : PhotoRealSizeItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoDataClass) {
            val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
            val imageSize : String = "original"
            val imageUrl = "$tmdbBaseUrl$imageSize${photo.file_path}"
            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.originalSizePhoto)


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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalPhotosFullScreenViewHolder {
        val binding = PhotoRealSizeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalPhotosFullScreenViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: HorizontalPhotosFullScreenViewHolder, position: Int) {
        differ.currentList[position]?.let { holder.bind(it) }
        holder.binding.donwLoad.setOnClickListener {
            listener.onItemClick(position)
        }
    }

}