package com.example.campusbuy.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_check_product_details.*

class CheckProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private var productOwnerId: String = ""
    private lateinit var mProductDetails: Product
    private lateinit var mUserDetails : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_product_details)

        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

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

        hideProgressDialog()
        mProductDetails = product

        GlideLoader(this@CheckProductDetailsActivity).loadProductPicture(
            product.image,
            iv_check_product_detail_image
        )
        tv_check_product_details_title.text = product.title
        tv_check_product_details_price.text = product.price
        tv_check_product_details_description.text = product.description
        tv_product_seller.text = product.user_name
        tv_check_product_details_viewed_count.text = product.seenCount.size.toString()
        tv_check_product_details_interested_count.text = product.interested.size.toString()

        if(mProductDetails.sold) {
            btn_offer_chat.isVisible = false
            sold_overlay.isVisible = true
        }
        else {
            btn_offer_chat.isVisible = true
            sold_overlay.isVisible = false
        }
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCheckProductDetaiils(this@CheckProductDetailsActivity, mProductId)
    }

    override fun onClick(view: View?) {
        if(view != null) {
            when (view.id) {
                R.id.btn_offer_chat -> {
                    if(mProductDetails.user_id != mUserDetails.id) {
                        updateInterestedArray()
                    }
                }
            }
        }
    }


    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this@CheckProductDetailsActivity)
    }

    fun userDetailsSuccess(user: User) {
        hideProgressDialog()
        mUserDetails = user
        FireStoreClass().upadteProductSeenList(this@CheckProductDetailsActivity, mProductId , mUserDetails!!.id, Constants.PRODUCT_SEENCOUNT)
    }

    fun productInterestedSuccess() {
        hideProgressDialog()
        val internt_chat = Intent(this@CheckProductDetailsActivity, ProductChatActivity::class.java)
        internt_chat.putExtra(Constants.USER_ID, mProductDetails.user_id)
        internt_chat.putExtra(Constants.USER_NAME, mProductDetails.user_name)
        internt_chat.putExtra(Constants.PRODUCT_ID, mProductId)
        internt_chat.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
        startActivity(internt_chat)
    }

    fun updateInterestedArray() {
        FireStoreClass().upadteProductList(this@CheckProductDetailsActivity, mProductId , mUserDetails, Constants.PRODUCT_INTERESTED)
    }
}