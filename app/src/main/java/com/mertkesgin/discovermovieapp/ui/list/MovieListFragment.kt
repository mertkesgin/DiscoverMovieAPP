package com.mertkesgin.discovermovieapp.ui.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_movie_list.*

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private lateinit var movieListViewModel: ListViewModel
    lateinit var movieAdapter: MoviesAdapter
    private lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupMovieListObserver()
    }

    private fun setupMovieListObserver() {
        movieListViewModel.movieList.observe(viewLifecycleOwner, Observer {
            it?.let { movieAdapter.differ.submitList(it) }
        })
    }

    private fun setupRecyclerView() {
        movieAdapter = MoviesAdapter()
        rvMovieList.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        }
        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply { putSerializable("movieEntry",it) }
            findNavController().navigate(R.id.action_listFragment_to_movieDetailsFragment,bundle)
        }
    }

    private fun setupViewModel() {
        val appRepository = AppRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = ViewModelProviderFactory(appRepository)
        movieListViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ListViewModel::class.java)
    }
}