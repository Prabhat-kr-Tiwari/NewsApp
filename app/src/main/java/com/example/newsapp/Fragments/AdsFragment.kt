package com.example.newsapp.Fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.Adapter.LatestNewsAdapter
import com.example.newsapp.Adapter.RecomendationAdapter
import com.example.newsapp.Application.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.SignUpActivity
import com.example.newsapp.databinding.FragmentAdsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewModel.NewsViewModel
import com.example.newsapp.viewModelProviderFactory.NewsViewModelProviderFactory.NewsViewModelProviderFactory
import kotlin.concurrent.thread


class AdsFragment : Fragment() {
    private var previousConnectivityStatus: Boolean = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private val connectivityManager by lazy {
        requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    lateinit var binding: FragmentAdsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var latestNewsAdapter: LatestNewsAdapter
    lateinit var recomendationAdapter: RecomendationAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Schedule initial check for internet connectivity
        checkInternetAndSetStatusBarColor()

        // Schedule periodic checks for internet connectivity
        schedulePeriodicInternetCheck()



        latestNewsAdapter = LatestNewsAdapter()
        recomendationAdapter = RecomendationAdapter()
        binding.rvLatestNews.apply {
            adapter = latestNewsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        binding.txtViewAllPopularTags.setOnClickListener {
            binding.popularTagsLl4.visibility = View.VISIBLE
        }
        binding.rvRecomendationTopic.apply {
            adapter = recomendationAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        }


        val newsRepository = NewsRepository()
        val viewModelProviderFactory =
            NewsViewModelProviderFactory(requireActivity().application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        viewModel.businessCategoryNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Success -> {
//                    Config.hideDialog()
                    response.data?.let { newsResponse ->

                        latestNewsAdapter.differ.submitList(newsResponse.articles.toList())
                        recomendationAdapter.differ.submitList(newsResponse.articles.toList())
                    }


                }

                is Resource.Loading -> {
//                    Config.showDialog(requireContext(),"Fetching...")

                }

                is Resource.Error -> {
                    response.message?.let { message ->

                        Toast.makeText(activity, "An error occured  $message", Toast.LENGTH_LONG)
                            .show()


                    }

                }
            }


        })


    }


    override fun onResume() {
        super.onResume()

    }
    private fun checkInternetAndSetStatusBarColor() {
        thread {
            val isConnected = checkInternetConnection()
            mainHandler.post {
                if (isConnected != previousConnectivityStatus) {
                    setStatusBarColor(isConnected)
                    previousConnectivityStatus = isConnected
                }
            }
        }
    }

    private fun schedulePeriodicInternetCheck() {
        val intervalMillis = 5000L // Adjust the interval as needed (e.g., check every 5 seconds)
        mainHandler.postDelayed({
            checkInternetAndSetStatusBarColor()
            schedulePeriodicInternetCheck()
        }, intervalMillis)
    }

    private fun isInternetConnected(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    private fun checkInternetConnection(): Boolean {
        val connectivityManager = requireActivity().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("CHECKPRABHAT", "checkInternetConnection: Here")
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            //these are not deprecated at api level below 23
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    private fun setStatusBarColor(isConnected: Boolean) {
        val window = requireActivity().window
        when {
            isConnected -> {
                // Internet is available, set status bar color to green
                window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.greeny)

                // Schedule changing the status bar color to white after a delay
                mainHandler.postDelayed({
                    window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
                }, 1000L) // Adjust the delay as needed (e.g., change to white after 5 seconds)
            }
            else -> {
                // No internet, set status bar color to red
                window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.red)
            }
        }
    }


}