package com.example.conduit.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.conduit.R
import com.example.conduit.databinding.FragmentFeedBinding

class GlobalFeedFragment : Fragment() {

    private var _fragmentFeedBinding: FragmentFeedBinding? = null
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var feedArticleAdapter: FeedArticleAdapter
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        feedViewModel = ViewModelProvider(requireActivity()).get(FeedViewModel::class.java)
        _fragmentFeedBinding = FragmentFeedBinding.inflate(inflater, container, false)
        return _fragmentFeedBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        _fragmentFeedBinding!!.feedRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        feedArticleAdapter = FeedArticleAdapter {
            feedViewModel.saveArticle(it)
            navController.navigate(R.id.action_nav_feed_to_nav_article)
        }
        _fragmentFeedBinding!!.feedRecyclerview.adapter = feedArticleAdapter

        // Calling the fetchGlobalFeed() method to make a request to server
        feedViewModel.fetchGlobalFeed()
        // & now observing the viewModel
        feedViewModel.feed.observe(viewLifecycleOwner) {
            feedArticleAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Making it null in onDestroyView saves a memory leak
        _fragmentFeedBinding = null
    }

}