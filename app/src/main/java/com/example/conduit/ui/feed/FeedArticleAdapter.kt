package com.example.conduit.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.entities.Article
import com.example.conduit.databinding.ListItemArticleBinding

class FeedArticleAdapter(
    private val onArticleClicked: (article: Article) -> Unit
) : ListAdapter<Article, FeedArticleAdapter.ArticleViewHolder>(
    object : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.createdAt.equals(newItem.createdAt)

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.equals(newItem)
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val listItemArticleBinding = ListItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(listItemArticleBinding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.listItemArticleBinding.author.text = article.author.username
        holder.listItemArticleBinding.timestamp.text = article.createdAt
        holder.listItemArticleBinding.articleText.text = article.title
        holder.listItemArticleBinding.descriptionText.text = article.body
        holder.listItemArticleBinding.root.setOnClickListener {
            onArticleClicked.invoke(getItem(position))
        }
    }

    class ArticleViewHolder(val listItemArticleBinding: ListItemArticleBinding) :
        RecyclerView.ViewHolder(listItemArticleBinding.root)
}