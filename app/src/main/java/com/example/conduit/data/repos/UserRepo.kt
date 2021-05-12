package com.example.conduit.data.repos

import android.content.Context
import com.example.api.models.entities.User
import com.example.api.models.entities.UserUpdateCred
import com.example.api.models.requests.*
import com.example.api.models.responses.UserResponse
import com.example.api.services.ConduitAPI
import com.example.api.services.ConduitAuthAPI
import com.example.api.services.ConduitClient
import com.example.conduit.base.BaseRepo
import com.example.conduit.base.Resource
import com.example.conduit.data.UserPreference
import retrofit2.Response

class UserRepo(
    private val publicApi: ConduitAPI? = null,
    private val authApi: ConduitAuthAPI? = null,
    private val userPreference: UserPreference
) : BaseRepo() {

//    val publicApi = ConduitClient.getApiService()
//    val authApi = ConduitClient.getAuthApiService()

//    suspend fun loginUser(email: String, password: String): User? {
//        val response: Response<UserResponse>? = publicApi?.loginUser(LoginRequest(UserLoginCred(email, password)))
//        ConduitClient.authToken = response?.body()?.user?.token
//        return response?.body()?.user
//    }

//    suspend fun signUp(username: String, email: String, password: String): User? {
//        val response = publicApi?.signUpUser(SignUpRequest(UserSignupCred(username, email, password)))
//        ConduitClient.authToken = response?.body()?.user?.token
//        return response?.body()?.user
//    }

//    suspend fun getCurrentUser(token: String): User? {
//        ConduitClient.authToken = token
//        return authApi?.getCurrentUser()?.body()?.user
//    }

//    suspend fun updateUser(
//        username: String?,
//        email: String?,
//        password: String?,
//        bio: String?,
//        imageUrl: String?
//    ): User? {
//        val response = authApi?.updateCurrentUser(
//            UserUpdateRequest(
//                UserUpdateCred(
//                    username, email, password, bio, imageUrl
//                )
//            )
//        )
//        return response?.body()?.user
//    }


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