package com.example.campusbuy.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.ui.adapters.ChatUserAdapter
import com.example.campusbuy.utils.Constants

import kotlinx.android.synthetic.main.activity_recived_offers.*

class RecivedOffersActivity : BaseActivity() {

    private lateinit var productId: String
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: ChatUserAdapter
    private lateinit var productDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recived_offers)

        setupActionBar()

        productDetails = Product()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            productId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        if(intent.hasExtra("product")) {
            productDetails = intent.getParcelableExtra<Product>("product")!!
        }

        userList = productDetails.interested
        adapter = ChatUserAdapter(this@RecivedOffersActivity, userList, productDetails, productId)

        rv_received_offers_user.layoutManager = LinearLayoutManager(this@RecivedOffersActivity)
        rv_received_offers_user.adapter = adapter
    }


    private fun setupActionBar() {

        setSupportActionBar(toolbar_recieved_offeres_activity)
        val actionbar = supportActionBar
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_recieved_offeres_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}