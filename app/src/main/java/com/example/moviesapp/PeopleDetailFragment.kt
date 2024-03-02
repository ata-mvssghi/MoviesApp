package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviesapp.adapters.HorizontalAdapter
import com.example.moviesapp.adapters.PhotosAdapter
import com.example.moviesapp.api_responses.people.PeopleResponse
import com.example.moviesapp.databinding.FragmentPeopleDetailBinding
import com.example.moviesapp.model.Movie
import com.example.moviesapp.viewModels.PersonDetailViewModel
import com.example.moviesapp.viewModels.PhotosShardViewModel
import com.ms.square.android.expandabletextview.ExpandableTextView
import gen._base._base_java__assetres.srcjar.R.id.text
import io.github.glailton.expandabletextview.EXPAND_TYPE_LAYOUT
import io.github.glailton.expandabletextview.EXPAND_TYPE_POPUP
import kotlinx.coroutines.launch

class PeopleDetailFragment : Fragment() , OnItemClickerListener {
    lateinit var binding : FragmentPeopleDetailBinding
    lateinit var viewModel : PersonDetailViewModel
    lateinit var creditAdadpter : HorizontalAdapter
    lateinit var photosAdapter : PhotosAdapter
    private val photoViewModel : PhotosShardViewModel by activityViewModels()
     var personId : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPeopleDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(PersonDetailViewModel::class.java)
        personId = arguments?.getInt("person_id")!!
        //TODO here we shall have a problem  is the credit of the actor is a serial
        creditAdadpter = HorizontalAdapter(this,true)
        // Inflate the layout for this fragment
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
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.creditRecycler.layoutManager = layoutManager
        binding.creditRecycler.adapter = creditAdadpter
        photosAdapter = PhotosAdapter(this)
        binding.profileRecycler.adapter = photosAdapter
        val layoutManager2 = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL,false)
        binding.profileRecycler.layoutManager = layoutManager2
        getTheData()

    }

    fun handleEvent(event:String){
        when(event){
            "person detail info fetched" ->{
                setUpInfo(viewModel.personDetail!!)
            }
            "credit fetched"->{
                creditFetchedToDo()
            }
            "profiles fetched"->{
                profileFethedToDo()
            }
        }
    }
    fun setUpInfo(personDetail :PeopleResponse){
        binding.namePersonDetail.text = personDetail.name
        binding.career.text = personDetail.known_for_department
        val tmdbBaseUrl = "https://image.tmdb.org/t/p/"
        val imageSize = "original"
        val backdropPath = personDetail.profile_path
        val imageUrl = "$tmdbBaseUrl$imageSize$backdropPath"
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(binding.imageView2)
        binding.expandTv.text = personDetail.biography
            binding.birthday.text = personDetail.birthday
            binding.birthday.visibility = View.VISIBLE
            binding.bornText.visibility = View.VISIBLE
        if(personDetail.deathday== null){
            binding.diedText.visibility = View.GONE
            binding.deathDate.visibility = View.GONE
        }
        else{
            binding.diedText.visibility = View.VISIBLE
            binding.deathDate.visibility = View.VISIBLE
            binding.deathDate.text = personDetail.deathday.toString()
        }

            binding.expandTv
                .setAnimationDuration(500)
                .setReadMoreText("View More")
                .setReadLessText("View Less")
                .setCollapsedLines(4)
                .setIsExpanded(false)
                .setIsUnderlined(true)
                .setExpandType(EXPAND_TYPE_POPUP).setEllipsizedTextColor(ContextCompat.getColor(requireContext(),
                    androidx.appcompat.R.color.material_blue_grey_800))
            binding.expandTv.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

    }

    override fun onPhotoCLickListener(position: Int) {
        photoViewModel.chosenMoviePhotoPosition = position
        val action = PeopleDetailFragmentDirections.actionPeopleDetailFragmentToFullScreenFragement("profile")
        findNavController().navigate(action)
    }


    override fun onItemClick(movie: Movie, isMovie: Boolean) {
        val action = PeopleDetailFragmentDirections.actionPeopleDetailFragmentToMovieDetailFragment(movie,isMovie)
        findNavController().navigate(action)
    }
    fun getTheData(){
        if(viewModel.personDetail == null)
            viewModel.getPersonDetail(personId)
        else
            setUpInfo(viewModel.personDetail!!)
        if(viewModel.profilePics == null)
            viewModel.getPersonProfile(personId)
        else
            profileFethedToDo()
        if(viewModel.creditList == null)
            viewModel.getCreditList(personId)
        else
            creditFetchedToDo()
    }
    fun profileFethedToDo(){
        Log.i("imdb","profile list size is ${viewModel.profilePics?.size}")
        val photos  = viewModel.profilePics
        photosAdapter.differ.submitList(photos)
        photosAdapter.notifyDataSetChanged()
        photoViewModel.profilePictures = photos!!
        binding.profileProgress.visibility = View.GONE
    }
    fun creditFetchedToDo(){
        val originalList = viewModel.creditList
        val sortedList = originalList?.sortedBy { it.popularity }
        creditAdadpter.differ.submitList(sortedList?.reversed())
        creditAdadpter.notifyDataSetChanged()
        binding.creditProgress.visibility = View.GONE
    }
}