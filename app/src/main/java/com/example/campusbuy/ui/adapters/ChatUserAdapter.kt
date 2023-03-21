package com.example.campusbuy.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.ui.activities.ProductChatActivity
import com.example.campusbuy.utils.Constants
import com.example.campusbuy.utils.GlideLoader


class ChatUserAdapter(
    val context: Context,
    val userList: ArrayList<User>,
    val product: Product,
    val pid: String
): RecyclerView.Adapter<ChatUserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_recieved_offers, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.userName.text = currentUser.firstName + " " + currentUser.lastName
        holder.price.text = "₹" + product.price.toString()
        GlideLoader(context).loadUserPicture(currentUser.image, holder.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductChatActivity::class.java)
            intent.putExtra(Constants.USER_ID, currentUser.id)
            intent.putExtra(Constants.USER_NAME, (currentUser.firstName + " " + currentUser.lastName))
            intent.putExtra(Constants.PRODUCT_ID, pid)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, currentUser)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.txt_user_name)
        val price = itemView.findViewById<TextView>(R.id.txt_offered_price)
        val img = itemView.findViewById<ImageView>(R.id.iv_user_offer_img)
    }

}