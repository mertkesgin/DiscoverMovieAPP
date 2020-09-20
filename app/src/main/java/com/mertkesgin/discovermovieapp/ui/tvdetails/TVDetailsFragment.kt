package com.mertkesgin.discovermovieapp.ui.tvdetails

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
import com.mertkesgin.discovermovieapp.adapter.TVAdapter
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.model.TVSeriesDetailsResponse
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.Constants.hideProgress
import com.mertkesgin.discovermovieapp.utils.Constants.showProgress
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

    val args: TVDetailsFragmentArgs by navArgs()
    private lateinit var tvSeriesEntry: TVSeriesEntry

    private var isExist = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSeriesEntry = args.tvSeriesEntry
        setupViewModel()
        MainScope().launch { viewModel.getAllData(tvSeriesEntry.tvSeriesId) }
        setupRecyclerView()
        setupTVDetailsObserver()
        setupSimilarTVSeriesObserver()
        setupCastObserver()
        setupSaveTvSeries()
        checIsTvSeriesExistInDb()
    }

    private fun checIsTvSeriesExistInDb() {
        viewModel.isTvSeriesExist(tvSeriesEntry.tvSeriesId).observe(viewLifecycleOwner, Observer {
            isExist = it
            when(it){
                true -> {imgSaveTvSerie.setImageResource(R.drawable.ic_save_fill)}
                else -> {imgSaveTvSerie.setImageResource(R.drawable.ic_save)}
            }
        })
    }

    private fun setupSaveTvSeries() {
        imgSaveTvSerie.setOnClickListener {
            if (!isExist){
                viewModel.insertTvSerie(tvSeriesEntry)
                Toast.makeText(requireContext(), "Tv Series Saved", Toast.LENGTH_SHORT).show()
            }else viewModel.deleteTvSeries(tvSeriesEntry)
        }
    }

    private fun setupCastObserver() {
        viewModel.tvCast.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { castAdapter.differ.submitList(it.castEntry) }
                    hideProgress(progressBarTvDetails)
                }
                is Resource.Error -> {
                    response.message?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
                    hideProgress(progressBarTvDetails)
                }
                is Resource.Loading -> { showProgress(progressBarTvDetails) }
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

    private fun initViews(tvSeries: TVSeriesDetailsResponse) {
        tv_mDetails_tv_name.text = tvSeries.original_name
        tv_mDetails_tv_vote_average.text = (tvSeries.vote_average.toString() + "/10")
        tv_season.text = ("Sezonlar(${tvSeries.seasons.size})")
        tv_tv_storyline.text = tvSeries.overview
        tv_status.text = tvSeries.status
        tv_tv_firstlast_date.text = ("${tvSeries.first_air_date} / ${tvSeries.last_air_date}")
        tv_tv_runtime.text = (tvSeries.episode_run_time[0].toString() + " per episode")
        picassoImageHelper.loadUrl(POSTER_BASE_URL+tvSeries.poster_path,img_tv_details_profile)
        picassoImageHelper.loadUrl(POSTER_BASE_URL+tvSeries.backdrop_path,img_tv_details_background)
        companyAdapter.differ.submitList(tvSeries.production_companies)
        imgTvDetailsBack.setOnClickListener { activity?.onBackPressed() }
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
        val movieRepository = AppRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = ViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(TVDetailsViewModel::class.java)
    }
}