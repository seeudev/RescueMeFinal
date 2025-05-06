package com.example.rescueme.api

import com.example.rescueme.models.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getTopHeadlines(
        @Header("X-Api-Key") apiKey: String,
        @Query("q") query: String = "(Philippines AND (emergency OR disaster OR typhoon OR earthquake OR flood OR evacuation OR rescue OR crisis OR accident OR incident))",
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("pageSize") pageSize: Int = 20,
        @Query("searchIn") searchIn: String = "title,description"
    ): NewsResponse

    companion object {
        const val BASE_URL = "https://newsapi.org/"
    }
} 