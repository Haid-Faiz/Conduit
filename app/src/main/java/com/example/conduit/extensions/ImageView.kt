package com.example.conduit.extensions

import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation


fun ImageView.loadImage(url: String) {
    this.load(url) {
        transformations(CircleCropTransformation())
    }
}