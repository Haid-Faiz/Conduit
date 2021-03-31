package com.example.conduit.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entities.Article
import com.example.conduit.data.ArticlesRepo
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {

    private lateinit var article: Article
    private val _feed: MutableLiveData<List<Article>> = MutableLiveData<List<Article>>()
    var feed: LiveData<List<Article>> = _feed

    fun fetchGlobalFeed() = viewModelScope.launch {
        ArticlesRepo.getGlobalFeed()?.let {
            _feed.postValue(it)
        }
    }

    fun fetchMyFeed() = viewModelScope.launch {
        ArticlesRepo.getFeedArticles()?.let {
            _feed.postValue(it)
        }
    }

    fun saveArticle(article: Article) {
        this.article = article
    }

    fun getArticle(): Article = article

}