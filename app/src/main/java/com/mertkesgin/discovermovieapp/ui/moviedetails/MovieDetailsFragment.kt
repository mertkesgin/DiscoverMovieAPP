package com.mertkesgin.discovermovieapp.ui.moviedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.CastAdapter
import com.mertkesgin.discovermovieapp.adapter.CompanyAdapter
import com.mertkesgin.discovermovieapp.adapter.GenreAdapter
import com.mertkesgin.discovermovieapp.adapter.MoviesAdapter
import com.mertkesgin.discovermovieapp.model.MovieDetailsResponse
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
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

    private val args:MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = args.movieId
        setupViewModel()
        MainScope().launch { viewModel.gelAllData(movieId) }
        setupRecyclerView()
        setupMovieDetailsObserver()
        setupSimilarMoviesObserver()
        setupCastObserver()
    }

    private fun setupCastObserver() {
        viewModel.cast.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        castAdapter.differ.submitList(it.castEntry)
                    }
                }
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
        val movieRepository = MovieRepository()
        val viewModelProviderFactory = ViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(MovieDetailsViewModel::class.java)
    }
}