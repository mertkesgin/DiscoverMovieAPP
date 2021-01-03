package com.mertkesgin.discovermovieapp.ui.tvdetails

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
import com.mertkesgin.discovermovieapp.adapter.TVAdapter
import com.mertkesgin.discovermovieapp.base.BaseFragment
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.data.remote.PeopleApi
import com.mertkesgin.discovermovieapp.data.remote.TvSeriesApi
import com.mertkesgin.discovermovieapp.databinding.FragmentTvDetailsBinding
import com.mertkesgin.discovermovieapp.model.TVSeriesDetailsResponse
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.repository.TvSeriesDetailsRepository
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.Constants.hideProgress
import com.mertkesgin.discovermovieapp.utils.Constants.showProgress
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TVDetailsFragment : BaseFragment<TVDetailsViewModel,FragmentTvDetailsBinding,TvSeriesDetailsRepository>() {

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
        MainScope().launch { viewModel.getAllData(tvSeriesEntry.tvSeriesId) }
        setupRecyclerView()
        subscribeObservers()
        setupSaveTvSeries()
        checIsTvSeriesExistInDb()
    }

    private fun checIsTvSeriesExistInDb() {
        viewModel.isTvSeriesExist(tvSeriesEntry.tvSeriesId).observe(viewLifecycleOwner, Observer {
            isExist = it
            when(it){
                true -> {binding.imgSaveTvSerie.setImageResource(R.drawable.ic_save_fill)}
                else -> {binding.imgSaveTvSerie.setImageResource(R.drawable.ic_save)}
            }
        })
    }

    private fun setupSaveTvSeries() {
        binding.imgSaveTvSerie.setOnClickListener {
            if (!isExist){
                viewModel.insertTvSeries(tvSeriesEntry)
                Toast.makeText(requireContext(), "Tv Series Saved", Toast.LENGTH_SHORT).show()
            }else viewModel.deleteTvSeries(tvSeriesEntry)
        }
    }

    private fun subscribeObservers() {
        viewModel.tvCast.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let { castAdapter.differ.submitList(it.castEntry) }
                    hideProgress(binding.progressBarTvDetails)
                }
                is Resource.Error -> {

                    hideProgress(binding.progressBarTvDetails)
                }
                is Resource.Loading -> { showProgress(binding.progressBarTvDetails) }
            }
        })

        viewModel.similarTVSeries.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    response.value.let {
                        similarTVAdapter.differ.submitList(it.tvSeriesEntries)
                    }
                }
            }
        })

        viewModel.tvSeriesDetails.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        initViews(it)
                        genreAdapter.differ.submitList(it.genres)
                    }
                }
            }
        })
    }

    private fun initViews(tvSeries: TVSeriesDetailsResponse) {
        binding.tvMDetailsTvName.text = tvSeries.original_name
        binding.tvMDetailsTvVoteAverage.text = (tvSeries.vote_average.toString() + "/10")
        binding.tvSeason.text = ("Sezonlar(${tvSeries.seasons.size})")
        binding.tvTvStoryline.text = tvSeries.overview
        binding.tvStatus.text = tvSeries.status
        binding.tvTvFirstlastDate.text = ("${tvSeries.first_air_date} / ${tvSeries.last_air_date}")
        binding.tvTvRuntime.text = (tvSeries.episode_run_time[0].toString() + " per episode")
        picassoImageHelper.loadUrl(POSTER_BASE_URL+tvSeries.poster_path,binding.imgTvDetailsProfile)
        picassoImageHelper.loadUrl(POSTER_BASE_URL+tvSeries.backdrop_path,binding.imgTvDetailsBackground)
        companyAdapter.differ.submitList(tvSeries.production_companies)
        binding.imgTvDetailsBack.setOnClickListener { activity?.onBackPressed() }
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter()
        binding.rvTvGenre.apply {
            adapter = genreAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        similarTVAdapter = TVAdapter()
        binding.rvTvSimilar.apply {
            adapter = similarTVAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        castAdapter = CastAdapter()
        binding.rvTvCast.apply {
            adapter = castAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }

        companyAdapter = CompanyAdapter()
        binding.rvTvCompany.apply {
            adapter = companyAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }
    }

    override fun getViewModel(): Class<TVDetailsViewModel> = TVDetailsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvDetailsBinding = FragmentTvDetailsBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): TvSeriesDetailsRepository {
        val tvSeriesApi = retrofitInstance.buildApi(TvSeriesApi::class.java)
        val peopleApi = retrofitInstance.buildApi(PeopleApi::class.java)
        val database = AppDatabase(requireContext())
        return TvSeriesDetailsRepository(tvSeriesApi, peopleApi, database)
    }
}