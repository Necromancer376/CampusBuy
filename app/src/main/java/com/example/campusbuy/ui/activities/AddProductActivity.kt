package com.example.campusbuy.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.campusbuy.R
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionBar()

        iv_add_update_product_galary.setOnClickListener(this@AddProductActivity)
        iv_add_update_product.setOnClickListener(this@AddProductActivity)
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
            }
        }
    }

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Activity.RESULT_OK) {
            if(requestCode == Constants.CAMERA || requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if(data != null) {
                    try {
//                        val mSelectedImageFileUri = data!!.extras!!.get("data")!!
                        mSelectedImageFileUri = data.data!!
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
//            else if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
//                if(data != null) {
//                    try {
//                        val mSelectedImageFileUri = data.data!!
//                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri, iv_product_image)
//                    }
//                    catch (e: IOException) {
//                        e.printStackTrace()
//                        Toast.makeText(
//                            this@AddProductActivity,
//                            resources.getString(R.string.image_selection_failed),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
        }
    }
}