package com.me.daggersample.extentions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.me.daggersample.R

fun Toast.makeSuccessMessage(context: Context, message: String) {
    this.view = inflateToastLayout(context, message, R.layout.layout_success_toast)
    this.duration = Toast.LENGTH_SHORT
    this.show()
}

fun Toast.makeErrorMessage(context: Context, message: String) {
    this.view = inflateToastLayout(context, message, R.layout.layout_error_toast)
    this.duration = Toast.LENGTH_SHORT
    this.show()
}

fun inflateToastLayout(context: Context, message: String, layout: Int): View {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(layout, null)
    val toastMessage = view.findViewById(R.id.tv_toast_message) as TextView
    toastMessage.text = message
    return view
}