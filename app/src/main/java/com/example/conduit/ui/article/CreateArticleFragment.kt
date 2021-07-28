package com.example.conduit.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.api.models.entities.Article
import com.example.api.services.ConduitClient
import com.example.conduit.base.BaseFragment
import com.example.conduit.base.Resource
import com.example.conduit.data.repos.ArticlesRepo
import com.example.conduit.databinding.FragmentCreateArticleBinding
import com.example.conduit.utils.handleApiError
import com.example.conduit.utils.showSnackBar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CreateArticleFragment : BaseFragment<FragmentCreateArticleBinding, ArticleViewModel, ArticlesRepo>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createArticle()
        viewModel.article.observe(viewLifecycleOwner) {
            (it is Resource.Loading).let { check: Boolean ->
                _binding!!.publishArticleProgressBar.isVisible = check
                _binding!!.publishArticleButton.text = if (check) "" else "Submit"
                _binding?.publishArticleButton?.isEnabled = !check
            }

            when (it) {
                is Resource.Error -> handleApiError(it) { createArticle() }
                is Resource.Success -> updateUI(it.data?.article)
            }
        }
    }

    private fun updateUI(article: Article?) {
        requireView().showSnackBar("Your is article successfully published.")
        findNavController().navigateUp()
    }

    private fun createArticle() {
        _binding?.apply {
            publishArticleButton.setOnClickListener {
                viewModel.createArticle(
                    title = inputArticleTitle.editText?.text.toString().trim(),
                    description = inputAboutArticle.editText?.text.toString().trim(),
                    body = inputArticleBody.editText?.text.toString().trim()
                )
            }
        }
    }


    // I don't want to go in future, i afraid of it !
    // Present does'nt have Relax type of Relax
    // I like the Past...
    // want to go in past
    // Is anyone here who could let me go over there

    override fun getViewModal(): Class<ArticleViewModel> = ArticleViewModel::class.java

    override fun getRepo(): ArticlesRepo {
        val authToken =
            runBlocking { userPreference.authToken.first() } // We shouldn't call runBlocking{} as it can cause the ANR
        return ArticlesRepo(authApi = authToken?.let { ConduitClient.getAuthApiService(it) })
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCreateArticleBinding =
        FragmentCreateArticleBinding.inflate(inflater, container, false)
}
