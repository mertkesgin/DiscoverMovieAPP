package com.mertkesgin.discovermovieapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.base.BaseFragment
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.databinding.FragmentMovieListBinding
import com.mertkesgin.discovermovieapp.repository.ListRepository
import kotlinx.android.synthetic.main.fragment_movie_list.*

class MovieListFragment : BaseFragment<ListViewModel,FragmentMovieListBinding,ListRepository>() {

    lateinit var movieAdapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupMovieListObserver()
    }

    private fun setupMovieListObserver() {
        viewModel.movieList.observe(viewLifecycleOwner, Observer {
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

    override fun getViewModel(): Class<ListViewModel> = ListViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieListBinding = FragmentMovieListBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): ListRepository {
        val database = AppDatabase(requireContext())
        return ListRepository(database)
    }
}