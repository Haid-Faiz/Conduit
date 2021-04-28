package com.example.conduit.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepo {

    suspend fun <T> safeApiCall(
        api: suspend () -> T
    ): Resource<out T> {

        return withContext(Dispatchers.IO) {
            try {
                Resource.Success<T>(api.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {

                    is HttpException -> Resource.Failure(
                        false,
                        throwable.code(),
                        throwable.response()?.errorBody()!!
                    )

                    else -> Resource.Failure(true, null, null)
                }
            }
        }
    }

    suspend fun logout() = safeApiCall {
        // TODO implement logout functionality
    }
}
