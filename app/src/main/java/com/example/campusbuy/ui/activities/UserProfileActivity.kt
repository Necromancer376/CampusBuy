package com.example.campusbuy.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.User
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.iv_user_photo
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""
    var campusList: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)



        val typeface: Typeface = Typeface.createFromAsset(assets, "Montserrat-Regular.ttf")
//        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, )

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
             mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_email_profile.isEnabled = false
        et_first_name_profile.setText(mUserDetails.firstName)
        et_last_name_profile.setText(mUserDetails.lastName)
        et_email_profile.setText(mUserDetails.email)
        ac_campus_select.setTypeface(typeface)

        if(mUserDetails.profileCompleted == 0) {
            tv_title_user_profile.text = resources.getString(R.string.title_complete_profile)

            et_first_name_profile.isEnabled = true
            et_last_name_profile.isEnabled = true
        }
        else {
            setupActionBar()
            tv_title_user_profile.text = resources.getString(R.string.title_edit_profile)
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, iv_user_photo)

            et_first_name_profile.isEnabled = true
            et_last_name_profile.isEnabled = true

            if(mUserDetails.mobile != 0L) {
                et_mobile_number_profile.setText(mUserDetails.mobile.toString())
            }
        }

        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_save.setOnClickListener(this@UserProfileActivity)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_user_profile)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_user_profile.setNavigationOnClickListener{ onBackPressed() }
    }

    override fun onClick(view: View) {
        if(view != null) {
            when(view.id) {
                R.id.iv_user_photo -> {
                    if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    }
                    else {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.READ_STORAGE_PERMISSION_CODE
                            )
                    }
                }

                R.id.btn_save -> {

                    if(validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if(mSelectedImageFileUri != null) {
                            FireStoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.USER_PROFILE_IMAGE)
                        }
                        else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()
        val mobileNumber = et_mobile_number_profile.text.toString().trim {it <= ' '}
        val firstName = et_first_name_profile.text.toString().trim {it <= ' '}
        val lastName = et_last_name_profile.text.toString().trim {it <= ' '}

        if(mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if(mobileNumber.isNotEmpty() && mobileNumber.length == 10 && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if(firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRSTNAME] = firstName
        }

        if(lastName != mUserDetails.lastName) {
            userHashMap[Constants.LASTNAME] = lastName
        }

        //TODO implement
        userHashMap[Constants.COMPLETE_PROFILE] = 1
        userHashMap[Constants.CAMPUS] = "VIT Vellore"

        FireStoreClass().updateUserDetails(this, userHashMap)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

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

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if(data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!

//                        iv_user_photo.setImageURI( selectedImageFailUri)
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_user_photo)
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_mobile_number_profile.text.toString().trim{ it<= ' '}) ||
                    et_mobile_number_profile.text.toString().length != 10 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}