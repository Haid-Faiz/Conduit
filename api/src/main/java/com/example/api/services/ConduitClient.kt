package com.example.api.services

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ConduitClient {

    companion object {

        private var retrofit: Retrofit? = null
        private var api: ConduitAPI? = null
        private const val BASE_URL = "https://conduit.productionready.io/api/"

        private fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }

            return retrofit
        }

        fun getApiService(): ConduitAPI? {
            if (api == null) {
                api = getClient()?.create(ConduitAPI::class.java)
            }
            return api
        }
    }
}