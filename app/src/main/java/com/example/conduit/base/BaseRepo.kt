package com.example.conduit.base

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseRepo {

    suspend fun <T> safeApiCall(
        api: suspend () -> Response<T>
    ): Resource<T> {

        return withContext(Dispatchers.IO) {
            try {
                Log.d("baseRepo00", "safeApiCall: success")

                val response = api.invoke()

                Log.d("baseRepo001", "safeApiCall: success")

                if (response.isSuccessful) {
                    Log.d("baseRepo1", "safeApiCall: success1")
                    Resource.Success<T>(response.body()!!)
                } else {
                    Log.d("baseRepo2", "safeApiCall: not successful")
                    Resource.Failure(
                        isNetworkError = false,
                        errorCode = response.code(),
                        errorBody = response.errorBody()
                    )
                }

            } catch (throwable: Throwable) {
                Log.d("baseRepo3", "safeApiCall: failed  // Exception: mesa${throwable.message} /--/")

                when (throwable) {

                    is HttpException -> Resource.Failure(
                        false,
                        throwable.code(),
                        throwable.response()?.errorBody()!!
                    )

//                    is IOException -> // not yet implemented

                    else -> Resource.Failure(true, null, null)
                }
            }
        }
    }

    suspend fun logout() {
        // TODO implement logout
    }
}
