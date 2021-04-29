package com.example.conduit.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.models.entities.CreateArticle
import com.example.api.models.requests.CreateArticleRequest
import com.example.api.models.responses.ArticleResponse
import com.example.conduit.base.BaseViewModel
import com.example.conduit.base.Resource
import com.example.conduit.data.ArticlesRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class ArticleViewModel(private val articlesRepo: ArticlesRepo) : BaseViewModel(articlesRepo) {

    private var _article : MutableLiveData<Resource<out Response<ArticleResponse>>> = MutableLiveData()
    val article: LiveData<Resource<out Response<ArticleResponse>>> = _article

    fun createArticle(
        title: String,
        description: String,
        body: String
    ) = viewModelScope.launch {
        articlesRepo.createArticle(
            CreateArticleRequest(
                CreateArticle(
                    title = title,
                    description = description,
                    body = body,
                    tagList = null
                )
            )
        ).let {
            _article.postValue(it)
        }
    }
}