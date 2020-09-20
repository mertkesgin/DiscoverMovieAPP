package com.mertkesgin.discovermovieapp.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.TVAdapter
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_tv_list.*

class TvListFragment : Fragment(R.layout.fragment_tv_list) {

    private lateinit var tvListViewModel:ListViewModel

    private lateinit var tvListAdapter: TVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupTvListObserver()
    }

    private fun setupTvListObserver() {
        tvListViewModel.tvList.observe(viewLifecycleOwner, Observer {
            it?.let { tvListAdapter.differ.submitList(it) }
        })
    }

    private fun setupRecyclerView() {
        tvListAdapter = TVAdapter()
        rvTvList.apply {
            adapter = tvListAdapter
            layoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        }
        tvListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply { putSerializable("tvSeriesEntry",it) }
            findNavController().navigate(R.id.action_listFragment_to_TVDetailsFragment,bundle)
        }
    }

    private fun setupViewModel() {
        val appRepository = AppRepository(AppDatabase(requireContext()))
        val viewModelProviderFactory = ViewModelProviderFactory(appRepository)
        tvListViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ListViewModel::class.java)
    }
}