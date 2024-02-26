package com.example.moviesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.moviesapp.adapters.FullScreenPhotoAdapter
import com.example.moviesapp.databinding.FragmentFullScreenFragementBinding
import com.example.moviesapp.model.PhotoDataClass
import com.example.moviesapp.viewModels.PhotosShardViewModel

class FullScreenFragement() : Fragment() {
    lateinit var binding : FragmentFullScreenFragementBinding
    lateinit var adapter  : FullScreenPhotoAdapter
    private val photoViewModel : PhotosShardViewModel by activityViewModels()
    lateinit var photoList : List<PhotoDataClass>
    lateinit var type :String
    var currentITem : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFullScreenFragementBinding.inflate(inflater)
        type = arguments?.getString("type")!!
        if(type.equals("moviePhoto"))
             photoList = photoViewModel.photoList
        else
            photoList = photoViewModel.profilePictures
        val viewPager = binding.fullScreenViewPager
        adapter = FullScreenPhotoAdapter()
        adapter.differ.submitList(photoList)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                     photoViewModel.chosenMoviePhotoPosition = position
            }
        })
         val notthing = arguments?.let { it.getInt("position") }!!
        viewPager.setCurrentItem(photoViewModel.chosenMoviePhotoPosition)
        return binding.root
    }

}