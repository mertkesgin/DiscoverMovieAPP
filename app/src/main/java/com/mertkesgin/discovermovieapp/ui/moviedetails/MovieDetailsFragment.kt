package com.mertkesgin.discovermovieapp.ui.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.CastAdapter
import com.mertkesgin.discovermovieapp.adapter.CompanyAdapter
import com.mertkesgin.discovermovieapp.adapter.GenreAdapter
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.base.BaseFragment
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.data.remote.MovieApi
import com.mertkesgin.discovermovieapp.data.remote.PeopleApi
import com.mertkesgin.discovermovieapp.databinding.FragmentMovieDetailsBinding
import com.mertkesgin.discovermovieapp.model.MovieDetailsResponse
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.repository.MovieDetailsRepository
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.Constants.convertRunTime
import com.mertkesgin.discovermovieapp.utils.Constants.hideProgress
import com.mertkesgin.discovermovieapp.utils.Constants.showProgress
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MovieDetailsFragment : BaseFragment<MovieDetailsViewModel,FragmentMovieDetailsBinding,MovieDetailsRepository>(){

    private val picassoImageHelper = PicassoImageHelper()

    private lateinit var genreAdapter: GenreAdapter
    private lateinit var similarMovieAdapter: MoviesAdapter
    private lateinit var castAdapter: CastAdapter
    private lateinit var companyAdapter: CompanyAdapter

    val args: MovieDetailsFragmentArgs by navArgs()
    lateinit var movieEntry:MovieEntry

    private var isExist = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieEntry = args.movieEntry
        MainScope().launch { viewModel.gelAllData(movieEntry.movieId) }
        setupRecyclerView()
        subscribeObservers()
        setupSaveMovie()
    }

    private fun subscribeObservers() {
        viewModel.isMovieExist(movieEntry.movieId).observe(viewLifecycleOwner, Observer {
            isExist = it
            when(it){
                true -> {binding.imgSaveMovie.setImageResource(R.drawable.ic_save_fill)}
                else -> {binding.imgSaveMovie.setImageResource(R.drawable.ic_save)}
            }
        })

        viewModel.cast.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgress(binding.progressBarMovieDetails)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    hideProgress(binding.progressBarMovieDetails)
                }
                is Resource.Loading -> { showProgress(binding.progressBarMovieDetails) }
            }
        })

        viewModel.similarMovies.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        similarMovieAdapter.differ.submitList(it.movieEntries)
                    }
                }
                is Resource.Error -> { Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show() }
            }
        })

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        initViews(it)
                        companyAdapter.differ.submitList(it.production_companies)
                    }
                }
                is Resource.Error -> { Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show() }
            }
        })
    }

    private fun setupSaveMovie() {
        binding.imgSaveMovie.setOnClickListener {
            if (!isExist){
                viewModel.insertMovie(movieEntry)
                Toast.makeText(requireContext(), "Movie Saved", Toast.LENGTH_SHORT).show()
            }else viewModel.deleteMovie(movieEntry)
        }
    }

    private fun initViews(movie: MovieDetailsResponse) {
        binding.tvMDetailsMovieName.text = movie.original_title
        binding.tvMovieStoryline.text = movie.overview
        picassoImageHelper.loadUrl(POSTER_BASE_URL+movie.poster_path,binding.imgMovieDetailsProfile)
        picassoImageHelper.loadUrl(POSTER_BASE_URL+movie.backdrop_path,binding.imgMovieDetailsBackground)
        binding.tvMDetailsVoteAverage.text = (movie.vote_average.toString()+ "/10")
        binding.tvMDeatilsDate.text = movie.release_date
        binding.tvMovieRuntime.text = convertRunTime(movie.runtime)
        genreAdapter.differ.submitList(movie.genres)
        binding.imgMovieDetailsBack.setOnClickListener { activity?.onBackPressed() }
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter()
        binding.rvGenre.apply {
            adapter = genreAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        similarMovieAdapter = MoviesAdapter()
        binding.rvSimilarMovies.apply {
            adapter = similarMovieAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        castAdapter = CastAdapter()
        binding.rvCast.apply {
            adapter = castAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        companyAdapter = CompanyAdapter()
        binding.rvMovieCompanies.apply {
            adapter = companyAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun getViewModel(): Class<MovieDetailsViewModel> = MovieDetailsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailsBinding = FragmentMovieDetailsBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): MovieDetailsRepository {
        val movieApi = retrofitInstance.buildApi(MovieApi::class.java)
        val peopleApi = retrofitInstance.buildApi(PeopleApi::class.java)
        val database = AppDatabase(requireContext())
        return MovieDetailsRepository(movieApi, peopleApi, database)
    }
}