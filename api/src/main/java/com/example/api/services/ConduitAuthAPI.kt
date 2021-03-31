package com.example.api.services

import com.example.api.models.entities.CreateArticle
import com.example.api.models.requests.CreateArticleRequest
import com.example.api.models.requests.LoginRequest
import com.example.api.models.requests.SignUpRequest
import com.example.api.models.requests.UserUpdateRequest
import com.example.api.models.responses.*
import retrofit2.Response
import retrofit2.http.*

interface ConduitAuthAPI {

    // These request calls requires a authentication token
    // So either send token with headers with each call or
    // either add an interceptor in client itself

    @GET("user")
    suspend fun getCurrentUser(): Response<UserResponse>

    @PUT("user")
    suspend fun updateCurrentUser(
        @Body userUpdateRequest: UserUpdateRequest
    ): Response<UserResponse>

    @POST("articles")
    suspend fun createArticle(
        @Body createArticleRequest: CreateArticleRequest
    ): Response<ArticleResponse>

    @GET("articles/feed")
    suspend fun getFeedArticles(): Response<ArticlesResponse>

    @GET("profiles/{username}")
    suspend fun getProfile(
        @Path("username") username: String
    ): Response<ProfileResponse>

    @POST("profiles/{username}/follow")
    suspend fun followProfile(
        @Path("username") username: String
    ): Response<ProfileResponse>

    @DELETE("profiles/{username}/follow")
    suspend fun unFollowProfile(
        @Path("username") username: String
    ): Response<ProfileResponse>

    @POST("articles/{slug}/favorite")
    suspend fun favoriteArticle(
        @Path("slug") slug: String
    ): Response<ArticleResponse>

    @DELETE("articles/{slug}/favorite")
    suspend fun unFavoriteArticle(
        @Path("slug") slug: String
    ): Response<ArticleResponse>
}
