package com.example.conduit.data

import com.example.api.models.entities.Article
import com.example.api.services.ConduitClient

object ArticlesRepo {

    private val publicApi = ConduitClient.getApiService()
    private val authAPI = ConduitClient.getAuthApiService()

    suspend fun getGlobalFeed() = publicApi?.getArticles()?.body()?.articles

    suspend fun getMyFeed() = authAPI?.getFeedArticles()?.body()?.articles
}