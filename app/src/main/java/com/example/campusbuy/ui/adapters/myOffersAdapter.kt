package com.example.campusbuy.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.campusbuy.R
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.ui.activities.ProductChatActivity
import com.example.campusbuy.ui.fragments.OrdersFragment
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import kotlinx.android.synthetic.main.item_my_offers.view.*

class myOffersAdapter (
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: OrdersFragment,
    private val currentUser: User
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_my_offers,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "₹${model.price}"

            if(model.sold) {
                Log.e("here: ", "overlay")
                holder.itemView.foreground = ContextCompat.getDrawable(context, R.drawable.sold_overlay1)
            }
            else {
                holder.itemView.foreground = null
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductChatActivity::class.java)
                intent.putExtra(Constants.USER_ID, model.user_id)
                intent.putExtra(Constants.USER_NAME, (currentUser.firstName + " " + currentUser.lastName))
                intent.putExtra(Constants.PRODUCT_ID, model.product_id)
                intent.putExtra(Constants.EXTRA_USER_DETAILS, currentUser)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}