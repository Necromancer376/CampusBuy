package com.example.campusbuy.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream


object Constants {
    const val USERS: String = "users"
    const val PRODUCTS: String = "products"

    const val CAMPUSBUY_PREFERENCES: String = "CampusBuyPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val CAMERA_PERMISSION_CODE = 1
    const val CAMERA = 2

    const val MOBILE: String = "mobile"
    const val USER_NAME: String = "user_name"
    const val FIRSTNAME: String = "firstName"
    const val LASTNAME: String = "lastName"
    const val USER_PROFILE_IMAGE = "User_Profile_Image"
    const val IMAGE: String = "image"
    const val CAMPUS: String = "campus"
    const val COMPLETE_PROFILE: String = "profileCompleted"

    const val PRODUCT_IMAGE: String = "Product_Image"
    const val PRODUCT_INTERESTED: String= "interested"

    const val USER_ID: String = "user_id"
    const val PRODUCT_ID: String = "product_id"

    const val EXTRA_PRODUCT_ID = "extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID = "extra_product_owner_id"

    const val ITEM_RECIEVED = 1
    const val ITEM_SENT = 2


    fun showImageChooser(activity: Activity) {
        val gallaryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(gallaryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun takePicture(activity: Activity) {
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(intentCamera, CAMERA)
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {

        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}