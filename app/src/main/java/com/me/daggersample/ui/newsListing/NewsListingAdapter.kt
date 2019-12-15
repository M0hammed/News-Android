package com.me.daggersample.ui.newsListing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.me.daggersample.R
import com.me.daggersample.base.BaseAdapter
import com.me.daggersample.base.BaseViewHolder
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.data.NewsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*

class NewsListingAdapter(context: Context, onListItemClickListener: OnListItemClickListener<NewsModel>) :
    BaseAdapter<NewsModel>(context, onListItemClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return NewsListingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val newsListingViewHolder = holder as NewsListingViewHolder
        newsListingViewHolder.onBind(position)
    }

    inner class NewsListingViewHolder(itemView: View) : BaseViewHolder(itemView) {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onBind(position: Int) {
            itemView.tvTitle.text = data[position].newsTitle
            itemView.tvDate.text = data.get(position).postDate
            itemView.tvLikes.text = context.resources.getString(R.string.likes, data[position].likes)
            itemView.tvViews.text = context.run { resources.getString(R.string.views, data.get(position).numofViews) }
            Picasso.get()
                .load(data.get(position).imageUrl)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.news_placeholder)!!)
                .error(ContextCompat.getDrawable(context, R.drawable.news_placeholder)!!)
                .into(itemView.imgNews)
        }

        override fun onClick(view: View?) {
            onListItemClickListener.onItemClicked(view, data[adapterPosition])
        }
    }
}