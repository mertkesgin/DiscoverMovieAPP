package com.mertkesgin.discovermovieapp.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.adapter.PeopleAdapter
import com.mertkesgin.discovermovieapp.adapter.SliderMovieAdapter
import com.mertkesgin.discovermovieapp.base.BaseFragment
import com.mertkesgin.discovermovieapp.data.remote.MovieApi
import com.mertkesgin.discovermovieapp.data.remote.PeopleApi
import com.mertkesgin.discovermovieapp.databinding.FragmentMovieBinding
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.utils.Constants.hideProgress
import com.mertkesgin.discovermovieapp.utils.Constants.showProgress
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : BaseFragment<MovieViewModel,FragmentMovieBinding,MovieRepository>() {

    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var peopleAdapter: PeopleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        subscribeObservers()
    }

    private fun subscribeObservers() {

        viewModel.trendsOfDayMovie.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        val trendList = it.movieEntries.take(5)
                        initSlider(trendList)
                        hideProgress(progressBarMovie)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    hideProgress(progressBarMovie)
                }
                is Resource.Loading ->{ showProgress(binding.progressBarMovie) }
            }
        })

        viewModel.popularMovies.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        popularMoviesAdapter.differ.submitList(it.movieEntries)
                        hideProgress(progressBarMovie)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    hideProgress(progressBarMovie)
                }
                is Resource.Loading ->{ showProgress(binding.progressBarMovie) }
            }
        })

        viewModel.topRated.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        topRatedMoviesAdapter.differ.submitList(it.movieEntries)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    hideProgress(progressBarMovie)
                }
                is Resource.Loading ->{ showProgress(binding.progressBarMovie) }
            }
        })

        viewModel.popularPeople.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        peopleAdapter.differ.submitList(it.peopleEntries)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    hideProgress(progressBarMovie)
                }
                is Resource.Loading ->{ showProgress(binding.progressBarMovie) }
            }
        })
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
                putSerializable("movieEntry",it)
            }
            findNavController().navigate(
                R.id.action_movieFragment_to_movieDetailsFragment,
                bundle
            )
        }

        topRatedMoviesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movieEntry",it)
            }
            findNavController().navigate(
                R.id.action_movieFragment_to_movieDetailsFragment,
                bundle
            )
        }
    }

    private fun initSlider(trendsOfDayEntries: List<MovieEntry>) {
        sliderPagerMovie.adapter = SliderMovieAdapter(trendsOfDayEntries)
        TabLayoutMediator(indicator,sliderPagerMovie) { tab,position -> }.attach()
    }

    override fun getViewModel() = MovieViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieBinding = FragmentMovieBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): MovieRepository {
        val movieApi = retrofitInstance.buildApi(MovieApi::class.java)
        val peopleApi = retrofitInstance.buildApi(PeopleApi::class.java)
        return MovieRepository(movieApi, peopleApi)
    }
}