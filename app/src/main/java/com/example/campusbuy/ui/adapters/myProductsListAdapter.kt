package com.example.campusbuy.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.campusbuy.R
import com.example.campusbuy.models.Product
import com.example.campusbuy.ui.activities.ProductDetailsActivity
import com.example.campusbuy.ui.fragments.ProductsFragment
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_list_layout.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.iv_item_image
import kotlinx.android.synthetic.main.item_list_layout.view.tv_item_name
import kotlinx.android.synthetic.main.item_list_layout.view.tv_item_price

class myProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: ProductsFragment

): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val user = FirebaseAuth.getInstance().currentUser


        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        val imgUrl = model.image

        if(holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "₹${model.price}"
            holder.itemView.tv_product_viewed_count.text = model.seenCount.size.toString()
            holder.itemView.tv_product_interested_count.text = model.interested.size.toString()

            holder.itemView.ib_delete_product.setOnClickListener{
                fragment.deleteProduct(model.product_id, imgUrl)
            }

            if(model.sold) {
                Log.e("here: ", "overlay")
                holder.itemView.sold_overlay_products.isVisible = true
            }
            else {
                holder.itemView.sold_overlay_products.isVisible = false
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                Log.e("pid: ", model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}