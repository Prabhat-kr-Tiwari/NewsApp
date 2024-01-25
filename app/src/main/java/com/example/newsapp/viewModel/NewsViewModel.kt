package com.example.newsapp.viewModel

import android.app.Application
import android.content.Context


import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.Application.NewsApplication
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.ConnectivityManager.*


class NewsViewModel(application: Application,val newsRepository: NewsRepository):AndroidViewModel(application) {

     val TAG="NewsViewModel"
    //business news category
    val businessCategoryNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var businessCategoryNewsPage=1
    val busnessCategory="business"
    var businessCategoryResponse:NewsResponse?=null
    //entertainment
    val entertainmentCategoryNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var entertainmentCategoryNewsPage=1
    val entertainmentCategory="entertainment"
    var entertainmentCategoryResponse:NewsResponse?=null

    //health
    val healthCategoryNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var healthCategoryNewsPage=1
    val healthCategory="health"
    var healthCategoryResponse:NewsResponse?=null
    //science
    val scienceCategoryNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var scienceCategoryNewsPage=1
    val scienceCategory="science"
    var scienceCategoryResponse:NewsResponse?=null

    //sports
    val sportsCategoryNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var sportseCategoryNewsPage=1
    val sportsCategory="sports"
    var sportsCategoryResponse:NewsResponse?=null
    //technology
    val technologyCategoryNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var technologyCategoryNewsPage=1
    val technologyCategory="technology"
    var technologyCategoryResponse:NewsResponse?=null

    //getting breaking news top headlines
    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsResponse:NewsResponse?=null


    init {
        Log.d(TAG, ": inist")
        getNewsBasedOnBusinessCategory("us")
        getNewsBasedOnEntertainmentCategory("us")
        getNewsBasedOnHealthCategory("us")
        getNewsBasedOnScienceCategory("us")
        getNewsBasedOnSportsCategory("us")
        getNewsBasedOnTechnologyCategory("us")
        getBreakingNes("in")
    }
    //getting breaking news
    fun getBreakingNes(countryCode: String)=viewModelScope.launch {

        safeBreakingNewsCall(countryCode)
    }
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingNewsPage++
                if (breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }else{
                    val oldArticles= breakingNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return  Resource.Success(breakingNewsResponse?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if(checkInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))

            }
        }
    }
    fun getNewsBasedOnBusinessCategory(countryCode: String)=viewModelScope.launch {
        Log.d(TAG, "getNewsBasedOnBusinessCategory: ")
        safeBusinessNewsBasedOnCategoryCall(countryCode)
    }
    private fun handleBusinessCategoryNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                businessCategoryNewsPage++
                if (businessCategoryResponse==null){
                    businessCategoryResponse=resultResponse

                }else{
                    val oldArticles= businessCategoryResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return  Resource.Success(businessCategoryResponse?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun safeBusinessNewsBasedOnCategoryCall(countryCode: String){
        businessCategoryNews.postValue(Resource.Loading())

        try {
            val status=checkInternetConnection()
            Log.d("NEWPRABHAT", "safeBusinessNewsBasedOnCategoryCall: ${status} ")

            if (checkInternetConnection()){
                val response=newsRepository.getNewsBasedOnCategory(countryCode,businessCategoryNewsPage,busnessCategory)
                businessCategoryNews.postValue(handleBusinessCategoryNewsResponse(response))

                Log.d("PRABHAT", "safeBusinessNewsBasedOnCategoryCall: ")
            }else{

                businessCategoryNews.postValue(Resource.Error("No internet Connection"))

            }

        }catch (t:Throwable){
            Log.d(TAG, "safeBusinessNewsBasedOnCategoryCall: Networ failure")
            when(t){
                
                is IOException->businessCategoryNews.postValue(Resource.Error("Network Failure"))
            }
        }
    }



    //entertainment
    fun getNewsBasedOnEntertainmentCategory(countryCode: String)=viewModelScope.launch {

        safeEntertainmentNewsBasedOnCategoryCall(countryCode)
    }
    private fun handleEntertainmentCategoryNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{

        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                entertainmentCategoryNewsPage++
                if (entertainmentCategoryResponse==null){
                    entertainmentCategoryResponse=resultResponse

                }else{
                    val oldArticles= entertainmentCategoryResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return  Resource.Success(entertainmentCategoryResponse?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun safeEntertainmentNewsBasedOnCategoryCall(countryCode: String){
        entertainmentCategoryNews.postValue(Resource.Loading())
        try {
            val status=checkInternetConnection()
            if (checkInternetConnection()){
                val response=newsRepository.getNewsBasedOnCategory(countryCode,entertainmentCategoryNewsPage,entertainmentCategory)
                entertainmentCategoryNews.postValue(handleEntertainmentCategoryNewsResponse(response))
                Log.d("PRABHAT", "safeBusinessNewsBasedOnCategoryCall: ")
            }else{
                entertainmentCategoryNews.postValue(Resource.Error("No internet Connection"))
            }
        }catch (t:Throwable){
            Log.d(TAG, "safeBusinessNewsBasedOnCategoryCall: Networ failure")
            when(t){
                is IOException->entertainmentCategoryNews.postValue(Resource.Error("Network Failure"))
            }
        }
    }
    //health
    fun getNewsBasedOnHealthCategory(countryCode: String)=viewModelScope.launch {
        safeHealthNewsBasedOnCategoryCall(countryCode)
    }
    private fun handleHealthCategoryNewsResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            Log.d(TAG, "handleHealthCategoryNewsResponse: $response")
            response.body()?.let {resultResponse->
                healthCategoryNewsPage++
                if (healthCategoryResponse==null){
                    healthCategoryResponse=resultResponse
                }else{
                    val oldArticles=healthCategoryResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(healthCategoryResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun safeHealthNewsBasedOnCategoryCall(countryCode: String){
        healthCategoryNews.postValue(Resource.Loading())
        try {
            val status=checkInternetConnection()
            if (checkInternetConnection()){
                val response=newsRepository.getNewsBasedOnCategory(countryCode,healthCategoryNewsPage,healthCategory)
                healthCategoryNews.postValue(handleHealthCategoryNewsResponse(response))
                Log.d("PRABHAT", "safeBusinessNewsBasedOnCategoryCall: ")
            }else{
                healthCategoryNews.postValue(Resource.Error("No internet Connection"))
            }
        }catch (t:Throwable){
            Log.d(TAG, "safeBusinessNewsBasedOnCategoryCall: Networ failure")
            when(t){
                is IOException->healthCategoryNews.postValue(Resource.Error("Network Failure"))
            }
        }
    }
    //science
    fun getNewsBasedOnScienceCategory(countryCode: String)=viewModelScope.launch {

        safeScienceNewsBasedOnCategoryCall(countryCode)
    }
    private fun handleScienceCategoryNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{

        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                scienceCategoryNewsPage++
                if (scienceCategoryResponse==null){
                    scienceCategoryResponse=resultResponse

                }else{
                    val oldArticles= scienceCategoryResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return  Resource.Success(scienceCategoryResponse?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun safeScienceNewsBasedOnCategoryCall(countryCode: String){
        scienceCategoryNews.postValue(Resource.Loading())
        try {
            val status=checkInternetConnection()
            if (checkInternetConnection()){
                val response=newsRepository.getNewsBasedOnCategory(countryCode,scienceCategoryNewsPage,scienceCategory)
                scienceCategoryNews.postValue(handleScienceCategoryNewsResponse(response))
                Log.d("PRABHAT", "safeBusinessNewsBasedOnCategoryCall: ")
            }else{
                scienceCategoryNews.postValue(Resource.Error("No internet Connection"))
            }
        }catch (t:Throwable){
            Log.d(TAG, "safeBusinessNewsBasedOnCategoryCall: Networ failure")
            when(t){
                is IOException->scienceCategoryNews.postValue(Resource.Error("Network Failure"))
            }
        }
    }
    //sports
    fun getNewsBasedOnSportsCategory(countryCode: String)=viewModelScope.launch {
        Log.d(TAG, "getNewsBasedOnBusinessCategory: ")
        safeSportsNewsBasedOnCategoryCall(countryCode)
    }
    private fun handleSportsCategoryNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        Log.d("PRABHAT", "handleBusinessCategoryNewsResponse:$response ")
        Log.d(TAG, "handleBusinessCategoryNewsResponse: ${response.message()}")
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                sportseCategoryNewsPage++
                if (sportsCategoryResponse==null){
                    sportsCategoryResponse=resultResponse

                }else{
                    val oldArticles= sportsCategoryResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return  Resource.Success(sportsCategoryResponse?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun safeSportsNewsBasedOnCategoryCall(countryCode: String){
        sportsCategoryNews.postValue(Resource.Loading())

        try {
            val status=checkInternetConnection()
            Log.d("NEWPRABHAT", "safeBusinessNewsBasedOnCategoryCall: ${status} ")

            if (checkInternetConnection()){
                val response=newsRepository.getNewsBasedOnCategory(countryCode,sportseCategoryNewsPage,sportsCategory)
                sportsCategoryNews.postValue(handleSportsCategoryNewsResponse(response))

                Log.d("PRABHAT", "safeBusinessNewsBasedOnCategoryCall: ")
            }else{

                sportsCategoryNews.postValue(Resource.Error("No internet Connection"))

            }

        }catch (t:Throwable){
            Log.d(TAG, "safeBusinessNewsBasedOnCategoryCall: Networ failure")
            when(t){

                is IOException->sportsCategoryNews.postValue(Resource.Error("Network Failure"))
            }
        }
    }

    //technology
    fun getNewsBasedOnTechnologyCategory(countryCode: String)=viewModelScope.launch {
        Log.d(TAG, "getNewsBasedOnBusinessCategory: ")
        safeTechnologyNewsBasedOnCategoryCall(countryCode)
    }
    private fun handleTechnologyCategoryNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{

        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                technologyCategoryNewsPage++
                if (technologyCategoryResponse==null){
                    technologyCategoryResponse=resultResponse

                }else{
                    val oldArticles= technologyCategoryResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return  Resource.Success(technologyCategoryResponse?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun safeTechnologyNewsBasedOnCategoryCall(countryCode: String){
        businessCategoryNews.postValue(Resource.Loading())

        try {
            val status=checkInternetConnection()
            Log.d("NEWPRABHAT", "safeBusinessNewsBasedOnCategoryCall: ${status} ")

            if (checkInternetConnection()){
                val response=newsRepository.getNewsBasedOnCategory(countryCode,technologyCategoryNewsPage,technologyCategory)
                technologyCategoryNews.postValue(handleTechnologyCategoryNewsResponse(response))

                Log.d("PRABHAT", "safeBusinessNewsBasedOnCategoryCall: ")
            }else{

                technologyCategoryNews.postValue(Resource.Error("No internet Connection"))

            }

        }catch (t:Throwable){
            Log.d(TAG, "safeBusinessNewsBasedOnCategoryCall: Networ failure")
            when(t){

                is IOException->technologyCategoryNews.postValue(Resource.Error("Network Failure"))
            }
        }
    }
    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("CHECKPRABHAT", "checkInternetConnection: Here")
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            //these are not deprecated at api level below 23
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}