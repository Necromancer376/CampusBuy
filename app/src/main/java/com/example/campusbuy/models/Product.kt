package com.example.campusbuy.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

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
    var sold: Boolean = false,
    var interested: ArrayList<String> = ArrayList<String>(),
    var bidders: ArrayList<String> = ArrayList<String>(),
    var buyerAgree: ArrayList<String> = ArrayList<String>(),
    var sellerAgree: ArrayList<String> = ArrayList<String>(),
    var buyer_id: String = "",
    var soldDate: Long = 0L,
    var product_id: String = "",
): Parcelable