package com.example.cook

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.security.MessageDigest

fun applyTextColor(context: Context, view: TextView, isDarkMode: Boolean) {
    view.setTextColor(ContextCompat.getColor(context, if (isDarkMode) R.color.white else R.color.black))
}
fun hashPassword(password: String): String {
    val bytes = password.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}