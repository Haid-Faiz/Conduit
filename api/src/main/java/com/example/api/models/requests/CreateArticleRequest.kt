package com.example.api.models.requests


import com.example.api.models.entities.CreateArticle
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateArticleRequest(
    @Json(name = "article")
    val article: CreateArticle
)