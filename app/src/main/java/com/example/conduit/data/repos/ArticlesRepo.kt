package com.example.conduit.data.repos

import com.example.api.models.requests.CreateArticleRequest
import com.example.api.models.responses.ArticleResponse
import com.example.api.models.responses.ArticlesResponse
import com.example.api.services.ConduitAPI
import com.example.api.services.ConduitAuthAPI
import com.example.conduit.base.BaseRepo
import com.example.conduit.base.Resource

class ArticlesRepo(
    private val publicApi: ConduitAPI? = null,
    private val authApi: ConduitAuthAPI? = null
) : BaseRepo() {


    suspend fun getGlobalFeed(): Resource<ArticlesResponse> = safeApiCall {
        publicApi!!.getArticles()
    }

    suspend fun getFeedArticles(): Resource<ArticlesResponse> = safeApiCall { authApi!!.getFeedArticles() }

    suspend fun createArticle(
        createArticleRequest: CreateArticleRequest
    ): Resource<ArticleResponse> = safeApiCall {
        authApi!!.createArticle(createArticleRequest)
    }

}