package com.mertkesgin.discovermovieapp.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import com.mertkesgin.discovermovieapp.utils.Constants.SEARCH_TIME_DELAY
import com.mertkesgin.discovermovieapp.utils.Constants.hideProgress
import com.mertkesgin.discovermovieapp.utils.Constants.showProgress
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var viewModel: SearchViewModel

    private lateinit var searchMovieAdapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupSearchMovie()
        setupSearchedMoviesObserver()
    }

    private fun setupSearchedMoviesObserver() {
        viewModel.movieSearch.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { searchMovieAdapter.differ.submitList(it.movieEntries) }
                    hideProgress(progressBarSearch)
                }
                is Resource.Error -> {
                    response.message?.let { Toast.makeText(requireContext(), it , Toast.LENGTH_SHORT).show() }
                    hideProgress(progressBarSearch)
                }
                is Resource.Loading -> { showProgress(progressBarSearch) }
            }
        })
    }

    private fun setupSearchMovie() {
        var job: Job? = null
        etSearch.addTextChangedListener{ editable ->
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
        rvSearch.apply {
            adapter = searchMovieAdapter
            layoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        }
    }

    private fun setupViewModel() {
        val movieRepository = AppRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = ViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(SearchViewModel::class.java)
    }
}
