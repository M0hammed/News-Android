package com.me.daggersample.ui.TeamsListing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.me.daggersample.R
import com.me.daggersample.base.BaseAdapter
import com.me.daggersample.base.BaseViewHolder
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.model.team.Teams

class TeamsListingAdapter(context: Context, onListItemClickListener: OnListItemClickListener<Teams>) :
    BaseAdapter<Teams>(context, onListItemClickListener) {

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

        }

        override fun onClick(view: View?) {
            onListItemClickListener.onItemClicked(view, data[adapterPosition])
        }
    }
}