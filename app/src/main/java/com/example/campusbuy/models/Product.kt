package com.example.campusbuy.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
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
    var seenCount: Int = 0,
    var isSold: Boolean = false,
    var interested: ArrayList<String> = ArrayList<String>(),
    var bidders: ArrayList<String> = ArrayList<String>(),
    var product_id: String = ""
): Parcelable