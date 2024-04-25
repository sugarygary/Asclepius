package com.dicoding.asclepius.data.network.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class HeadlineResponse(

    @Json(name = "totalResults")
    val totalResults: Int? = null,

    @Json(name = "articles")
    val articles: List<Article>,

    @Json(name = "status")
    val status: String
)


