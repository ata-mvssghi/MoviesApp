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
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.adapters.SearchAdapter
import com.example.moviesapp.databinding.FragmentSearchBinding
import com.example.moviesapp.viewModels.SearchFragmentViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    lateinit var binding : FragmentSearchBinding
    lateinit var viewModel : SearchFragmentViewModel
    lateinit var adapter : SearchAdapter
    var page = 1
    var currentQuery : String? = null


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
                if(newText!=null) {
                    page = 1
                    makeTheCall(newText,page)

                }
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
        var isScrolling = false
        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(isScrolling) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = adapter.itemCount
                    val pastVisibleItemCount =
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    //if user scrolls till second last row then new data will be fetched
                    if (visibleItemCount + pastVisibleItemCount >= totalItemCount-2) {
                        // Load more data
                        currentQuery?.let { viewModel.searchQuery(it,++page) }
                        Log.i("imdb","searching for more results with page  = $page")
                        //binding.loadMoreProgress.visibility = View.VISIBLE
                        isScrolling = false
                    }
                }
            }
        })
    }
    fun makeTheCall(query:String,page : Int){
        currentQuery = query
        viewModel.searchQuery(query,page)
    }
    fun handleEvent(event:String){
        when(event){
            "search result fetched" ->{
                val list = viewModel.result
                val filteredList = list.filter { it.backdrop_path!=null || it.poster_path!=null || it.profile_path!=null  }
               adapter.differ.submitList(filteredList)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
