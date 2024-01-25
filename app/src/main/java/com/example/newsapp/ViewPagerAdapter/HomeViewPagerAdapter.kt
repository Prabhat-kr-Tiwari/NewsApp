package com.example.newsapp.ViewPagerAdapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.HomesFragment.BusinessFragment
import com.example.newsapp.HomesFragment.EntertainmentFragment
import com.example.newsapp.HomesFragment.HealthFragment
import com.example.newsapp.HomesFragment.ScienceFragment
import com.example.newsapp.HomesFragment.SportsFragment
import com.example.newsapp.HomesFragment.TechnologyFragment

class HomeViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->BusinessFragment()
            1->EntertainmentFragment()
            2->HealthFragment()
            3->ScienceFragment()
            4->SportsFragment()
            5->TechnologyFragment()
            else->BusinessFragment()

        }
    }
}