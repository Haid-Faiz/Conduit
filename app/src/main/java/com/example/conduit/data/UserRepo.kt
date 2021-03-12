package com.example.conduit.data

import com.example.api.models.entities.User
import com.example.api.models.requests.LoginRequest
import com.example.api.models.requests.SignUpRequest
import com.example.api.models.requests.UserLoginCred
import com.example.api.models.requests.UserSignupCred
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

    suspend fun signUp(username: String, email: String, password: String) =
        publicApi?.signUpUser(SignUpRequest(UserSignupCred(username, email, password)))?.body()?.user

    suspend fun getCurrentUser() = authApi?.getCurrentUser()?.body()?.user
}