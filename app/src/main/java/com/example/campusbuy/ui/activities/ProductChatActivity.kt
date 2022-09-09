package com.example.campusbuy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Message
import com.example.campusbuy.models.User
import com.example.campusbuy.ui.adapters.MessageAdapter
import com.example.campusbuy.utils.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_product_chat.*

class ProductChatActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDBref: DatabaseReference
    private lateinit var mUserDetails: User

    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_chat)

        mDBref = FirebaseDatabase.getInstance().getReference()

        setupActionBar()

        val name = intent.getStringExtra(Constants.USER_NAME)
        val recieverUid = intent.getStringExtra(Constants.USER_ID)
        val senderUid = FireStoreClass().getCurrentUserId()
        val productId = intent.getStringExtra(Constants.PRODUCT_ID)

        tv_chat_title.text = name

        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ProductChatActivity, messageList)

        mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!


        btn_send_msg.setOnClickListener {

            val message = edt_message_box.text.toString()
            val messageObject = Message(senderUid, message, productId!!)

            Log.e("chat: ", "clicked")
            mDBref.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject)
                .addOnSuccessListener {
                    Log.e("chat: ", "Success")
                    mDBref.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
                .addOnFailureListener {
                    Log.e("chat: ", "fail")
                }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_product_chat_activity)
        val actionbar = supportActionBar
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_product_chat_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}