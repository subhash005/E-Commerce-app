package com.example.admin_e_commerce.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.admin_e_commerce.R
import com.example.admin_e_commerce.Utils
import com.example.admin_e_commerce.adapter.AdapterCartProducts
import com.example.admin_e_commerce.databinding.FragmentOrderDetailBinding
import com.example.admin_e_commerce.model.Orders
import com.example.admin_e_commerce.viewmodels.AdminViewModel
import kotlinx.coroutines.launch

class OrderDetailFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailBinding
    private val viewModel : AdminViewModel by viewModels()
    private lateinit var adapterCartProducts: AdapterCartProducts
    private var status = 0
    private var currentStatus = 0
    private var orderId=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderDetailBinding.inflate(layoutInflater)
        getValues()
        settingStatus(status)
        setStatusBarColor()
        onBackButtonClicked()
        onChangeStatusButtonClicked()

        lifecycleScope.launch {
            getOrderedProducts()
        }


        return binding.root
    }

    private fun onChangeStatusButtonClicked() {
        binding.btnChangeStatus.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)

            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {menu->
                when(menu.itemId){

                    R.id.menuReceived->{
                        currentStatus=1
                        if (currentStatus>status){
                            status=1
                            settingStatus (1)
                            viewModel.updateOrderStatus(orderId,1)
                        }else{
                            Utils.showToast(requireContext(), "Order is already received...")
                        }
                        true
                    }
                    R.id.menuDispatched->{
                        currentStatus=2
                        if (currentStatus>status){
                            status=2
                            settingStatus (2)
                            viewModel.updateOrderStatus(orderId,2)
                        }else{
                            Utils.showToast(requireContext(), "Order is already dispatched...")
                        }
                        true
                    }
                    R.id.menuDelivered->{
                        currentStatus=3
                        if (currentStatus>status){
                            status=3
                            settingStatus (3)
                            viewModel.updateOrderStatus(orderId,3)
                        }else{
                            Utils.showToast(requireContext(), "Order is already delivered...")
                        }
                        true
                    }

                    else -> {false}
                }
            }
        }
    }

    suspend fun getOrderedProducts() {
        viewModel.getOrderedProducts(orderId).collect{cartList->
            adapterCartProducts=AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartList)
        }
    }

    private fun settingStatus(status : Int) {
        when(status){
            0->{
                binding. iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
            }
            1->{
                binding. iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding. iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
            }
            2->{
                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R. color.blue)
                binding.iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R. color. blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding.iv3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
            }
            3->{
                binding. iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding. iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding.iv3.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding. iv4.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
                binding.view3.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.blue)
            }
        }
    }

    private fun getValues() {
        val bundle = arguments
        status= bundle?.getInt("status")!!
        orderId=bundle.getString("orderId").toString()
        binding.tvUserAddress.text=bundle.getString("userAddress").toString()
    }

    private fun onBackButtonClicked() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDetailFragment_to_orderFragment)
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