package com.example.campusbuy.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusbuy.R
import com.example.campusbuy.models.User


class ChatUserAdapter(
    val context: Context,
    val userList: ArrayList<User>
): RecyclerView.Adapter<ChatUserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_recieved_offers, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        if(holder is UserViewHolder) {
//            holder.userName =
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextureView>(R.id.txt_user_name)
        val price = itemView.findViewById<TextureView>(R.id.txt_offered_price)
    }

}