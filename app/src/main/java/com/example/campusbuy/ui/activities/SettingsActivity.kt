package com.example.campusbuy.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.User
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupActionBar()

        tv_edit.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {

        mUserDetails = user
        hideProgressDialog()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_email.text = user.email
        tv_mobile_number.text = "${user.mobile}"
        tv_campus.text = user.campus
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(view: View?) {
        if(view != null) {
            when (view.id) {
                R.id.tv_edit -> {
                    val intentProfile = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intentProfile.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intentProfile)
                }

                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intentLogin = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intentLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentLogin)
                    finish()
                }
            }
        }
    }
}