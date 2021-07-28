package com.example.conduit.data.repos

import com.example.api.models.entities.UserUpdateCred
import com.example.api.models.requests.*
import com.example.api.models.responses.UserResponse
import com.example.api.services.ConduitAPI
import com.example.api.services.ConduitAuthAPI
import com.example.conduit.base.BaseRepo
import com.example.conduit.base.Resource
import com.example.conduit.data.UserPreference

class UserRepo(
    private val publicApi: ConduitAPI? = null,
    private val authApi: ConduitAuthAPI? = null,
    private val userPreference: UserPreference
) : BaseRepo() {

    suspend fun loginUser(email: String, password: String): Resource<UserResponse> = safeApiCall {
        publicApi!!.loginUser(LoginRequest(UserLoginCred(email, password)))
    }

    suspend fun signUp(username: String, email: String, password: String): Resource<UserResponse> =
        safeApiCall {
            publicApi!!.signUpUser(SignUpRequest(UserSignupCred(username, email, password)))
        }

    suspend fun getCurrentUser(token: String): Resource<UserResponse> = safeApiCall {
        authApi!!.getCurrentUser()
    }

    suspend fun updateUser(
        username: String?,
        email: String?,
        bio: String?,
        imageUrl: String?
    ): Resource<UserResponse> = safeApiCall {
        authApi!!.updateCurrentUser(UserUpdateRequest(UserUpdateCred(username, email, bio, imageUrl)))
    }

    suspend fun saveAuthToken(token: String) {
        userPreference.saveAuthToken(token)
    }
}