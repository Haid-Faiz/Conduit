package com.example.conduit.data.repos

import com.example.api.models.requests.CreateArticleRequest
import com.example.api.models.responses.ArticleResponse
import com.example.api.models.responses.ArticlesResponse
import com.example.api.services.ConduitAPI
import com.example.api.services.ConduitAuthAPI
import com.example.api.services.ConduitClient
import com.example.conduit.base.BaseRepo
import com.example.conduit.base.Resource
import retrofit2.Response

class ArticlesRepo(
    private val publicApi: ConduitAPI? = null,
    private val authApi: ConduitAuthAPI? = null
) : BaseRepo() {

//    private val publicApi = ConduitClient.getApiService()
//    private val authAPI = ConduitClient.getAuthApiService()

//    suspend fun getGlobalFeed() = publicApi?.getArticles()?.body()?.articles
//
//    suspend fun getFeedArticles() = authApi?.getFeedArticles()?.body()?.articles
//
//    suspend fun createArticle(
//        createArticleRequest: CreateArticleRequest
//    ) = authApi?.createArticle(createArticleRequest)?.body()?.article

    suspend fun getGlobalFeed(): Resource<ArticlesResponse> = safeApiCall { publicApi!!.getArticles() }

    suspend fun getFeedArticles(): Resource<ArticlesResponse> = safeApiCall { authApi!!.getFeedArticles() }

    suspend fun createArticle(
        createArticleRequest: CreateArticleRequest
    ): Resource<ArticleResponse> = safeApiCall { authApi!!.createArticle(createArticleRequest) }

}