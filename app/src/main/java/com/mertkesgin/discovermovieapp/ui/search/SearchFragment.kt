package com.mertkesgin.discovermovieapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.base.BaseFragment
import com.mertkesgin.discovermovieapp.data.remote.MovieApi
import com.mertkesgin.discovermovieapp.databinding.FragmentSearchBinding
import com.mertkesgin.discovermovieapp.repository.SearchRepository
import com.mertkesgin.discovermovieapp.utils.Constants.SEARCH_TIME_DELAY
import com.mertkesgin.discovermovieapp.utils.Constants.hideProgress
import com.mertkesgin.discovermovieapp.utils.Constants.showProgress
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment<SearchViewModel,FragmentSearchBinding,SearchRepository>() {

    private lateinit var searchMovieAdapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchMovie()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.movieSearch.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let { searchMovieAdapter.differ.submitList(it.movieEntries) }
                    hideProgress(binding.progressBarSearch)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    hideProgress(binding.progressBarSearch)
                }
                is Resource.Loading -> { showProgress(binding.progressBarSearch) }
            }
        })
    }

    private fun setupSearchMovie() {
        var job: Job? = null
        binding.etSearch.addTextChangedListener{ editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchMovies(editable.toString())
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        searchMovieAdapter = MoviesAdapter()
        binding.rvSearch.apply {
            adapter = searchMovieAdapter
            layoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        }
    }

    override fun getViewModel(): Class<SearchViewModel> = SearchViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): SearchRepository {
        val movieApi = retrofitInstance.buildApi(MovieApi::class.java)
        return SearchRepository(movieApi)
    }
}
