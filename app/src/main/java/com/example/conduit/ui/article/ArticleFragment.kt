package com.example.conduit.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.conduit.databinding.FragmentArticleBinding
import com.example.conduit.extensions.formatDate
import com.example.conduit.extensions.loadImage
import com.example.conduit.ui.feed.FeedViewModel

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var feedViewModel: FeedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        feedViewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
