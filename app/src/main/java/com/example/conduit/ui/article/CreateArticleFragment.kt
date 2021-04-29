package com.example.conduit.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.api.models.entities.Article
import com.example.api.services.ConduitClient
import com.example.conduit.base.BaseFragment
import com.example.conduit.base.Resource
import com.example.conduit.data.repos.ArticlesRepo
import com.example.conduit.databinding.FragmentCreateArticleBinding
import com.example.conduit.extensions.handleApiError
import com.example.conduit.extensions.showSnackBar

class CreateArticleFragment : BaseFragment<FragmentCreateArticleBinding, ArticleViewModel, ArticlesRepo>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.article.observe(viewLifecycleOwner) {
            (it is Resource.Loading).let { check: Boolean ->
                _binding!!.publishArticleProgressBar.isVisible = check
                _binding!!.publishArticleButton.text = if (check) "" else "Submit"
                _binding?.publishArticleButton?.isEnabled = !check
            }

            when (it) {
                is Resource.Failure -> handleApiError(it, { createArticle() })
                is Resource.Success -> updateUI(it.value.body()?.article)
            }
        }
    }

    private fun updateUI(article: Article?) {
        showSnackBar("Your is article successfully published.")
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

    override fun getRepo(): ArticlesRepo = ArticlesRepo(authApi = ConduitClient.getAuthApiService())

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCreateArticleBinding =
        FragmentCreateArticleBinding.inflate(inflater, container, false)
}
