package com.example.campusbuy.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Message (
    val user_id: String = "",
    val message: String = "",
    val product_id: String = "",
    val id: String = ""
): Parcelable