package com.example.conduit.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.services.ConduitClient
import com.example.conduit.R
import com.example.conduit.base.BaseFragment
import com.example.conduit.base.Resource
import com.example.conduit.data.repos.ArticlesRepo
import com.example.conduit.databinding.FragmentFeedBinding
import com.example.conduit.extensions.handleApiError

class GlobalFeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel, ArticlesRepo>() {

    private lateinit var feedArticleAdapter: FeedArticleAdapter
    private lateinit var navController: NavController

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        feedViewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)
//        _fragmentFeedBinding = FragmentFeedBinding.inflate(inflater, container, false)
//        return _fragmentFeedBinding?.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        _binding!!.feedRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        feedArticleAdapter = FeedArticleAdapter {
            viewModel.saveArticle(it)
            navController.navigate(R.id.action_nav_feed_to_nav_article)
        }
        _binding!!.feedRecyclerview.adapter = feedArticleAdapter

        // Calling the fetchGlobalFeed() method to make a request to server
        viewModel.fetchGlobalFeed()
        // & now observing the viewModel
        viewModel.feed.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Success -> feedArticleAdapter.submitList(it.value.body()?.articles)
                is Resource.Failure -> handleApiError(it)  // Here it will be the Resource.Failure object
                Resource.Loading -> {
                    _binding?.feedRecyclerview?.isVisible = true
                    _binding?.shimmerLayout?.isVisible = false
                    _binding?.shimmerLayout?.stopShimmer()
                }
            }
        }
    }

    override fun getViewModal(): Class<FeedViewModel> = FeedViewModel::class.java

    override fun getRepo(): ArticlesRepo = ArticlesRepo(publicApi = ConduitClient.getApiService())

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFeedBinding =
        FragmentFeedBinding.inflate(inflater, container, false)

}