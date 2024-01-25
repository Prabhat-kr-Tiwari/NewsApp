package com.example.newsapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.R
import com.example.newsapp.ViewPagerAdapter.HomeViewPagerAdapter
import com.example.newsapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout


class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentHomeBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tablayout=binding.tabLayout
        val viewpager=binding.viewpager
        val viewpagerAdapter=HomeViewPagerAdapter(this)
        viewpager.adapter=viewpagerAdapter
        tablayout.let {
            viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tablayout.getTabAt(position)?.select()
                }
            })

            tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {

                        viewpager.currentItem = it.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                   tab?.let {
                       viewpager.currentItem=it.position
                   }
                }

            })
            viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tablayout.getTabAt(position)?.select()
                }
            })

        }

    }


}