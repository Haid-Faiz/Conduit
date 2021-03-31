package com.example.conduit.data

import com.example.api.models.requests.CreateArticleRequest
import com.example.api.services.ConduitClient

object ArticlesRepo {

    private val publicApi = ConduitClient.getApiService()
    private val authAPI = ConduitClient.getAuthApiService()

    suspend fun getGlobalFeed() = publicApi?.getArticles()?.body()?.articles

    suspend fun getFeedArticles() = authAPI?.getFeedArticles()?.body()?.articles

    suspend fun createArticle(
        createArticleRequest: CreateArticleRequest
    ) = authAPI?.createArticle(createArticleRequest)?.body()?.article
}