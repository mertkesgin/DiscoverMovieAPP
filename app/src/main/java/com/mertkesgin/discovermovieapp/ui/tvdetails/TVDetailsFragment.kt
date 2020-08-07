package com.mertkesgin.discovermovieapp.ui.tvdetails

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
import com.mertkesgin.discovermovieapp.adapter.TVAdapter
import com.mertkesgin.discovermovieapp.model.TVSeriesDetailsResponse
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_tv_details.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TVDetailsFragment : Fragment(R.layout.fragment_tv_details) {

    private lateinit var viewModel : TVDetailsViewModel

    private val picassoImageHelper = PicassoImageHelper()

    private lateinit var genreAdapter: GenreAdapter
    private lateinit var companyAdapter: CompanyAdapter
    private lateinit var similarTVAdapter: TVAdapter
    private lateinit var castAdapter: CastAdapter

    private val args:TVDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvId = args.tvİd
        setupViewModel()
        MainScope().launch { viewModel.getAllData(tvId) }
        setupRecyclerView()
        setupTVDetailsObserver()
        setupSimilarTVSeriesObserver()
        setupCastObserver()
    }

    private fun setupCastObserver() {
        viewModel.tvCast.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        castAdapter.differ.submitList(it.castEntry)
                    }
                }
            }
        })
    }

    private fun setupSimilarTVSeriesObserver() {
        viewModel.similarTVSeries.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    response.data?.let {
                        similarTVAdapter.differ.submitList(it.tvSeriesEntries)
                    }
                }
            }
        })
    }

    private fun setupTVDetailsObserver() {
        viewModel.tvSeriesDetails.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        initViews(it)
                        genreAdapter.differ.submitList(it.genres)
                    }
                }
            }
        })
    }

    private fun initViews(tvSerie: TVSeriesDetailsResponse) {
        tv_mDetails_tv_name.text = tvSerie.original_name
        tv_mDetails_tv_vote_average.text = (tvSerie.vote_average.toString() + "/10")
        tv_season.text = ("Sezonlar(${tvSerie.seasons.size})")
        tv_tv_storyline.text = tvSerie.overview
        if (tvSerie.status.equals("Returning Series")){
            tv_status.text = "Devam Ediyor"
        }else{tv_status.text = "Bitti"}
        tv_tv_firstlast_date.text = ("${tvSerie.first_air_date} / ${tvSerie.last_air_date}")
        tv_tv_runtime.text = (tvSerie.episode_run_time[0].toString() + " dk her bölüm")
        picassoImageHelper.loadUrl(POSTER_BASE_URL+tvSerie.poster_path,img_tv_details_profile)
        picassoImageHelper.loadUrl(POSTER_BASE_URL+tvSerie.backdrop_path,img_tv_details_background)
        companyAdapter.differ.submitList(tvSerie.production_companies)
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter()
        rv_tv_genre.apply {
            adapter = genreAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        similarTVAdapter = TVAdapter()
        rv_tv_similar.apply {
            adapter = similarTVAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        castAdapter = CastAdapter()
        rv_tv_cast.apply {
            adapter = castAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        companyAdapter = CompanyAdapter()
        rv_tv_company.apply {
            adapter = companyAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupViewModel() {
        val movieRepository = MovieRepository()
        val viewModelProviderFactory = ViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(TVDetailsViewModel::class.java)
    }
}