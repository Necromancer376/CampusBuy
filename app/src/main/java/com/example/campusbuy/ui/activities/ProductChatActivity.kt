package com.example.campusbuy.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbuy.R
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Message
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.ui.adapters.MessageAdapter
import com.example.campusbuy.utils.Constants
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_product_chat.*

class ProductChatActivity : BaseActivity() {

    private lateinit var currentUser: User
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDBref: DatabaseReference
    private lateinit var mUserDetails: User
    private lateinit var mProductDetails: Product

    private lateinit var name: String
    private lateinit var recieverUid: String
    private lateinit var senderUid: String
    private lateinit var productId: String

    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_chat)

        mDBref = FirebaseDatabase.getInstance("https://campusbuy-79e1a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()

        setupActionBar()
        getUserDetails()
        mProductDetails = Product()

        name = intent.getStringExtra(Constants.USER_NAME).toString()
        recieverUid = intent.getStringExtra(Constants.USER_ID).toString()
        senderUid = FireStoreClass().getCurrentUserId().toString()
        mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        productId = intent.getStringExtra(Constants.PRODUCT_ID).toString()
        getProductDetails()

        tv_chat_title.text = name
        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ProductChatActivity, messageList)

        rv_chat.layoutManager = LinearLayoutManager(this@ProductChatActivity)
        rv_chat.adapter = messageAdapter

        if(mProductDetails.sellerAgree) {
            btn_agree_seller.setBackgroundColor(R.color.button_agree_green)
        }
        else {
            btn_agree_seller.setBackgroundColor(R.color.button_agree_red)
        }
        if(mProductDetails.buyerAgree) {
            btn_agree_buyer.setBackgroundColor(R.color.button_agree_green)
        }
        else {
            btn_agree_buyer.setBackgroundColor(R.color.button_agree_red)
        }

        if(mProductDetails.sellerAgree && mProductDetails.buyerAgree) {
            setProductBooleans("isSold", true)
        }

        btn_agree_seller.setOnClickListener {
            setProductBooleans("sellerAgree", !mProductDetails.sellerAgree)
        }

        btn_agree_buyer.setOnClickListener {
            setProductBooleans("buyerAgree", !mProductDetails.buyerAgree)
        }

        mDBref.child("chats").child(productId).child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children) {
                        val msg = postSnapshot.getValue(Message::class.java)
                        messageList.add(msg!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        btn_send_msg.setOnClickListener {

            val message = edt_message_box.text.toString()
            val messageObject = Message(senderUid, message, productId)

            if(!message.equals("")) {
                mDBref.child("chats").child(productId).child(senderRoom!!).child("messages").push()
                    .setValue(messageObject)
                    .addOnSuccessListener {
                        mDBref.child("chats").child(productId).child(recieverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                    .addOnFailureListener {
                        Log.e("chat: ", "fail")
                    }
                edt_message_box.setText("")

                if(mProductDetails.user_id != mUserDetails.id){
                    FireStoreClass().upadteUserOfferedList(this@ProductChatActivity, productId, currentUser)
                }
            }
        }
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        hideProgressDialog()
        currentUser = user
    }

    fun offersOnProductsSuccess() {
        hideProgressDialog()
//        Log.e("offer", "success")
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

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProductDetails(this@ProductChatActivity, productId)
    }

    fun productDetailsSuccess(product: Product) {
        hideProgressDialog()
        mProductDetails = product
        Log.i("Product Details", mProductDetails.toString())
    }

    private fun setProductBooleans(field: String, status: Boolean) {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().updateProducBoolean(this@ProductChatActivity, productId, field, status)
    }

    fun getUpdatatedProduct() {
        hideProgressDialog()
        Log.i("Product Details", mProductDetails.toString())
        getProductDetails()
    }
}