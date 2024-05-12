package com.example.rakamin_mandiri_newsapp

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)