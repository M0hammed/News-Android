package com.me.daggersample.base

import android.view.View

interface OnListItemClickListener<T> {
    fun onItemClicked(view: View?, model: T)
}
