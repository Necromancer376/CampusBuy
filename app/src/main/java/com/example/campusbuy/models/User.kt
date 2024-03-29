package com.example.campusbuy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0L,
    val profileCompleted: Int = 0,
    val campus: String = "",
    var sellingProducts: ArrayList<Product> = ArrayList<Product>(),
    var offersOnProducts: ArrayList<String> = ArrayList<String>()
): Parcelable