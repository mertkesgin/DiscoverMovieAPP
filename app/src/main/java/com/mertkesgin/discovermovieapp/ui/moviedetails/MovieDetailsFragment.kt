package com.mertkesgin.discovermovieapp.ui.moviedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.CastAdapter
import com.mertkesgin.discovermovieapp.adapter.CompanyAdapter
import com.mertkesgin.discovermovieapp.adapter.GenreAdapter
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.model.MovieDetailsResponse
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.Constants.hideProgress
import com.mertkesgin.discovermovieapp.utils.Constants.showProgress
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private lateinit var viewModel : MovieDetailsViewModel

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
        setupViewModel()
        MainScope().launch { viewModel.gelAllData(movieEntry.movieId) }
        setupRecyclerView()
        setupMovieDetailsObserver()
        setupSimilarMoviesObserver()
        setupCastObserver()
        setupSaveMovie()
        checkIsMovieExistInDb()
    }

    private fun checkIsMovieExistInDb() {
        viewModel.isMovieExist(movieEntry.movieId).observe(viewLifecycleOwner, Observer {
            isExist = it
            when(it){
                true -> {imgSaveMovie.setImageResource(R.drawable.ic_save_fill)}
                else -> {imgSaveMovie.setImageResource(R.drawable.ic_save)}
            }
        })
    }

    private fun setupSaveMovie() {
        imgSaveMovie.setOnClickListener {
            if (!isExist){
                viewModel.insertMovie(movieEntry)
                Toast.makeText(requireContext(), "Movie Saved", Toast.LENGTH_SHORT).show()
            }else viewModel.deleteMovie(movieEntry)
        }
    }

    private fun setupCastObserver() {
        viewModel.cast.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { castAdapter.differ.submitList(it.castEntry) }
                    hideProgress(progressBarMovieDetails)
                }
                is Resource.Error -> {
                    response.message?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
                    hideProgress(progressBarMovieDetails)
                }
                is Resource.Loading -> { showProgress(progressBarMovieDetails) }
            }
        })
    }

    private fun setupSimilarMoviesObserver() {
        viewModel.similarMovies.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        similarMovieAdapter.differ.submitList(it.movieEntries)
                    }
                }
            }
        })
    }


    private fun setupMovieDetailsObserver() {
        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        initViews(it)
                        companyAdapter.differ.submitList(it.production_companies)
                    }
                }
            }
        })
    }

    private fun initViews(movie: MovieDetailsResponse) {
        tv_mDetails_movie_name.text = movie.original_title
        tv_movie_storyline.text = movie.overview
        picassoImageHelper.loadUrl(POSTER_BASE_URL+movie.poster_path,img_movie_details_profile)
        picassoImageHelper.loadUrl(POSTER_BASE_URL+movie.backdrop_path,img_movie_details_background)
        tv_mDetails_vote_average.text = (movie.vote_average.toString()+ "/10")
        tv_mDeatils_date.text = movie.release_date
        tv_movie_runtime.text = convertRunTime(movie.runtime)
        genreAdapter.differ.submitList(movie.genres)
        imgMovieDetailsBack.setOnClickListener { activity?.onBackPressed() }
    }

    private fun convertRunTime(runtime: Int): String {
        val hour = (runtime/60).toString()
        val min = (runtime%60).toString()
        return (hour+"h "+min+"min")
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter()
        rv_genre.apply {
            adapter = genreAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        similarMovieAdapter = MoviesAdapter()
        rv_similar_movies.apply {
            adapter = similarMovieAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        castAdapter = CastAdapter()
        rv_cast.apply {
            adapter = castAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        companyAdapter = CompanyAdapter()
        rv_movie_companies.apply {
            adapter = companyAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun setupViewModel() {
        val movieRepository = AppRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = ViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(MovieDetailsViewModel::class.java)
    }
}