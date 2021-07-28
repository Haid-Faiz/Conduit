package com.example.conduit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.api.models.entities.Article
import com.example.api.services.ConduitAPI
import com.example.conduit.utils.Constants.STARTING_PAGE_NUMBER
import retrofit2.HttpException

class ArticlesPagingSource(private val publicApi: ConduitAPI) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        val page = params.key ?: STARTING_PAGE_NUMBER

        return try {
            val response = publicApi.getArticles()
            val data = response.body()!!.articles
            LoadResult.Page(
                data = data,
                prevKey = if (page == STARTING_PAGE_NUMBER) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        TODO("Not yet implemented")
    }
}