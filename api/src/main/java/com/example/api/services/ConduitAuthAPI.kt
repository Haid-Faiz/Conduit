package com.example.api.services

import com.example.api.models.requests.LoginRequest
import com.example.api.models.requests.SignUpRequest
import com.example.api.models.requests.UserUpdateRequest
import com.example.api.models.responses.*
import retrofit2.Response
import retrofit2.http.*

interface ConduitAuthAPI {

    @GET("user")
    suspend fun getCurrentUser(): Response<UserResponse>

    @PUT("user")
    suspend fun updateCurrentUser(
        @Body userUpdateRequest: UserUpdateRequest
    ): Response<UserResponse>

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

    @GET("articles/feed")
    suspend fun getFeedArticles(): Response<ArticlesResponse>

    @POST("articles/{slug}/favorite")
    suspend fun favoriteArticle(
        @Path("slug") slug: String
    ): Response<ArticleResponse>

    @DELETE("articles/{slug}/favorite")
    suspend fun unFavoriteArticle(
        @Path("slug") slug: String
    ): Response<ArticleResponse>
}
