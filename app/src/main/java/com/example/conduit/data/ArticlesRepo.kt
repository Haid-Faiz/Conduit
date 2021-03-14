package com.example.conduit.data

import com.example.api.services.ConduitClient

object ArticlesRepo {

    private val publicApi = ConduitClient.getApiService()

    suspend fun getArticles() = publicApi?.getArticles()
}