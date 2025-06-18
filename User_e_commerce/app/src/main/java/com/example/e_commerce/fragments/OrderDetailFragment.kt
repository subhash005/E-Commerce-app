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
import com.example.e_commerce.adapters.AdapterCartProducts
import com.example.e_commerce.databinding.FragmentOrderDetailBinding
import com.example.e_commerce.databinding.FragmentOrdersBinding
import com.example.e_commerce.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class orderDetailFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailBinding
    private val viewModel : UserViewModel by viewModels()
    private lateinit var adapterCartProducts: AdapterCartProducts
    private var status = 0
    private var orderId=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderDetailBinding.inflate(layoutInflater)
        getValues()
        settingStatus()
        setStatusBarColor()
        onBackButtonClicked()

        lifecycleScope.launch {
            getOrderedProducts()
        }


        return binding.root
    }

    suspend fun getOrderedProducts() {
        viewModel.getOrderedProducts(orderId).collect{cartList->
            adapterCartProducts=AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartList)
        }
    }

    private fun settingStatus() {
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
    }

    private fun onBackButtonClicked() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDetailFragment_to_ordersFragment)
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