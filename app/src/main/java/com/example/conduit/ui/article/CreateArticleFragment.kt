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
import com.example.conduit.databinding.FragmentCreateArticleBinding

class CreateArticleFragment : Fragment() {

    private var _binding: FragmentCreateArticleBinding? = null
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreateArticleBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        _binding?.apply {
            publishArticleButton.setOnClickListener {
                articleViewModel.createArticle(
                    title = inputArticleTitle.editText?.text.toString().trim(),
                    description = inputAboutArticle.editText?.text.toString().trim(),
                    body = inputArticleBody.editText?.text.toString().trim()
                )
                // start progress
                _binding?.publishArticleProgressBar?.isVisible = true
                _binding?.publishArticleButton?.text = "Please wait..."
                _binding?.publishArticleButton?.isEnabled = false
            }
        }

        articleViewModel.article.observe(viewLifecycleOwner) {
            it?.let {
                // end progress
                _binding?.publishArticleProgressBar?.isVisible = true
                _binding?.publishArticleButton?.isEnabled = false
                findNavController().navigateUp()
                Toast.makeText(requireContext(), "Article Created", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // I don't want to go in future, i afraid of it !
    // Present does'nt have Relax type of Relax
    // I like the Past...
    // want to go in past
    // Is anyone here who could let me go over there

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
