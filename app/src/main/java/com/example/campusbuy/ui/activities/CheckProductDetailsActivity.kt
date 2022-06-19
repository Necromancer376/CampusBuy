package com.example.campusbuy.ui.activities

import android.os.Bundle
import android.util.Log
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_check_product_details.*
import kotlinx.android.synthetic.main.activity_product_details.iv_product_detail_image
import kotlinx.android.synthetic.main.activity_product_details.tv_product_details_description
import kotlinx.android.synthetic.main.activity_product_details.tv_product_details_interested_count
import kotlinx.android.synthetic.main.activity_product_details.tv_product_details_price
import kotlinx.android.synthetic.main.activity_product_details.tv_product_details_title
import kotlinx.android.synthetic.main.activity_product_details.tv_product_details_viewed_count

class CheckProductDetailsActivity : BaseActivity() {

    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_product_details)

        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product id", mProductId)
        }
        getProductDetails()
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
        GlideLoader(this@CheckProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
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
}