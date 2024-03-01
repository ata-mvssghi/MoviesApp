package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.adapters.SearchAdapter
import com.example.moviesapp.databinding.FragmentSearchBinding
import com.example.moviesapp.viewModels.SearchFragmentViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    lateinit var viewModel : SearchFragmentViewModel
    lateinit var adapter : SearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submission
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!=null)
                         makeTheCall(newText)
                return true
            }
        })
        viewModel = ViewModelProvider(this).get(SearchFragmentViewModel::class.java)
        adapter = SearchAdapter(findNavController())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateFlow.collect { value ->
                handleEvent(value)
                Log.i("imdb", "Received: $value")
            }
        }
        val layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.searchRecyclerView.layoutManager = layoutManager
        binding.searchRecyclerView.adapter = adapter
    }
    fun makeTheCall(query:String){
        viewModel.searchQuery(query)
    }
    fun handleEvent(event:String){
        when(event){
            "search result fetched" ->{
               adapter.differ.submitList(viewModel.result)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
