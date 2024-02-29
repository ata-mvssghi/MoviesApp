package com.example.moviesapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.moviesapp.adapters.FullScreenPhotoAdapter
import com.example.moviesapp.adapters.OnPhotoItemClickListener
import com.example.moviesapp.databinding.FragmentFullScreenFragementBinding
import com.example.moviesapp.model.PhotoDataClass
import com.example.moviesapp.viewModels.PhotosShardViewModel
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class FullScreenFragement() : Fragment() , OnPhotoItemClickListener {
    lateinit var binding : FragmentFullScreenFragementBinding
    lateinit var adapter  : FullScreenPhotoAdapter
    private val photoViewModel : PhotosShardViewModel by activityViewModels()
    lateinit var photoList : List<PhotoDataClass>
    var postionToBeDownloaded : Int = 0
    lateinit var type :String
    var currentITem : Int = 0
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
        adapter = FullScreenPhotoAdapter(this)
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

    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 123

// ...
    // Check and request permission
    fun checkAndRequestPermission() {
        if (EasyPermissions.hasPermissions(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Permission already granted, proceed with saving the image
            saveImageToGallery()
        } else {
            // Request permission
            EasyPermissions.requestPermissions(
                this,
                "Please grant the WRITE_EXTERNAL_STORAGE permission to save the image.",
                WRITE_EXTERNAL_STORAGE_REQUEST_CODE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }
    private fun saveImageToGallery() {
        val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
        val imageSize : String = "original"
        val imageUrl = "$tmdbBaseUrl$imageSize${adapter.differ.currentList[photoViewModel.chosenMoviePhotoPosition].file_path}"

        Glide.with(requireContext())
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        saveBitmapToGalleryQ(resource)
                    } else {
                        saveBitmapToGalleryLegacy(resource)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    //  we do nothing here
                }
            })
    }
    private fun saveBitmapToGalleryQ(bitmap: Bitmap) {
        Log.i("imdb","save image Q called")
        // Use MediaStore API for Android 10 and higher
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "movie_app${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val contentResolver = requireContext().contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            Toast.makeText(
                requireContext(),
                "Image saved to gallery",
                Toast.LENGTH_SHORT
            ).show()
        } ?: run {
            // Handle the case where the insertion failed
            Toast.makeText(
                requireContext(),
                "Failed to save image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveBitmapToGalleryLegacy(bitmap: Bitmap) {
        Log.i("imdb","save image legacy called")
        // Use legacy method for versions below Android 10
        val fileName = "movie_app${System.currentTimeMillis()}.jpg"
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        val file = File(directory, fileName)

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

            MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                file.absolutePath,
                file.name,
                null
            )

            Toast.makeText(
                requireContext(),
                "Image saved to gallery",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Failed to save image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onItemClick(position: Int) {
        checkAndRequestPermission()
        postionToBeDownloaded = position
    }

}