package com.example.conduit.data

import com.example.api.models.entities.User
import com.example.api.models.entities.UserUpdateCred
import com.example.api.models.requests.*
import com.example.api.models.responses.UserResponse
import com.example.api.services.ConduitClient
import retrofit2.Response

object UserRepo {

    val publicApi = ConduitClient.getApiService()
    val authApi = ConduitClient.getAuthApiService()

    suspend fun loginUser(email: String, password: String): User? {
        val response: Response<UserResponse>? = publicApi?.loginUser(LoginRequest(UserLoginCred(email, password)))
        ConduitClient.authToken = response?.body()?.user?.token
        return response?.body()?.user
    }

    suspend fun signUp(username: String, email: String, password: String): User? {
        val response = publicApi?.signUpUser(SignUpRequest(UserSignupCred(username, email, password)))
        ConduitClient.authToken = response?.body()?.user?.token
        return response?.body()?.user
    }

    suspend fun getCurrentUser() = authApi?.getCurrentUser()?.body()?.user

    suspend fun updateUser(
        username: String?,
        email: String?,
        password: String?,
        bio: String?,
        imageUrl: String?
    ): User? {
        val response = authApi?.updateCurrentUser(
            UserUpdateRequest(
                UserUpdateCred(
                    username, email, password, bio, imageUrl
                )
            )
        )
        return response?.body()?.user
    }
}