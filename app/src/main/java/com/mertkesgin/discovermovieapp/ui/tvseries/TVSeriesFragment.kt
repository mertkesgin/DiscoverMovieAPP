package com.mertkesgin.discovermovieapp.ui.tvseries

import android.os.Bundle
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
import com.mertkesgin.discovermovieapp.adapter.*
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_tvseries.*

/**
 * A simple [Fragment] subclass.
 */
class TVSeriesFragment : Fragment() {

    private lateinit var viewModel: TVSeriesViewModel

    private lateinit var popularAdapter: TVAdapter
    private lateinit var topRatedAdapter: TVAdapter
    private var trendsList = mutableListOf<TVSeriesEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvseries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerViews()
        setupTrendsOfDay()
        setupPopularTVSeriesObserver()
        setupTopRatedObserver()
    }

    private fun setupTopRatedObserver() {
        viewModel.topRated.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        topRatedAdapter.differ.submitList(it.tvSeriesEntries)
                    }
                }
            }
        })
    }

    private fun setupPopularTVSeriesObserver() {
        viewModel.popularTVSeries.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        popularAdapter.differ.submitList(it.tvSeriesEntries)
                    }
                }
            }
        })
    }

    private fun setupTrendsOfDay() {
        viewModel.trendsOfDayTV.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        trendsList.clear()
                        trendsList.add(it.tvSeriesEntries[0])
                        trendsList.add(it.tvSeriesEntries[1])
                        trendsList.add(it.tvSeriesEntries[2])
                        trendsList.add(it.tvSeriesEntries[3])
                        trendsList.add(it.tvSeriesEntries[4])
                        Toast.makeText(context,it.total_pages.toString(), Toast.LENGTH_LONG).show()
                        initSlider(trendsList)
                    }
                }
            }
        })
    }

    private fun initSlider(trendsOfDayEntries: MutableList<TVSeriesEntry>) {
        sliderPagerTV.adapter = SliderTVAdapter(trendsOfDayEntries)
        TabLayoutMediator(indicatorTv,sliderPagerTV) { tab,position -> }.attach()
    }

    private fun setupViewModel() {
        val movieRepository = MovieRepository()
        val viewModelProviderFactory = ViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(TVSeriesViewModel::class.java)
    }

    private fun setupRecyclerViews() {
        popularAdapter = TVAdapter()
        topRatedAdapter = TVAdapter()

        rvPopularTvSeries.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        }

        popularAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("tv_id",it.id)
            }
            findNavController().navigate(
                R.id.action_tvSeriesFragment_to_TVDetailsFragment,
                bundle
            )
        }

        rvTopRatedTvSeries.apply {
            adapter = topRatedAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        }

        topRatedAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putInt("tv_id",it.id)
            }
            findNavController().navigate(
                R.id.action_tvSeriesFragment_to_TVDetailsFragment,
                bundle
            )
        }
    }
}
