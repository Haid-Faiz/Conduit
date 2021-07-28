package com.example.conduit.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo {

    suspend fun <T> safeApiCall(api: suspend () -> Response<T>): Resource<T> {

        return withContext(Dispatchers.IO) {

            try {
                val response = api()
                if (response.isSuccessful)
                    Resource.Success(data = response.body()!!)
                else
                    Resource.Error(response.message())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> Resource.Error("Something went wrong")
                    is IOException -> Resource.Error("Network error")
                    else -> Resource.Error(throwable.message.toString())
                }
            }

        }
    }

    suspend fun logout() {
        // TODO implement logout
    }
}
