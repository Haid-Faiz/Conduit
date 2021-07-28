package com.example.conduit.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.services.ConduitClient
import com.example.conduit.R
import com.example.conduit.base.BaseFragment
import com.example.conduit.base.Resource
import com.example.conduit.data.repos.ArticlesRepo
import com.example.conduit.databinding.FragmentFeedBinding
import com.example.conduit.utils.handleApiError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MyFeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel, ArticlesRepo>() {

    private lateinit var feedArticleAdapter: FeedArticleAdapter
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        _binding!!.feedRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        feedArticleAdapter = FeedArticleAdapter() {
            viewModel.saveArticle(it)
            navController.navigate(R.id.action_nav_my_feed_to_nav_article)
        }
        _binding!!.feedRecyclerview.adapter = feedArticleAdapter

        // Calling the fetchGlobalFeed() method to make a request to server & then observing feed (live data)
        viewModel.fetchMyFeed()
        viewModel.feed.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Success -> feedArticleAdapter.submitList(it.data?.articles)
                is Resource.Error -> handleApiError(it)
                is Resource.Loading -> {
                    _binding?.feedRecyclerview?.isVisible = true
                    _binding?.shimmerLayout?.stopShimmer()
                    _binding?.shimmerLayout?.isVisible = false
                }
            }
        }
    }

    override fun getViewModal(): Class<FeedViewModel> = FeedViewModel::class.java

    override fun getRepo(): ArticlesRepo {
        val authToken = runBlocking { userPreference.authToken.first() }
        return ArticlesRepo(authApi = authToken?.let { ConduitClient.getAuthApiService(it) })
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFeedBinding =
        FragmentFeedBinding.inflate(inflater, container, false)

}