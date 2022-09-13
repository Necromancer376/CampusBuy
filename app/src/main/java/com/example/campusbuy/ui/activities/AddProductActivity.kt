package com.example.campusbuy.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.Constants.getImageUriFromBitmap
import com.example.campusbuy.utils.Constants.rotate
import com.example.campusbuy.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedImageFileUri: Uri? = null
    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionBar()

        iv_add_update_product_galary.setOnClickListener(this@AddProductActivity)
        iv_add_update_product.setOnClickListener(this@AddProductActivity)
        btn_submit_product.setOnClickListener(this@AddProductActivity)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_add_product_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_add_product_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    override fun onClick(view: View?) {

        if(view != null) {

            when(view.id) {
                R.id.iv_add_update_product_galary -> {
                    if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                        Constants.showImageChooser(this@AddProductActivity)
                    }
                    else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.iv_add_update_product -> {
                    if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED) {
                        Constants.takePicture(this@AddProductActivity)
                    }
                    else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA),
                            Constants.CAMERA_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit_product -> {
                    if(validateProductDetails()) {
                        uploadProductImage()
                    }
                }
            }
        }
    }

    // Image related functions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == Constants.CAMERA_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.takePicture(this)
            }
            else {
                Toast.makeText(this,
                    "Permission To Denied Go to Settings to Give Permisson",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            }
            else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Constants.CAMERA) {
                if(data != null) {
                    try {

                        val img = data!!.extras!!.get("data")
                        Log.e("orientation", "error")
                        val orientation = getOrientation(img.toString())
                        Log.e("orientation", "success ${orientation}")
                        val imgBitmap = img as Bitmap
                        val newImgBitmap = imgBitmap.rotate(orientation.toFloat())
                        mSelectedImageFileUri = getImageUriFromBitmap(this, newImgBitmap)
                        GlideLoader(this@AddProductActivity).loadProductPicture(mSelectedImageFileUri!!, iv_product_image)
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddProductActivity,
                            resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if(data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadProductPicture(mSelectedImageFileUri!!, iv_product_image)
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddProductActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    //Edit text Functions
    private fun validateProductDetails(): Boolean {
        return when {
            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
                false
            }
            TextUtils.isEmpty(et_product_title.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }
            TextUtils.isEmpty(et_product_description.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_description), true)
                false
            }
            TextUtils.isEmpty(et_product_tag.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_tag), true)
                false
            }
            else -> {
                true
            }
        }
    }

    //Uploading to firestore

    private fun uploadProductImage() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.PRODUCT_IMAGE)
    }

    fun productUploadSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@AddProductActivity,
            resources.getString(R.string.product_upload_success_msg),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {

        showErrorSnackBar("uploaded ${imageURL}", false)
        mProductImageURL = imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails() {

        val username = this.getSharedPreferences(
            Constants.CAMPUSBUY_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME, "")!!

        val product = Product(
            FireStoreClass().getCurrentUserId(),
            username,
            et_product_title.text.toString().trim { it <= ' ' },
            et_product_price.text.toString().trim { it <= ' ' },
            et_product_description.text.toString().trim { it <= ' ' },
            et_product_tag.text.toString().trim { it <= ' ' },
            mProductImageURL,
            FireStoreClass().getCurrentCampus(),
        )

        FireStoreClass().uploadProductDetails(this@AddProductActivity, product)
    }

    private fun getOrientation(img: String):Int {
        var ei = ExifInterface(img)
        var orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                return 90
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                return 180
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                return 270
            }
            ExifInterface.ORIENTATION_NORMAL -> {
                return 0
            }
        }
        return 0
    }
}