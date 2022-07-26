package com.example.campusbuy.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusbuy.models.Message
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.utils.Constants
import com.google.firebase.auth.FirebaseAuth


class MessageAdapter(
    val context: Context,
    val messageList: ArrayList<Message>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.msg_recieved, parent, false)
            return RecievedViewHolder(view)
        }
        else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.msg_sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }
        else {
            val viewHolder = holder as RecievedViewHolder
            holder.recievedMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        if(FireStoreClass().getCurrentUserId().equals(currentMessage.user_id)) {
            return Constants.ITEM_SENT
        }
        else {
            return Constants.ITEM_RECIEVED
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val sentMessage = itemView.findViewById<com.example.campusbuy.utils.CBTextView>(R.id.txt_sent_msg)
    }

    class RecievedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val recievedMessage = itemView.findViewById<com.example.campusbuy.utils.CBTextView>(R.id.txt_recieved_msg)
    }
}