package com.example.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserUpdateCred(
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "image")
    val image: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)