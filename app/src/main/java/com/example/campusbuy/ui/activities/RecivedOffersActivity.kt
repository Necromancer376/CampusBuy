package com.example.campusbuy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.campusbuy.R
import kotlinx.android.synthetic.main.activity_recived_offers.*

class RecivedOffersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recived_offers)


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