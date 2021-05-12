package com.example.api.services

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ConduitClient {

    private var retrofitBuilder: Retrofit.Builder? = null
    private var publicApi: ConduitAPI? = null
    private var authApi: ConduitAuthAPI? = null
    private const val BASE_URL = "https://conduit.productionready.io/api/"

//    private val authInterceptor: Interceptor = object : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            var request = chain.request()
//            authToken?.let {
//                request = request.newBuilder().header("Authorization", "Token $it").build()
//            }
//            return chain.proceed(request = request)
//        }
//    }

    private var okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(2, TimeUnit.SECONDS)  // .build()  ->  will give okHttpclient

    private fun getClient(): Retrofit.Builder? {
        if (retrofitBuilder == null) {
            retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
        }
        return retrofitBuilder
    }

    fun getApiService(): ConduitAPI? {
        if (publicApi == null) {
            publicApi = getClient()?.client(
                okHttpBuilder.build()   // okHttpBuilder.build() will give the okHttpClient
            )?.build()?.create(ConduitAPI::class.java)
        }
        return publicApi
    }

    fun getAuthApiService(token: String): ConduitAuthAPI? {
        if (authApi == null) {
            authApi = getClient()?.client(
                okHttpBuilder.addInterceptor(Interceptor { chain ->
                    var request = chain.request()
                    request = request.newBuilder().header("Authorization", "Token $token").build()
                    chain.proceed(request)
                }).build()
            )?.build()?.create(ConduitAuthAPI::class.java)
        }
        return authApi
    }

//    fun getAuthApiService(): ConduitAuthAPI? {
//        if (authApi == null) {
//            authApi = getClient()?.client(
//                okHttpBuilder.addInterceptor(authInterceptor).build()
//            )?.build()?.create(ConduitAuthAPI::class.java)
//        }
//        return authApi
//    }

//    ***NOTE***
//    Interceptors allow us to intercept incoming or outgoing HTTP requests using the HttpClient.
//    By intercepting the HTTP request, we can modify or change the value of the request.
}