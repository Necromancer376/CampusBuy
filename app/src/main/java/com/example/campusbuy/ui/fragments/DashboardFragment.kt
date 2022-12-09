package com.example.campusbuy.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.campusbuy.R
//import com.example.campusbuy.activities.databinding.FragmentDashboardBinding
import com.example.campusbuy.databinding.FragmentDashboardBinding
import com.example.campusbuy.firestore.FireStoreClass
import com.example.campusbuy.models.Product
import com.example.campusbuy.models.User
import com.example.campusbuy.ui.activities.CheckProductDetailsActivity
import com.example.campusbuy.ui.activities.SettingsActivity
import com.example.campusbuy.ui.adapters.DashboardItemsListAdapter
import com.example.campusbuy.utils.Constants
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
        btn_search.setOnClickListener {
            getDashboardItemsList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun successDashBoardItemsList(dashboardItemList: ArrayList<Product>) {
        hideProgressDialog()

        val itemList: ArrayList<Product>
        val search = getSearchQuerry()

        if(search.isEmpty()) {
            itemList = dashboardItemList
        }
        else {
            itemList = ArrayList<Product>()
            for(i in dashboardItemList) {
                if(i.title.lowercase().contains(search.lowercase()) ||
                    i.description.lowercase().contains(search.lowercase())) {
                    itemList.add(i)
                }
            }
        }

        if(dashboardItemList.isNotEmpty()) {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapterDashBoardProducts = DashboardItemsListAdapter(requireActivity(), itemList)
            rv_dashboard_items.adapter = adapterDashBoardProducts

            adapterDashBoardProducts.setOnClickListener(object: DashboardItemsListAdapter.OnClickListener {
                override fun onClick(position: Int, product: Product) {
                    val intent = Intent(context, CheckProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                    context?.startActivity(intent)
                }
            })
        }
        else{
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }

    private fun getDashboardItemsList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().getDashboardItemsList(this@DashboardFragment, mUserDetails.campus)
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUserDetailsFragment(this@DashboardFragment)
    }

    fun userDetailsFragmentSuccess(user: User) {
        mUserDetails = user
        hideProgressDialog()
        getDashboardItemsList()
    }

    fun getSearchQuerry(): String {
        return edt_search_box.text.toString()
    }
}