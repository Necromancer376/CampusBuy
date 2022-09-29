package com.example.campusbuy.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.campusbuy.models.Message
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.ui.activities.*
import com.example.campusbuy.ui.fragments.BaseFragment
import com.example.campusbuy.ui.fragments.DashboardFragment
import com.example.campusbuy.ui.fragments.OrdersFragment
import com.example.campusbuy.ui.fragments.ProductsFragment
import com.example.campusbuy.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class FireStoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()
    var firebaseStorage = FirebaseStorage.getInstance()


    fun registerUser(activity: RegisterActivity, userInfo: User) {

        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering",
                    e
                )
            }
    }

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentId = ""
        if (currentUser != null) {
            currentId = currentUser.uid
        }

        return currentId
    }

    fun getCurrentCampus(): String {
//        var wrapper = String(){ var campus: String = "" }
        var currentCampus: String = ""
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->

                val user = document.toObject(User::class.java)!!
                user.campus.also { currentCampus = it }
            }

        return currentCampus
    }

    fun getUserDetails(activity: Activity) {

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.CAMPUSBUY_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is CheckProductDetailsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is ProductChatActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CheckProductDetailsActivity -> {
                        activity.hideProgressDialog()
                    }
                    is ProductChatActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering",
                    e
                )
            }
    }

    fun updateUserDetails(activity: Activity, userHashMap: HashMap<String, Any>) {

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating profile",
                    e
                )
            }
    }

    fun upadteUserOfferedList(activity: Activity, productId: String, user: User) {

        mFirestore.collection(Constants.USERS)
            .document(user.id)
            .update("offersOnProducts", FieldValue.arrayUnion(productId))
            .addOnSuccessListener { e ->
                when (activity) {
                    is ProductChatActivity -> {
                        activity.hideProgressDialog()
                        activity.offersOnProductsSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is ProductChatActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating profile",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?, imageType: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "." +
                    Constants.getFileExtension(activity, imageFileUri)
        )
        sRef.putFile(imageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Download Image URL ", uri.toString())
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }

                            is AddProductActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product) {
        val newRef = mFirestore.collection(Constants.PRODUCTS).document()

        productInfo.product_id = newRef.id

        newRef.set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding product",
                    e
                )
            }
    }

    fun getUserDetailsFragment(fragment: OrdersFragment) {

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!
                fragment.userDetailsFragmentSuccess(user)

            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while registering", e)
            }
    }

    fun getOffersProductList(fragment: OrdersFragment, pidList: ArrayList<String>) {
        Log.e("pidLis: ", pidList.toString())
        mFirestore.collection(Constants.PRODUCTS)
            .whereIn(Constants.PRODUCT_ID , pidList)
            .get()
            .addOnSuccessListener { document ->
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                fragment.successProductsListFromFireStore(productsList)
            }
    }

    fun getProductsList(fragment: ProductsFragment) {
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products list", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                fragment.successProductsListFromFireStore(productsList)
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.javaClass.simpleName,
                    "error while getting my products"
                )
            }
    }

    fun getDashboardItemsList(fragment: DashboardFragment) {
        mFirestore.collection(Constants.PRODUCTS)
            .whereNotEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products list", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                fragment.successDashBoardItemsList(productsList)
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.javaClass.simpleName,
                    "error while getting dashboard items"
                )
            }
    }

    fun deleteProduct(fragment: ProductsFragment, productId: String, imgUrl: String) {
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "error while deleating",
                    e
                )
            }

//        val photoRef: StorageReference = mFirestore.getReferenceFromUrl(mImageUrl)

    }

    fun getProductDetails(activity: Activity, productId: String) {
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    when(activity) {
                        is ProductDetailsActivity -> {
                            activity.productDetailsSuccess(product)
                        }
                        is ProductChatActivity -> {
                            activity.productDetailsSuccess(product)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                when(activity) {
                    is ProductDetailsActivity -> { activity.hideProgressDialog() }

                    is ProductChatActivity -> { activity.hideProgressDialog() }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "error while deleating",
                    e
                )

            }
    }

    fun getCheckProductDetaiils(activity: CheckProductDetailsActivity, productId: String) {

        Log.e("id", productId)
        activity.hideProgressDialog()
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->

//                Log.e(activity.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)
                if (product != null) {
//                    activity.hideProgressDialog()
                    activity.productDetailsSuccess(product)
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "error while deleating",
                    e
                )

            }
    }

    fun upadteProductList(activity: Activity, productId: String, user: User, listName: String) {
        Log.e("id", productId)
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .update(listName, FieldValue.arrayRemove(user))
            .addOnSuccessListener { e ->
                when (activity) {
                    is CheckProductDetailsActivity -> {
                        if (listName.equals(Constants.PRODUCT_INTERESTED)) {
                            activity.productInterestedSuccess()
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CheckProductDetailsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating profile",
                    e
                )
            }
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .update(listName, FieldValue.arrayUnion(user))
            .addOnSuccessListener { e ->
                when (activity) {
                    is CheckProductDetailsActivity -> {
                        if (listName.equals(Constants.PRODUCT_INTERESTED)) {
                            activity.productInterestedSuccess()
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CheckProductDetailsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating profile",
                    e
                )
            }
    }

    fun upadteProductSeenList(
        activity: Activity,
        productId: String,
        uid: String,
        listName: String
    ) {

        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .update(listName, FieldValue.arrayRemove(uid))
            .addOnSuccessListener {
                when (activity) {
                    is CheckProductDetailsActivity -> {
                        if (listName.equals(Constants.PRODUCT_INTERESTED)) {
                            activity.hideProgressDialog()
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CheckProductDetailsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating profile",
                    e
                )
            }
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .update(listName, FieldValue.arrayUnion(uid))
            .addOnSuccessListener { e ->
                when (activity) {
                    is CheckProductDetailsActivity -> {
                        if (listName.equals(Constants.PRODUCT_INTERESTED)) {
                            activity.hideProgressDialog()
                            activity.productInterestedSuccess()
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CheckProductDetailsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating profile",
                    e
                )
            }
    }

    fun updateProducBoolean(activity: Activity, productId: String, field: String, status: Boolean) {
        Log.e("bool", status.toString())
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .update(field, status)
            .addOnSuccessListener {
                when(activity) {
                    is ProductChatActivity -> {
                        Log.e("here", field.toString())
//                        activity.hideProgressDialog()
                        activity.getUpdatatedProduct()
                    }
                }
            }
            .addOnFailureListener { e ->
                when(activity) {
                    is ProductChatActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating profile",
                    e
                )
            }
    }

    fun getCampusList(activity: Activity): ArrayList<String> {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child("campus.txt")
        var array: ArrayList<String> = ArrayList<String>()
        val tempFile = File.createTempFile("campus", "txt")

        sRef.getFile(tempFile).addOnSuccessListener {
            tempFile.forEachLine {
                array.add(it)
            }
        }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Error while downloading file")
            }

        return array
    }

    fun updateCampusList(activity: Activity, newName: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child("campus.txt")
        var array: ArrayList<String> = ArrayList<String>()
        val tempFile = File.createTempFile("campus", "txt")


    }
}