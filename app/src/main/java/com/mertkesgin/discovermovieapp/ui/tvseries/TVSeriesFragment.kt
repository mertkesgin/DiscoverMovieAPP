package com.mertkesgin.discovermovieapp.ui.tvseries

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
import com.mertkesgin.discovermovieapp.adapter.SliderTVAdapter
import com.mertkesgin.discovermovieapp.adapter.TVAdapter
import com.mertkesgin.discovermovieapp.base.BaseFragment
import com.mertkesgin.discovermovieapp.data.remote.TvSeriesApi
import com.mertkesgin.discovermovieapp.databinding.FragmentTvseriesBinding
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.repository.TvSeriesRepository
import com.mertkesgin.discovermovieapp.utils.Constants.hideProgress
import com.mertkesgin.discovermovieapp.utils.Constants.showProgress
import com.mertkesgin.discovermovieapp.utils.Resource

class TVSeriesFragment : BaseFragment<TVSeriesViewModel,FragmentTvseriesBinding,TvSeriesRepository>() {

    private lateinit var popularAdapter: TVAdapter
    private lateinit var topRatedAdapter: TVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.topRated.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        topRatedAdapter.differ.submitList(response.value.tvSeriesEntries)
                    }
                }
            }
        })

        viewModel.popularTVSeries.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        popularAdapter.differ.submitList(response.value.tvSeriesEntries)
                    }
                }
            }
        })

        viewModel.trendsOfDayTV.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.value.let {
                        val trendList = response.value.tvSeriesEntries.take(5)
                        initSlider(trendList)
                    }
                    hideProgress(binding.progressBarTV)
                }
                is Resource.Error ->{
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    hideProgress(binding.progressBarTV)
                }
                is Resource.Loading -> { showProgress(binding.progressBarTV) }
            }
        })
    }

    private fun initSlider(trendsOfDayEntries: List<TVSeriesEntry>) {
        binding.sliderPagerTV.adapter = SliderTVAdapter(trendsOfDayEntries)
        TabLayoutMediator(binding.indicatorTv,binding.sliderPagerTV) { tab,position -> }.attach()
    }

    private fun setupRecyclerViews() {
        popularAdapter = TVAdapter()
        topRatedAdapter = TVAdapter()

        binding.rvPopularTvSeries.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        }

        popularAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("tvSeriesEntry",it)
            }
            findNavController().navigate(
                R.id.action_tvSeriesFragment_to_TVDetailsFragment,
                bundle
            )
        }

        binding.rvTopRatedTvSeries.apply {
            adapter = topRatedAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        }

        topRatedAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("tvSeriesEntry",it)
            }
            findNavController().navigate(
                R.id.action_tvSeriesFragment_to_TVDetailsFragment,
                bundle
            )
        }
    }

    override fun getViewModel(): Class<TVSeriesViewModel> = TVSeriesViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvseriesBinding = FragmentTvseriesBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): TvSeriesRepository {
        val tvSeriesApi = retrofitInstance.buildApi(TvSeriesApi::class.java)
        return TvSeriesRepository(tvSeriesApi)
    }
}
