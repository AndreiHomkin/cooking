package com.example.cook

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat

fun applyTextColor(context: Context, view: TextView, isDarkMode: Boolean) {
    view.setTextColor(ContextCompat.getColor(context, if (isDarkMode) R.color.white else R.color.black))
}
