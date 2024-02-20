package com.example.moviesapp

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.moviesapp.databinding.FragmentMovieDetailBinding
import com.google.android.exoplayer2.ExoPlayer


class MovieDetailFragment : Fragment() {
    lateinit var binding : FragmentMovieDetailBinding
    lateinit var webView : WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater)
        // Inflate the layout for this fragment
        webView = binding.webView

        // Enable JavaScript
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Load YouTube video URL
        val videoId = "PLl99DlL6b4" // Replace with the YouTube video ID
        val youtubeUrl = "https://www.youtube.com/embed/$videoId"
        webView.loadData("<iframe width=\"100%\" height=\"100%\" src=\"$youtubeUrl\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8")

        // Set a WebChromeClient to handle video playback
        webView.webChromeClient = WebChromeClient()

        return binding.root
    }
}