package com.example.campusbuy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Message (
    val user_id: String = "",
    val product_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    val message: String = "",
    val id: String = ""
): Parcelable