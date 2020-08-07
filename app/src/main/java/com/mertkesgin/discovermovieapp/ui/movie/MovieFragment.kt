package com.mertkesgin.discovermovieapp.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator

import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.PeopleAdapter
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.adapter.SliderMovieAdapter
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    private lateinit var viewModel : MovieViewModel

    private var trendsList = mutableListOf<MovieEntry>()
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var peopleAdapter: PeopleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerViews()
        setupTrendsOfDayObserver()
        setupPopularMoviesObserver()
        setupTopRatedMoviesObserver()
        setupPeopleObserver()
    }

    private fun setupTrendsOfDayObserver() {
        viewModel.trendsOfDayMovie.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        trendsList.clear()
                        trendsList.add(it.movieEntries[0])
                        trendsList.add(it.movieEntries[1])
                        trendsList.add(it.movieEntries[2])
                        trendsList.add(it.movieEntries[3])
                        trendsList.add(it.movieEntries[4])
                        Toast.makeText(context,it.total_pages.toString(),Toast.LENGTH_LONG).show()
                        initSlider(trendsList)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context,"Başarısız",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun initSlider(trendsOfDayEntries: List<MovieEntry>) {
        sliderPagerMovie.adapter = SliderMovieAdapter(trendsOfDayEntries)
        TabLayoutMediator(indicator,sliderPagerMovie) { tab,position -> }.attach()
    }

    private fun setupPopularMoviesObserver() {
        viewModel.popularMovies.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        popularMoviesAdapter.differ.submitList(it.movieEntries)
                    }
                }
            }
        })
    }

    private fun setupTopRatedMoviesObserver() {
        viewModel.topRated.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        topRatedMoviesAdapter.differ.submitList(it.movieEntries)
                    }
                }
            }
        })
    }

    private fun setupPeopleObserver() {
        viewModel.popularPeople.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        peopleAdapter.differ.submitList(it.peopleEntries)
                    }
                }
            }
        })
    }

    private fun setupViewModel() {
        val movieRepository = MovieRepository()
        val viewModelProviderFactory = ViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(MovieViewModel::class.java)
    }

    private fun setupRecyclerViews() {
        popularMoviesAdapter = MoviesAdapter()
        topRatedMoviesAdapter = MoviesAdapter()
        peopleAdapter = PeopleAdapter()

        rvPopularMovies.apply {
            adapter = popularMoviesAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        rvTopRatedMovie.apply {
            adapter = topRatedMoviesAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        rvPeople.apply {
            adapter = peopleAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        popularMoviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("movieId",it.id)
            }
            findNavController().navigate(
                R.id.action_movieFragment_to_movieDetailsFragment,
                bundle
            )
        }

        topRatedMoviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("movieId",it.id)
            }
            findNavController().navigate(
                R.id.action_movieFragment_to_movieDetailsFragment,
                bundle
            )
        }
    }
}
