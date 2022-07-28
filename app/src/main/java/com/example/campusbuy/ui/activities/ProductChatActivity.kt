package com.example.campusbuy.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Message
import com.example.campusbuy.ui.adapters.MessageAdapter
import com.example.campusbuy.utils.Constants
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_product_chat.*

class ProductChatActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDBref: DatabaseReference

    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_chat)

        val name = intent.getStringExtra(Constants.USER_NAME)
        val recieverUid = intent.getStringExtra(Constants.USER_ID)
        val senderUid = FireStoreClass().getCurrentUserId()
        val productId = intent.getStringExtra(Constants.PRODUCT_ID)

        senderRoom = recieverUid + senderUid
        senderRoom = senderUid + recieverUid

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ProductChatActivity, messageList)


        btn_send_msg.setOnClickListener {

            val message = edt_message_box.text.toString()
            val messageObject = Message(senderUid, message, productId!!)
            
        }
    }
}