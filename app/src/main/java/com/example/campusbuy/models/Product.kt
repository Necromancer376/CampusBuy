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
    var seenCount: ArrayList<String> = ArrayList<String>(),
    var isSold: Boolean = false,
    var interested: ArrayList<User> = ArrayList<User>(),
    var bidders: ArrayList<String> = ArrayList<String>(),
    var product_id: String = "",
    var sellerAgree: Boolean = false,
    var buyerAgree: Boolean = false,
    var sellerPrice: Int = 0,
    var buyerPrice: Int = 0
): Parcelable