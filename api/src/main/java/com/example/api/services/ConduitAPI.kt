package com.example.api.services

import com.example.api.models.requests.LoginRequest
import com.example.api.models.requests.SignUpRequest
import com.example.api.models.responses.ArticleResponse
import com.example.api.models.responses.ArticlesResponse
import com.example.api.models.responses.TagsResponse
import com.example.api.models.responses.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface ConduitAPI {

    @GET("articles")
    suspend fun getArticles(
        @Query("author") author: String? = null,
        @Query("favorite") favorite: String? = null,
        @Query("tag") tag: String? = null
    ): Response<ArticlesResponse>   // To use coroutine we need to remove Call<> & then
    // wrap it inside Response<>

    @POST("users")
    suspend fun signUpUser(
        @Body signUpRequest: SignUpRequest
    ): Response<UserResponse>

    @POST("users/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    )

    @GET("articles/{slug}")
    suspend fun getArticleBySlug(
        @Path("slug") slug: String
    ): Response<ArticleResponse>

    @GET("tags")
    suspend fun getTags() : Response<TagsResponse>
}
