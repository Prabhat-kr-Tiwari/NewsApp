package com.example.newsapp.HomesFragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.Adapter.CarousalViewPagerAdapter
import com.example.newsapp.Adapter.NewsAdapter
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentBusinessBinding
import com.example.newsapp.model.ImageItem
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.DetailNewsActivity
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewModel.NewsViewModel
import java.util.UUID

import com.example.newsapp.viewModelProviderFactory.NewsViewModelProviderFactory.NewsViewModelProviderFactory


class BusinessFragment : Fragment() {
    lateinit var binding: FragmentBusinessBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "BusinessFragment"

    lateinit var viewPager2: ViewPager2
    lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback

    private val params=LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8,0,8,0)
    }

    private var sliderHandler: Handler = Handler()
    var breakingNewsItemList= mutableListOf<ImageItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBusinessBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2=binding.viewpager2





        val newsRepository = NewsRepository()
        val viewModelProviderFactory =
            NewsViewModelProviderFactory(requireActivity().application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        //getting the breaking news
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    response.data?.let {
                        Log.d("BREAKINGNEWS", "onViewCreated: ${response.data}")
                        val articles=response.data.articles
                         breakingNewsItemList= mutableListOf<ImageItem>()
                        for (i in 0 until minOf(6, articles.size)) {
                            val article = articles[i]
                            val imageItem = ImageItem(
                                id = i.toString(), // You can use a unique identifier here
                                url = article.urlToImage ?: "", // Provide a default value if urlToImage is null
                                newsHeadline = article.title ?: "" // Provide a default value if title is null
                            )
                            breakingNewsItemList.add(imageItem)
                        }
                        Log.d("BREAKINGNEWSA", "onViewCreated: $breakingNewsItemList")

                        val slideDotsLL=binding.slideDotsll



                        val imageAdapter = CarousalViewPagerAdapter()
                        binding.viewpager2.adapter = imageAdapter
                        imageAdapter.submitList(breakingNewsItemList)

                        val dotsImage=Array(breakingNewsItemList.size){ImageView(requireContext())}
                        Log.d("PRABHATDOTSIMAGE", "onViewCreated: ${dotsImage[0]}")
                        dotsImage.forEach {
                            Log.d("PRABHATDOTSIMAGE", "onViewCreated: dots")
                            it.setImageResource(R.drawable.non_active_dots)
                            slideDotsLL.addView(it,params)
                        }
                        //default first dot
                        dotsImage[0].setImageResource(R.drawable.active_dots)
                        pageChangeListener=object :ViewPager2.OnPageChangeCallback(){

                            override fun onPageSelected(position: Int) {
                                Log.d(TAG, "onPageSelected: $position")

                                sliderHandler.removeCallbacks(sliderRunnable)
                                sliderHandler.postDelayed(sliderRunnable,2000)
                                dotsImage.mapIndexed { index, imageView ->

                                    if (position==index){
                                        imageView.setImageResource(R.drawable.active_dots)
                                    }else{
                                        imageView.setImageResource(R.drawable.non_active_dots)
                                    }
                                    if (position>=5){
                                        viewPager2.setCurrentItem(0,false)
                                    }


                                }
                                super.onPageSelected(position)
                            }



                        }
                        viewPager2.registerOnPageChangeCallback(pageChangeListener)



                    }

                }
                is Resource.Error->{
                    response.message?.let {
                        Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                    }

                }
                is Resource.Loading->{
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })



        setUpRecyclerView()
        //detailactivity open
        newsAdapter.setOnItemClickListener {
            val intent= Intent(requireContext(),DetailNewsActivity::class.java)
            val bundle=Bundle()
            bundle.putSerializable("key",it)
            intent.putExtras(bundle)
            requireContext().startActivity(intent)
        }




        //
        viewModel.businessCategoryNews.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: ${response.data}")
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.businessCategoryNewsPage == totalPages
                        if (isLastPage) {
                            binding.rvNews.setPadding(0, 0, 0, 0)
                        }

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d("PRABHATTW", "onViewCreated: An error occured $message")
                        Toast.makeText(activity, "An error occured  $message", Toast.LENGTH_LONG)
                            .show()


                    }
                }

                is Resource.Loading -> {

                    showProgressBar()
                }

            }

        })

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false

    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true

    }

    //pagination
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getNewsBasedOnBusinessCategory("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    //pagination end
    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BusinessFragment.scrollListener)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        /*viewPager2.unregisterOnPageChangeCallback(pageChangeListener)*/
        if (::pageChangeListener.isInitialized) {
            viewPager2.unregisterOnPageChangeCallback(pageChangeListener)
        }
    }
    private val sliderRunnable: Runnable = Runnable {
        // Your code to be executed when the Runnable runs

        viewPager2.setCurrentItem(viewPager2.currentItem + 1)
    }
    @Override
    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)


    }

    @Override
    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }


}