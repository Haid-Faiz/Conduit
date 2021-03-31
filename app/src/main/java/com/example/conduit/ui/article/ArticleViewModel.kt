package com.example.conduit.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entities.Article
import com.example.api.models.entities.CreateArticle
import com.example.api.models.requests.CreateArticleRequest
import com.example.conduit.data.ArticlesRepo
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private var _article = MutableLiveData<Article?>()
    val article: LiveData<Article?> = _article

    fun createArticle(
        title: String,
        description: String,
        body: String
    ) = viewModelScope.launch {
        ArticlesRepo.createArticle(
            CreateArticleRequest(
                CreateArticle(
                    title = title,
                    description = description,
                    body = body,
                    tagList = null
                )
            )
        )?.let {
            _article.postValue(it)
        }
    }
}