package com.me.daggersample.ui.HeadLines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.me.daggersample.R
import com.me.daggersample.base.BaseAdapter
import com.me.daggersample.base.BaseViewHolder
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.model.headLine.HeadLineModel

class HeadLinesAdapter(
    context: Context,
    onListItemClickListener: OnListItemClickListener<HeadLineModel>
) : BaseAdapter<HeadLineModel>(context, onListItemClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_head_line, parent, false)
        return HeadLineViewHolder(view)
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class HeadLineViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBind(position: Int) {

        }

        override fun onClick(p0: View?) {

        }

    }
}