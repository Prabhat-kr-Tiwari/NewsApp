package com.example.newsapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.newsapp.Fragments.AdsFragment
import com.example.newsapp.Fragments.HomeFragment
import com.example.newsapp.Fragments.PollsFragment
import com.example.newsapp.Fragments.ProfileFragment
import com.example.newsapp.Fragments.SettingsFragment
import com.example.newsapp.databinding.ActivityHomeBinding
import com.example.newsapp.ui.AddPostActivity
import com.example.newsapp.ui.DetailNewsActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.background=null
        binding.bottomNavigation.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.mHome->openFragment(HomeFragment())
                R.id.mads->openAdsFragment(AdsFragment())
                R.id.mPoll->openFragment(PollsFragment())
                R.id.mProfile->openFragment(ProfileFragment())
            }
            true
        }
        fragmentManager=supportFragmentManager
        openFragment(HomeFragment())
        binding.addPost.setOnClickListener {
            Toast.makeText(this, "Add post", Toast.LENGTH_SHORT).show()
            val intent= Intent(this,AddPostActivity::class.java)
            startActivity(intent)

        }


    }
    private fun openFragment(fragment: Fragment){
        binding.customToolbar.visibility=View.VISIBLE
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
    }
    private fun openAdsFragment(fragment: Fragment){

        val fragmentTransaction=fragmentManager.beginTransaction()
        binding.customToolbar.visibility=View.GONE
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
    }



    override fun onResume() {
        super.onResume()
        binding.hamberger.setOnClickListener {


            val fragmentTransaction=fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right,R.anim.anim_slide_in_right,R.anim.anim_slide_out_right)
            binding.customToolbar.visibility=View.GONE
            fragmentTransaction.replace(R.id.fragment_container,SettingsFragment())
            fragmentTransaction.commit()
        }
    }
}