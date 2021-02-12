package com.me.daggersample.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    /*
    * implemented in view holder and called in onBindViewHolder in adapter
    * USE bind data in views and set views clickListener
    **/
    abstract fun onBind(position: Int)
}
