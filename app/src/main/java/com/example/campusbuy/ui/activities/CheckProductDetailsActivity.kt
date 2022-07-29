package com.example.campusbuy.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_check_product_details.*
import kotlinx.android.synthetic.main.activity_settings.*

class CheckProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private lateinit var mProductDetails: Product
    private lateinit var mUserDetails : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_product_details)

        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        var productOwnerId: String = ""
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        getProductDetails()
        getUserDetails()

        btn_offer_chat.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_check_product_details_activity)
        val actionbar = supportActionBar
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_check_product_details_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product

        hideProgressDialog()
        GlideLoader(this@CheckProductDetailsActivity).loadProductPicture(
            product.image,
            iv_check_product_detail_image
        )
        tv_check_product_details_title.text = product.title
        tv_check_product_details_price.text = product.price
        tv_check_product_details_description.text = product.description
        tv_check_product_details_viewed_count.text = product.seenCount.toString()
        tv_check_product_details_interested_count.text = product.interested.size.toString()
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCheckProductDetaiils(this@CheckProductDetailsActivity, mProductId)
    }

    override fun onClick(p0: View?) {
//        mProductDetails.interested.add(mUserDetails)
        val internt_chat = Intent(this@CheckProductDetailsActivity, ProductChatActivity::class.java)
        internt_chat.putExtra(Constants.USER_ID, mProductDetails.user_id)
        internt_chat.putExtra(Constants.USER_NAME, mProductDetails.user_name)
        internt_chat.putExtra(Constants.PRODUCT_ID, mProductDetails.product_id)
        internt_chat.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
        startActivity(internt_chat)
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this@CheckProductDetailsActivity)
    }

    fun userDetailsSuccess(user: User) {

        mUserDetails = user
        hideProgressDialog()
    }
}