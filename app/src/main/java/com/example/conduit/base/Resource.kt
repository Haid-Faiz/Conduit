package com.example.conduit.base

sealed class Resource<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : Resource<T>(Status.SUCCESS, data)

    class Error<T>(message: String) : Resource<T>(Status.ERROR, message = message)

    class Loading<T> : Resource<T>(Status.LOADING)
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}