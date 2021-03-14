package com.example.conduit.ui.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entities.Article
import com.example.api.models.responses.ArticlesResponse
import com.example.conduit.data.ArticlesRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class FeedViewModel : ViewModel() {

    private val _feed: MutableLiveData<List<Article>> = MutableLiveData<List<Article>>()
    var feed: LiveData<List<Article>> = _feed

    fun fetchGlobalFeed() = viewModelScope.launch {
        ArticlesRepo.getArticles()?.body().let {
            _feed.postValue(it?.articles)
        }
    }

}