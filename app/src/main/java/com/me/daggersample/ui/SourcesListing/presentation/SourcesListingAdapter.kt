package com.me.daggersample.ui.SourcesListing.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.me.daggersample.R
import com.me.daggersample.base.BaseAdapter
import com.me.daggersample.base.BaseViewHolder
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.model.source.Sources
import javax.inject.Inject
import kotlinx.android.synthetic.main.item_source.view.*

class SourcesListingAdapter @Inject constructor(
    context: Context,
    onListItemClickListener: OnListItemClickListener<Sources>?
) : BaseAdapter<Sources>(context, onListItemClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return SourcesListingViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_source, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val newsListingViewHolder = holder as SourcesListingViewHolder
        newsListingViewHolder.onBind(position)
    }

    inner class SourcesListingViewHolder(itemView: View) : BaseViewHolder(itemView) {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onBind(position: Int) {
            itemView.tvTitle.text = data[position].name
            itemView.tvDescription.text = data[position].description
        }

        override fun onClick(view: View?) {
            onListItemClickListener?.onItemClicked(view, data[adapterPosition])
        }
    }
}
