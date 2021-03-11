package com.example.api.models.requests


import com.example.api.models.entities.UserUpdateCred
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserUpdateRequest(
    @Json(name = "user")
    val userUpdateCred: UserUpdateCred
)