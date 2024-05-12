package com.example.rakamin_mandiri_newsapp.api

import com.example.rakamin_mandiri_newsapp.models.NewsResponse
import com.example.rakamin_mandiri_newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/everything")
    suspend fun getEverything(
        @Query("language")
        languageCode: String="id",
        @Query("q")
        keyWords: String,
        @Query("page")
        pageNumber: Int =1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        languageCode: String="id",
        @Query("q")
        keyWords: String="saham",
        @Query("page")
        pageNumber: Int =1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

}