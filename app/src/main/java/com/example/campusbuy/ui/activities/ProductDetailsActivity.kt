package com.example.campusbuy.ui.activities

import android.content.Intent
import android.os.Bundle
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {

    private var mProductId: String = ""
    private lateinit var mProduct: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionBar()
        mProduct = Product()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        getProductDetails()

        btn_see_offer_chat.setOnClickListener {
            val intent = Intent(this@ProductDetailsActivity, RecivedOffersActivity::class.java)
            intent.putExtra("product", mProduct)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, mProductId)
            startActivity(intent)
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_product_details_activity)
        val actionbar = supportActionBar
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_product_details_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    fun productDetailsSuccess(product: Product){
        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_title.text = product.title
        tv_product_details_price.text = product.price
        tv_product_details_description.text = product.description
        tv_product_details_viewed_count.text = product.seenCount.size.toString()
        tv_product_details_interested_count.text = product.interested.size.toString()

        mProduct = product
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }
}
