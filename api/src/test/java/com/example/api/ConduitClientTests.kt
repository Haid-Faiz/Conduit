package com.example.api

import com.example.api.models.requests.SignUpRequest
import com.example.api.models.requests.UserSignupCred
import com.example.api.models.responses.ArticlesResponse
import com.example.api.models.responses.UserResponse
import com.example.api.services.ConduitClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Response
import kotlin.random.Random

class ConduitClientTests {

    @Test
    fun `GET Articles`() {
        runBlocking {
            val apiArticlesResponse = ConduitClient.getApiService()!!.getArticles()
            assertNotNull(apiArticlesResponse.body()?.articles)
        }
    }

    @Test
    fun `GET Articles by Author`() {
        runBlocking {
            val apiArticlesResponse = ConduitClient.getApiService()!!.getArticles(author = "444")
            assertNotNull(apiArticlesResponse.body()?.articles)
        }
    }

    @Test
    fun `GET Articles by Tag`() {
        runBlocking {
            val apiArticlesResponse: Response<ArticlesResponse> = ConduitClient.getApiService()!!.getArticles(tag = "dragons")
            assertNotNull(apiArticlesResponse.body()?.articles)
        }
    }

    @Test
    fun `POST users - create user`() {

        val userCred = UserSignupCred(
            username = "testUserName${Random.nextInt(0, 1000)}",
            email = "testemail${Random.nextInt(0, 1000)}@test.com",
            password = "testPass${Random.nextInt(0, 1000)}"
        )

        runBlocking {
            val resp: Response<UserResponse>? = ConduitClient.getApiService()?.signUpUser(SignUpRequest(userCred))
            assertEquals(userCred.username, resp?.body()?.user?.username)
        }
    }

}