package com.example.conduit.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.conduit.AuthViewModel
import com.example.conduit.data.repos.ArticlesRepo
import com.example.conduit.data.repos.UserRepo

import com.example.conduit.ui.article.ArticleViewModel
import com.example.conduit.ui.feed.FeedViewModel

class ViewModelFactory(private val repo: BaseRepo) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repo as UserRepo) as T
            modelClass.isAssignableFrom(ArticleViewModel::class.java) -> ArticleViewModel(repo as ArticlesRepo) as T
            modelClass.isAssignableFrom(FeedViewModel::class.java) -> FeedViewModel(repo as ArticlesRepo) as T
            else -> throw IllegalArgumentException("Your ViewModel class isn't found")
        }
    }
}
