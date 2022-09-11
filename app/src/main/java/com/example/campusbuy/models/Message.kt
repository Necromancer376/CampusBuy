package com.example.campusbuy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Message (
    val user_id: String = "",
    val message: String = "",
    val product_id: String = "",
    var priceSeller: Int = 0,
    var priceBuyer: Int = 0,
    var agreeSeller: Boolean = false,
    var agreeBuyer: Boolean = false
): Parcelable