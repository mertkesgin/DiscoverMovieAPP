package com.mertkesgin.discovermovieapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.TVAdapter
import com.mertkesgin.discovermovieapp.base.BaseFragment
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.databinding.FragmentTvListBinding
import com.mertkesgin.discovermovieapp.repository.ListRepository
import kotlinx.android.synthetic.main.fragment_tv_list.*

class TvListFragment : BaseFragment<ListViewModel,FragmentTvListBinding,ListRepository>() {

    private lateinit var tvListAdapter: TVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupTvListObserver()
    }

    private fun setupTvListObserver() {
        viewModel.tvSeriesList.observe(viewLifecycleOwner, Observer {
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

    override fun getViewModel(): Class<ListViewModel> = ListViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvListBinding = FragmentTvListBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): ListRepository {
        val database = AppDatabase(requireContext())
        return ListRepository(database)
    }
}