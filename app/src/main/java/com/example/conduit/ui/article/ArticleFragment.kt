package com.example.conduit.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.api.services.ConduitClient
import com.example.conduit.base.BaseFragment
import com.example.conduit.data.repos.ArticlesRepo
import com.example.conduit.databinding.FragmentArticleBinding
import com.example.conduit.extensions.formatDate
import com.example.conduit.extensions.loadImage
import com.example.conduit.ui.feed.FeedViewModel

class ArticleFragment : BaseFragment<FragmentArticleBinding, ArticleViewModel, ArticlesRepo>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val feedViewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)

        feedViewModel.getArticle()?.let {
            _binding?.apply {
                articleTitle.text = it.title
                articleTimestamp.formatDate(it.createdAt)
                articleAuthorName.text = it.author.username
                articleBody.text = it.body
                articleAuthorImage.loadImage(it.author.image)
            }
        }
    }

    override fun getViewModal(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getRepo(): ArticlesRepo =
        ArticlesRepo(ConduitClient.getApiService(), ConduitClient.getAuthApiService())

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentArticleBinding =
        FragmentArticleBinding.inflate(inflater, container, false)
}
