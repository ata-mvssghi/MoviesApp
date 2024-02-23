package com.example.moviesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.databinding.FragmentMovieHomeBinding

class MovieHomeFragment : Fragment() {
    lateinit var binding : FragmentMovieHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMovieHomeBinding.inflate(inflater)
        binding.button.setOnClickListener {
            val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMoviesFragment(true)
            findNavController().navigate(action)
        }

        binding.button2.setOnClickListener {
            val action = MovieHomeFragmentDirections.actionMovieHomeFragmentToMoviesFragment(false)
            findNavController().navigate(action)
        }
        return binding.root
    }

}