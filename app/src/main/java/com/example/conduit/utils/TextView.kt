package com.example.conduit.utils

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*



fun TextView.formatDate(timeStamp: String) {
    val parsedTime: Date? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(timeStamp)
    val formattedDate = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(parsedTime)
    this.text = formattedDate
}


//val TextView.timeStamp: String
//    set(value) {
//
//    }
//    get() {
//
//    }