package com.example.campusbuy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbuy.R
import com.example.campusbuy.databinding.FragmentOrdersBinding
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.ui.adapters.myOffersAdapter
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        getUserDetails()
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUserDetailsFragment(this)
    }

    fun userDetailsFragmentSuccess(user: User) {
        mUserDetails = user
        hideProgressDialog()

        val offersList = mUserDetails.offersOnProducts

        if(offersList.size > 0) {
            rv_my_offers.visibility = View.VISIBLE
            tv_no_offers_found.visibility = View.GONE
            FireStoreClass().getOffersProductList(this@OrdersFragment, user.offersOnProducts)
        }
        else {
            rv_my_offers.visibility = View.GONE
            tv_no_offers_found.visibility = View.VISIBLE
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        rv_my_offers.layoutManager = LinearLayoutManager(activity)

        val adapterOffers = myOffersAdapter(requireActivity(), productsList, this@OrdersFragment, mUserDetails)
        rv_my_offers.adapter = adapterOffers
    }
}