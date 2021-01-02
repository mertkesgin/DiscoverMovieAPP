package com.mertkesgin.discovermovieapp.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list,container,false)
        setupViewPagerWithTabLayout(view)
        return view
    }

    private fun setupViewPagerWithTabLayout(view: View) {
        val fragmentList = arrayListOf<Fragment>(
            MovieListFragment(),
            TvListFragment()
        )
        val adapter =
            ViewPagerAdapter(
                fragmentList,
                requireActivity().supportFragmentManager,
                lifecycle
            )
        view.viewPager.adapter = adapter

        TabLayoutMediator(view.tabLayout,view.viewPager){tab, position ->
            when(position){
                0 -> {tab.text = "Movies"}
                1 -> {tab.text = "Tv Series"}
                else -> {/*DoNothing*/}
            }
        }.attach()
    }

}
