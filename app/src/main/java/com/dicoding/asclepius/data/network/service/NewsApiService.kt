package com.dicoding.asclepius.data.network.service

import com.dicoding.asclepius.data.network.response.HeadlineResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("q") q: String = "cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String
    ): HeadlineResponse
}