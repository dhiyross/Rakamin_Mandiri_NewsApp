package com.example.rakamin_mandiri_newsapp.repository

import com.example.rakamin_mandiri_newsapp.api.RetrofitInstance
import com.example.rakamin_mandiri_newsapp.db.ArticleDatabase
import com.example.rakamin_mandiri_newsapp.models.Article

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getEverything(languageCode: String, keyWords: String, pageNumber: Int)=
        RetrofitInstance.api.getEverything(languageCode, keyWords, pageNumber)

    suspend fun getHeadlines(countryCode: String, keyWords: String="saham", pageNumber: Int)=
        RetrofitInstance.api.getHeadlines(countryCode, keyWords, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}