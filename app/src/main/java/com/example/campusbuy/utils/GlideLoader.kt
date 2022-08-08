package com.example.campusbuy.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.example.campusbuy.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(imageURI: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(imageURI)
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageView)
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadProductPicture(imageURI: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(imageURI)
                .centerCrop()
                .into(imageView)
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
    }
}