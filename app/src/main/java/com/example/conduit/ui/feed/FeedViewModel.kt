package com.example.conduit.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.models.entities.Article
import com.example.api.models.responses.ArticlesResponse
import com.example.conduit.base.BaseViewModel
import com.example.conduit.base.Resource
import com.example.conduit.data.repos.ArticlesRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class FeedViewModel(private val articlesRepo: ArticlesRepo) : BaseViewModel(articlesRepo) {

    private lateinit var article: Article

    private var _feed: MutableLiveData<Resource<ArticlesResponse>> = MutableLiveData()
    val feed: LiveData<Resource<ArticlesResponse>> = _feed

    fun fetchGlobalFeed() = viewModelScope.launch {
        _feed.postValue(Resource.Loading())
        _feed.postValue(articlesRepo.getGlobalFeed())
    }

    fun fetchMyFeed() = viewModelScope.launch {
        articlesRepo.getFeedArticles().let {
            _feed.postValue(it)
        }
    }

    fun saveArticle(article: Article) {
        this.article = article
    }

    fun getArticle(): Article = article

}