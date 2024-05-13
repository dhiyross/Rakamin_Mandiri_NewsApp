package com.example.rakamin_mandiri_newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rakamin_mandiri_newsapp.models.Article
import com.example.rakamin_mandiri_newsapp.models.NewsResponse
import com.example.rakamin_mandiri_newsapp.repository.NewsRepository
import com.example.rakamin_mandiri_newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query
import java.io.IOException

class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app) {

    val everything: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var everythingSection = 1
    var everythingResponse: NewsResponse? = null

    val headlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesSection = 1
    var headlinesResponse: NewsResponse? = null

    /**
    /*ini hapus aja soalnya buat store hasil pencarian*/
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null
    **/

    init {
        getEverything("id")
    }

    fun getEverything(languageCode: String) = viewModelScope.launch {
        everythingInternet(languageCode)
    }
/**
    fun getHeadlines(countryCode: String, keyWord: String) = viewModelScope.launch {
        headlinesInternet(countryCode, keyWord)
    } **/

    private fun handleEverythingResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                everythingSection++
                if (everythingResponse == null) {
                    everythingResponse = resultResponse
                } else {
                    val oldArticles = everythingResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(everythingResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
/*INI HAPUS*/
    private fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinesSection++
                /*ini logic jika search part 5 menit 8:07*/
                if (headlinesResponse == null) {
                    headlinesSection = 1
                    headlinesResponse = resultResponse
                } else {
                    val oldArticles = headlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToSaved(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    private suspend fun everythingInternet(languageCode: String){
        everything.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response = newsRepository.getEverything(languageCode, everythingSection)
                everything.postValue(handleEverythingResponse(response))
            } else {
                everything.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> everything.postValue(Resource.Error("Unable to Connect"))
                else -> everything.postValue(Resource.Error("No Signal"))
            }
        }
    }

    /*fungsi yang search query part 5 12:52*/
    /**
    private suspend fun headlinesInternet(countryCode: String, keyWord: String){
        headlines.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response = newsRepository.getHeadlines(countryCode, keyWord, headlinesSection)
                headlines.postValue(handleHeadlinesResponse(response))
            } else {
                headlines.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> headlines.postValue(Resource.Error("Unable to Connect"))
                else -> headlines.postValue(Resource.Error("No Signal"))
            }
        }
    } **/
}