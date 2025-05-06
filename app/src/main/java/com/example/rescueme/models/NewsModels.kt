package com.example.rescueme.models

data class NewsArticle(
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String
)

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
) 