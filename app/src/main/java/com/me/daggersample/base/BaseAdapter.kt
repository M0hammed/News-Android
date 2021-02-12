package com.me.daggersample.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<item>(
    val context: Context,
    val onListItemClickListener: OnListItemClickListener<item>?
) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var data: ArrayList<item>
    protected var isLoadingAdded = false

    init {
        data = ArrayList()
    }

    fun insertAll(insertedItemList: ArrayList<item>) {
        data = insertedItemList
        notifyDataSetChanged()
    }

    fun updateAll(insertedItemList: ArrayList<item>) {
        data.clear()
        data = insertedItemList
        notifyDataSetChanged()
    }

    fun update(item: item, position: Int) {
        data.removeAt(position)
        data.add(position, item)
        notifyItemChanged(position)
    }

    fun deleteAll() {
        data.clear()
        notifyDataSetChanged()
    }

    fun deleteItem(item: item) {
        data.remove(item)
        notifyDataSetChanged()
    }

    fun addLoadingFooter(item: item) { // can add empty object
        isLoadingAdded = true
        data.add(item)
        notifyItemInserted(data.size - 1)
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = data.size - 1
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}
