package com.example.e_commerce.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.adapters.AdapterOrders
import com.example.e_commerce.databinding.FragmentOrdersBinding
import com.example.e_commerce.models.OrderedItems
import com.example.e_commerce.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class ordersFragment : Fragment() {
    private val viewModel : UserViewModel by viewModels()
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var adapterOrders: AdapterOrders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentOrdersBinding.inflate(layoutInflater)
        onBackButtonClicked()
        setStatusBarColor()
        getAllOrders()

        return binding.root
    }

    private fun getAllOrders() {
        binding.shimmerViewContainer.visibility=View.VISIBLE
        lifecycleScope.launch {
            viewModel.getAllOrders().collect{orderList->

                if (orderList.isNotEmpty()){
                    val orderedList = ArrayList<OrderedItems>()
                    for (orders in orderList){
                        val title = StringBuilder()
                        var totalPrice = 0
                        for(products in orders.orderList!!){
                            val price = products.productPrice?.substring(1)?.toInt()
                            val itemCount = products.productCount!!
                            totalPrice+=(price?.times(itemCount)!!)

                            title.append ("${products.productCategory}, ")
                        }

                        val orderedItems = OrderedItems(orders.orderId, orders.orderDate, orders. orderStatus, title.toString() , totalPrice)
                        orderedList.add(orderedItems)
                    }
                    adapterOrders=AdapterOrders(requireContext(),::onOrderItemViewClicked)
                    binding.rvOrders.adapter=adapterOrders
                    adapterOrders.differ.submitList(orderedList)
                    binding.shimmerViewContainer.visibility=View.GONE

                }

            }
        }

    }

    fun onOrderItemViewClicked(orderedItems: OrderedItems){
        val bundle = Bundle()
        bundle.putInt("status" , orderedItems.itemStatus!!)
        bundle.putString("orderId" , orderedItems.orderId)

        findNavController().navigate(R.id.action_ordersFragment_to_orderDetailFragment,bundle)
    }

    private fun onBackButtonClicked() {
        binding.tbProfileFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_ordersFragment_to_profileFragment)
        }
    }
    private fun setStatusBarColor() {
        activity?.window?.apply {
            // Set the status bar color
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)

            // If the Android version is Marshmallow (API 23) or higher, set the light status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }


}