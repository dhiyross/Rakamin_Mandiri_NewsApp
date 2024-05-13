package com.example.rakamin_mandiri_newsapp.repository

import com.example.rakamin_mandiri_newsapp.api.RetrofitInstance
import com.example.rakamin_mandiri_newsapp.db.ArticleDatabase
import com.example.rakamin_mandiri_newsapp.models.Article

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getEverything(languageCode: String, pageNumber: Int)=
        RetrofitInstance.api.getEverything(languageCode, pageNumber)

    suspend fun getHeadlines(countryCode: String, pageNumber: Int)=
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}