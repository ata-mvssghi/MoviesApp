package com.example.moviesapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.moviesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() ,BottomNavigatorCallback {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navigationView = binding.bottomNavigator
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navigationView.setupWithNavController(navHostFragment.navController)
    }

    override fun showBottomNavigator(show: Boolean) {
        // Implement the logic to show or hide the bottom navigator
        // This might involve toggling visibility or managing visibility state
        if (show) {
            binding.bottomNavigator.visibility = View.VISIBLE
        } else {
            binding.bottomNavigator.visibility = View.GONE
        }
    }

}