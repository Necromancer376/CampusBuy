package com.example.campusbuy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    val user_id: String = "",
    val user_name: String = "",
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val tag: String = "",
    val image: String = "",
    var campus: String = "",
    var product_id: String = "",
    var interested: ArrayList<User>,
    var bidders: ArrayList<User>,
    var seenCount: Int = 0,
    var isSold: Boolean = false
): Parcelable