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
import com.example.conduit.utils.handleApiError

class GlobalFeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel, ArticlesRepo>() {



    //        ListFragmentDirections.ActionListFragmentToDetailsFragment action =
    //                ListFragmentDirections.actionListFragmentToDetailsFragment();
    //
    //        action.setPosition(position);
    //        navControllerTwo.navigate(action);
    //-----------------------------------------------------

    //        In Java --->
//        position = DetailsFragmentArgs.fromBundle(getArguments()).position
//        In Kotlin --->
//        val args by navArgs<DetailsFragmentArgs>()

    private lateinit var feedArticleAdapter: FeedArticleAdapter
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        setUpRecyclerView()
        // Calling the fetchGlobalFeed() method to make a request to server
        viewModel.fetchGlobalFeed()
        // & now observing the viewModel
        viewModel.feed.observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Error -> handleApiError(it)  // Here it will be the Resource.Error object
                is Resource.Success -> feedArticleAdapter.submitList(it.data?.articles)
                is Resource.Loading -> {
                    _binding?.feedRecyclerview?.isVisible = true
                    _binding?.shimmerLayout?.isVisible = false
                    _binding?.shimmerLayout?.stopShimmer()
                }
            }
        }

    }

    private fun setUpRecyclerView() {
        _binding!!.feedRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        feedArticleAdapter = FeedArticleAdapter {
            viewModel.saveArticle(it)
            // JetPack Safe Args
//            val action : GlobalFeedFragmentDirections.ActionNavFeedToNavArticle = GlobalFeedFragmentDirections.actionNavFeedToNavArticle()
//            action.article = it
//            navController.navigate(action)
            navController.navigate(R.id.action_nav_feed_to_nav_article)
        }
        _binding!!.feedRecyclerview.adapter = feedArticleAdapter
    }

    override fun getViewModal(): Class<FeedViewModel> = FeedViewModel::class.java

    override fun getRepo(): ArticlesRepo = ArticlesRepo(publicApi = ConduitClient.getApiService())

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFeedBinding =
        FragmentFeedBinding.inflate(inflater, container, false)

}