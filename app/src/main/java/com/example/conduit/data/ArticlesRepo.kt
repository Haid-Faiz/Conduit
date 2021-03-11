package com.example.conduit.data

import com.example.api.services.ConduitClient

object ArticlesRepo {

    private val api = ConduitClient.getApiService()

    suspend fun getArticles() = api?.getArticles()
}